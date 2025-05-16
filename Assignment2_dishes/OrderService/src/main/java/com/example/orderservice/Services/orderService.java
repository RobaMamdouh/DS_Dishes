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

//    public ResponseEntity<?> createOrder(CreateOrderRequest request) {
//        List<dishesModel> dishList = new ArrayList<>();
//        List<ReduceDishesDTO> reduceDishesList = new ArrayList<>();
//        double totalPrice = 0;
//        double MINIMUM_ORDER_AMOUNT = 50.0;
//        boolean shouldCancel = false;
//        String cancelReason = null;
//
//        for (Map.Entry<Long, Integer> entry : request.getDishIdQuantityMap().entrySet()) {
//            Long dishId = entry.getKey();
//            int quantity = entry.getValue();
//
//            try {
//                String url = DISH_SERVICE_URL + dishId;
//                dishesModel fetchedDish = restTemplate.getForObject(url, dishesModel.class);
//
//                if (fetchedDish != null) {
//                    if (quantity > fetchedDish.getQuantity()) {
//                        shouldCancel = true;
//                        cancelReason = "Requested quantity exceeds available stock for dish ID: " + dishId;
//                        logEvent("OrderService", "ERROR", cancelReason);
//                        continue;
//                    }
//
//                    dishesModel dish = new dishesModel();
//                    dish.setDishName(fetchedDish.getDishName());
//                    dish.setQuantity(quantity);
//                    dish.setPrice(fetchedDish.getPrice() * quantity);
//                    dish.setUserId(request.getUserId());
//                    totalPrice += dish.getPrice();
//                    dishList.add(dish);
//                    reduceDishesList.add(new ReduceDishesDTO(dishId, quantity));
//                }
//            } catch (Exception ex) {
//                String errorMessage = "Error fetching dish with ID " + dishId + ": " + ex.getMessage();
//                logEvent("OrderService", "ERROR", errorMessage);
//            }
//        }
//
//        if (dishList.isEmpty()) {
//            shouldCancel = true;
//            cancelReason = "None of the selected dishes are available or in stock.";
//            logEvent("OrderService", "ERROR", cancelReason);
//        }
//
//        if (totalPrice < MINIMUM_ORDER_AMOUNT) {
//            shouldCancel = true;
//            cancelReason = "Minimum order amount must be: " + MINIMUM_ORDER_AMOUNT;
//            logEvent("OrderService", "ERROR", cancelReason);
//        }
//
//        String balanceURL = "http://localhost:8082/api/users/getBalance?userId=" + request.getUserId();
//        ResponseEntity<Double> getBalance = restTemplate.getForEntity(balanceURL, Double.class);
//        // check if the balance is less than the total return log the error and payment failure
//        if (getBalance.getBody() < totalPrice) {
//            shouldCancel = true;
//            cancelReason = "Insufficient balance.";
//            logEvent("OrderService", "ERROR", cancelReason);
//            publishPaymentFailedEvent(request.getUserId(), totalPrice);
//        }
//
//
//        orderModel order = new orderModel();
//        order.setUserId(request.getUserId());
//
//        if (shouldCancel) {
//            order.setOrderStatus(status.CANCELLED);
//            orderRepo.save(order);
//
//            Map<String, Object> response = new HashMap<>();
//            response.put("status", "CANCELLED");
//            response.put("message", "Order cancelled due to: " + cancelReason);
//            logEvent("OrderService", "INFO", "Order cancelled for user ID: " + request.getUserId());
//            return ResponseEntity.badRequest().body(response);
//        } else {
//            order.setDishesId(dishList);
//            order.setTotalPrice(totalPrice);
//            order.setOrderStatus(status.COMPLETED);
//            orderModel savedOrder = orderRepo.save(order);
//            logEvent("OrderService", "INFO", "Order created successfully for user ID: " + request.getUserId());
//
//            try {
//                String reduceStockUrl = DISH_SERVICE_URL + "/reduce-stock";
//                ResponseEntity<String> reduceResponse = restTemplate.postForEntity(reduceStockUrl, reduceDishesList, String.class);
//
//                if (!reduceResponse.getStatusCode().is2xxSuccessful()) {
//                    logEvent("OrderService", "WARNING", "Stock reduction failed in dishes service.");
//                }
//            } catch (Exception e) {
//                logEvent("OrderService", "ERROR", "Error reducing dish stock: " + e.getMessage());
//            }
//
//            try {
//                String reduceBalanceUrl = "http://localhost:8082/api/users/reduce-balance?userId="
//                        + request.getUserId() + "&amount=" + totalPrice;
//
//                ResponseEntity<String> balanceResponse = restTemplate.postForEntity(reduceBalanceUrl, null, String.class);
//
//                if (!balanceResponse.getStatusCode().is2xxSuccessful()) {
//                    publishPaymentFailedEvent(request.getUserId(), totalPrice);
//                    logEvent("OrderService", "ERROR", "Balance deduction failed for user ID: " + request.getUserId());
//                }
//            } catch (Exception e) {
//                logEvent("OrderService", "ERROR", "Error reducing user balance: " + e.getMessage());
//            }
//
//            return ResponseEntity.ok(savedOrder);
//        }
//    }

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
                        cancelReason = "Requested quantity exceeds stock for dish ID: " + dishId;
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
        if (dishList.isEmpty()) {
            shouldCancel = true;
            cancelReason = "No available dishes in order.";
            logEvent("OrderService", "ERROR", cancelReason);
        }

        if (totalPrice < MINIMUM_ORDER_AMOUNT) {
            shouldCancel = true;
            cancelReason = "Order must be at least $" + MINIMUM_ORDER_AMOUNT;
            logEvent("OrderService", "ERROR", cancelReason);
        }

        ResponseEntity<Double> balanceResponse;
        try {
            balanceResponse = restTemplate.getForEntity(
                    "http://localhost:8082/api/users/getBalance?userId=" + request.getUserId(), Double.class
            );
        } catch (Exception ex) {
            shouldCancel = true;
            cancelReason = "Failed to fetch balance: " + ex.getMessage();
            logEvent("OrderService", "ERROR", cancelReason);
            balanceResponse = null;
        }

        if (balanceResponse == null || balanceResponse.getBody() < totalPrice) {
            shouldCancel = true;
            cancelReason = "Insufficient balance.";
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

        // Step 5: Success
        order.setOrderStatus(status.PENDING);
        order.setDishesId(dishList);
        orderRepo.save(order);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "PENDING");
        response.put("message", "Order placed successfully");
        response.put("orderId", order.getOrderId());
        return ResponseEntity.ok(response);
    }



    public List<orderModel> getAllPastOrders(long userId) {
        return orderRepo.findAll().stream()
                .filter(order -> order.getUserId() == userId)
                .collect(Collectors.toList());
    }

//    public List<orderModel> getAllPendingOrders(long userId) {
//        return orderRepo.findAll().stream()
//                .filter(order -> order.getUserId() == userId && order.getOrderStatus() == status.PENDING)
//                .collect(Collectors.toList());
//    }
    public List<dishesModel> getAllDishes() {
        return dishesRepo.findAll();
    }


}
