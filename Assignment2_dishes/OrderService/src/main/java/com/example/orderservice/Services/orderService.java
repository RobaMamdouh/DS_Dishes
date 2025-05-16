package com.example.orderservice.Services;

import com.example.orderservice.DTO.CreateOrderRequest;
import com.example.orderservice.DTO.ReduceDishesDTO;
import com.example.orderservice.DTO.SoldDishInfo;
import com.example.orderservice.Models.dishesModel;
import com.example.orderservice.Models.status;
import com.example.orderservice.Repo.DishesRepo;
import com.example.orderservice.Repo.orderRepo;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.orderservice.Models.orderModel;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class orderService {
    @Autowired
    private orderRepo orderRepo;

    @Autowired
    private DishesRepo dishesRepo;

    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private final String DISH_SERVICE_URL = "http://localhost:8081/DishesService-1.0-SNAPSHOT/api/dishes/";


    public void publishPaymentFailedEvent(long userId, double amount) {
        String message = "Payment failed for user ID: " + userId + ", Amount: " + amount;
        rabbitTemplate.convertAndSend("payments", "PaymentFailed", message);
    }

    public void logEvent(String serviceName, String severity, String message) {
        String routingKey = serviceName + "." + severity;
        try {
            rabbitTemplate.convertAndSend("log", routingKey, message);
            System.out.println("Sent log event with routing key: " + routingKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ResponseEntity<?> createOrder(CreateOrderRequest request) {
        List<dishesModel> dishList = new ArrayList<>();
        List<ReduceDishesDTO> reduceDishesList = new ArrayList<>();
        double totalPrice = 0;
        double MINIMUM_ORDER_AMOUNT = 50.0;
        boolean shouldCancel = false;
        String cancelReason = null;

        // Convert String keys to Long for dishIdQuantityMap
        Map<Long, Integer> dishIdQuantityMap = new HashMap<>();
        for (Map.Entry<String, Integer> entry : request.getDishIdQuantityMap().entrySet()) {
            try {
                dishIdQuantityMap.put(Long.parseLong(entry.getKey()), entry.getValue());
            } catch (NumberFormatException e) {
                logEvent("OrderService", "ERROR", "Invalid dish ID: " + entry.getKey());
            }
        }

        // Step 1: Fetch and validate dishes
        for (Map.Entry<Long, Integer> entry : dishIdQuantityMap.entrySet()) {
            Long dishId = entry.getKey();
            int quantity = entry.getValue();

            try {
                String url = DISH_SERVICE_URL + dishId;
                dishesModel fetchedDish = restTemplate.getForObject(url, dishesModel.class);

                if (fetchedDish != null) {
                    if (quantity > fetchedDish.getQuantity()) {
                        shouldCancel = true;
                        cancelReason = "Requested quantity exceeds stock";
                        logEvent("OrderService", "ERROR", cancelReason);
                        continue;
                    }

                    dishesModel dish = new dishesModel();
                    dish.setDishName(fetchedDish.getDishName());
                    dish.setQuantity(quantity);
                    dish.setPrice(fetchedDish.getPrice() * quantity);
                    dish.setUserId(fetchedDish.getUserId());
                    totalPrice += dish.getPrice();
                    dishList.add(dish);
                    reduceDishesList.add(new ReduceDishesDTO(dishId, quantity));
                }
            } catch (Exception ex) {
                logEvent("OrderService", "ERROR", "Failed to fetch dish ID " + dishId + ": " + ex.getMessage());
            }
        }

        // Step 2: Validations
        // Only set the first error encountered, and do not overwrite it with later checks
        if (dishList.isEmpty()) {
            shouldCancel = true;
            if (cancelReason == null) {
                cancelReason = "No available dishes in order.";
            }
            logEvent("OrderService", "ERROR", cancelReason);
        }

        if (totalPrice < MINIMUM_ORDER_AMOUNT) {
            shouldCancel = true;
            if (cancelReason == null) {
                cancelReason = "Order must be at least $" + MINIMUM_ORDER_AMOUNT;
            }
            logEvent("OrderService", "ERROR", cancelReason);
        }

        ResponseEntity<Double> balanceResponse;
        try {
            balanceResponse = restTemplate.getForEntity(
                    "http://localhost:8082/api/users/getBalance?userId=" + request.getUserId(), Double.class
            );
        } catch (Exception ex) {
            shouldCancel = true;
            if (cancelReason == null) {
                cancelReason = "Failed to fetch balance: " + ex.getMessage();
            }
            logEvent("OrderService", "ERROR", cancelReason);
            balanceResponse = null;
        }

        if (balanceResponse == null || balanceResponse.getBody() < totalPrice) {
            shouldCancel = true;
            if (cancelReason == null) {
                cancelReason = "Insufficient balance.";
            }
            publishPaymentFailedEvent(request.getUserId(), totalPrice);
            logEvent("OrderService", "ERROR", cancelReason);
        }


        // Step 3: Create and store order
        orderModel order = new orderModel();
        order.setUserId(request.getUserId());
        order.setTotalPrice(totalPrice);

        if (shouldCancel) {
            order.setOrderStatus(status.CANCELLED);
            orderRepo.save(order);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "CANCELLED");
            response.put("message", "Order cancelled: " + cancelReason);
            return ResponseEntity.badRequest().body(response);
        }

        // Step 4: Reduce stock
        try {
            restTemplate.postForObject(DISH_SERVICE_URL + "reduceQuantity", reduceDishesList, Void.class);
        } catch (Exception ex) {
            logEvent("OrderService", "ERROR", "Failed to reduce stock: " + ex.getMessage());
            order.setOrderStatus(status.CANCELLED);
            orderRepo.save(order);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "CANCELLED");
            response.put("message", "Dish stock error. Order cancelled.");
            return ResponseEntity.status(500).body(response);
        }

        // Step 5: Deduct user balance
        ResponseEntity<String> reduceBalanceResponse;
        try {
            String reduceBalanceUrl = "http://localhost:8082/api/users/reduce-balance?userId=" + request.getUserId() + "&amount=" + totalPrice;
            reduceBalanceResponse = restTemplate.postForEntity(reduceBalanceUrl, null, String.class);
        } catch (HttpClientErrorException ex) {
            // This is a 4xx error from UserService (e.g., insufficient balance)
            String userServiceMsg = ex.getResponseBodyAsString();
            String errorMsg = "Insufficient balance. Order cancelled.";
            if (userServiceMsg != null && !userServiceMsg.isEmpty()) {
                errorMsg = userServiceMsg;
            }
            logEvent("OrderService", "ERROR", "User balance deduction failed: " + errorMsg);
            order.setOrderStatus(status.CANCELLED);
            orderRepo.save(order);
            publishPaymentFailedEvent(request.getUserId(), totalPrice);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "CANCELLED");
            response.put("message", errorMsg);
            return ResponseEntity.badRequest().body(response);
        } catch (Exception ex) {
            // This is a network or server error (e.g., connection refused)
            logEvent("OrderService", "ERROR", "Failed to reduce user balance: " + ex.getMessage());
            order.setOrderStatus(status.CANCELLED);
            orderRepo.save(order);
            publishPaymentFailedEvent(request.getUserId(), totalPrice);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "CANCELLED");
            response.put("message", "Could not process payment. Please try again later.");
            return ResponseEntity.status(500).body(response);
        }

        if (!reduceBalanceResponse.getStatusCode().is2xxSuccessful()) {
            String userServiceMsg = reduceBalanceResponse.getBody();
            String errorMsg = "Insufficient balance. Order cancelled.";
            if (userServiceMsg != null && !userServiceMsg.isEmpty()) {
                errorMsg = userServiceMsg;
            }
            logEvent("OrderService", "ERROR", "User balance deduction failed: " + errorMsg);
            order.setOrderStatus(status.CANCELLED);
            orderRepo.save(order);
            publishPaymentFailedEvent(request.getUserId(), totalPrice);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "CANCELLED");
            response.put("message", errorMsg);
            return ResponseEntity.badRequest().body(response);
        }

        // Step 6: Success
        order.setOrderStatus(status.COMPLETED);
        order.setDishesId(dishList);
        orderRepo.save(order);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "COMPLETED");
        response.put("message", "Order placed successfully");
        response.put("orderId", order.getOrderId());
        return ResponseEntity.ok(response);
    }



    public List<orderModel> getAllPastOrders(long userId) {
        return orderRepo.findAll().stream()
                .filter(order -> order.getUserId() == userId)
                .collect(Collectors.toList());
    }

    public List<dishesModel> getAllDishes() {
        return dishesRepo.findAll();
    }


}
