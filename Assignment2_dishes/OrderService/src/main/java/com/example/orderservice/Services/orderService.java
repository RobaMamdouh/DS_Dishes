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

    public ResponseEntity<?> createOrder(CreateOrderRequest request) {
        List<dishesModel> dishList = new ArrayList<>();
        List<ReduceDishesDTO> reduceDishesList = new ArrayList<>();
        double totalPrice = 0;
        double MINIMUM_ORDER_AMOUNT = 50.0;
        boolean shouldCancel = false;
        String cancelReason = null;

        for (Map.Entry<Long, Integer> entry : request.getDishIdQuantityMap().entrySet()) {
            Long dishId = entry.getKey();
            int quantity = entry.getValue();

            try {
                String url = DISH_SERVICE_URL + dishId;
                dishesModel fetchedDish = restTemplate.getForObject(url, dishesModel.class);

                if (fetchedDish != null) {
                    if (quantity > fetchedDish.getQuantity()) {
                        shouldCancel = true;
                        cancelReason = "Requested quantity exceeds available stock for dish ID: " + dishId;
                        logEvent("OrderService", "ERROR", cancelReason);
                        continue;
                    }

                    dishesModel dish = new dishesModel();
                    dish.setDishName(fetchedDish.getDishName());
                    dish.setQuantity(quantity);
                    dish.setPrice(fetchedDish.getPrice() * quantity);
                    dish.setUserId(request.getUserId());
                    totalPrice += dish.getPrice();
                    dishList.add(dish);
                    reduceDishesList.add(new ReduceDishesDTO(dishId, quantity));
                }
            } catch (Exception ex) {
                String errorMessage = "Error fetching dish with ID " + dishId + ": " + ex.getMessage();
                logEvent("OrderService", "ERROR", errorMessage);
            }
        }

        if (dishList.isEmpty()) {
            shouldCancel = true;
            cancelReason = "None of the selected dishes are available or in stock.";
            logEvent("OrderService", "ERROR", cancelReason);
        }

        if (totalPrice < MINIMUM_ORDER_AMOUNT) {
            shouldCancel = true;
            cancelReason = "Minimum order amount must be: " + MINIMUM_ORDER_AMOUNT;
            logEvent("OrderService", "ERROR", cancelReason);
        }

        String balanceURL = "http://localhost:8082/api/users/getBalance?userId=" + request.getUserId();
        ResponseEntity<Double> getBalance = restTemplate.getForEntity(balanceURL, Double.class);
        // check if the balance is less than the total return log the error and payment failure
        if (getBalance.getBody() < totalPrice) {
            shouldCancel = true;
            cancelReason = "Insufficient balance.";
            logEvent("OrderService", "ERROR", cancelReason);
            publishPaymentFailedEvent(request.getUserId(), totalPrice);
        }


        orderModel order = new orderModel();
        order.setUserId(request.getUserId());

        if (shouldCancel) {
            order.setOrderStatus(status.CANCELLED);
            orderRepo.save(order);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "CANCELLED");
            response.put("message", "Order cancelled due to: " + cancelReason);
            logEvent("OrderService", "INFO", "Order cancelled for user ID: " + request.getUserId());
            return ResponseEntity.badRequest().body(response);
        } else {
            order.setDishesId(dishList);
            order.setTotalPrice(totalPrice);
            order.setOrderStatus(status.COMPLETED);
            orderModel savedOrder = orderRepo.save(order);
            logEvent("OrderService", "INFO", "Order created successfully for user ID: " + request.getUserId());

            try {
                String reduceStockUrl = DISH_SERVICE_URL + "/reduce-stock";
                ResponseEntity<String> reduceResponse = restTemplate.postForEntity(reduceStockUrl, reduceDishesList, String.class);

                if (!reduceResponse.getStatusCode().is2xxSuccessful()) {
                    logEvent("OrderService", "WARNING", "Stock reduction failed in dishes service.");
                }
            } catch (Exception e) {
                logEvent("OrderService", "ERROR", "Error reducing dish stock: " + e.getMessage());
            }

            try {
                String reduceBalanceUrl = "http://localhost:8082/api/users/reduce-balance?userId="
                        + request.getUserId() + "&amount=" + totalPrice;

                ResponseEntity<String> balanceResponse = restTemplate.postForEntity(reduceBalanceUrl, null, String.class);

                if (!balanceResponse.getStatusCode().is2xxSuccessful()) {
                    publishPaymentFailedEvent(request.getUserId(), totalPrice);
                    logEvent("OrderService", "ERROR", "Balance deduction failed for user ID: " + request.getUserId());
                }
            } catch (Exception e) {
                logEvent("OrderService", "ERROR", "Error reducing user balance: " + e.getMessage());
            }

            return ResponseEntity.ok(savedOrder);
        }
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

    public List<SoldDishInfo> getSoldDishesWithUsernames() {
        List<orderModel> completedOrders = orderRepo.findAll().stream()
                .filter(order -> order.getOrderStatus() == status.COMPLETED)
                .toList();

        List<SoldDishInfo> result = new ArrayList<>();

        for (orderModel order : completedOrders) {
            String username = fetchUsernameByUserId(order.getUserId());
            for (dishesModel dish : order.getDishes()) {
                result.add(new SoldDishInfo(
                        dish.getDishName(),
                        dish.getQuantity(),
                        dish.getPrice(),
                        username
                ));
            }
        }

        return result;
    }

    private String fetchUsernameByUserId(long userId) {
        try {
            String url = "http://localhost:8082/users/" + userId; // Adjust if needed
            Map response = restTemplate.getForObject(url, Map.class);
            return response != null && response.get("username") != null ? response.get("username").toString() : "Unknown";
        } catch (Exception e) {
            return "Unknown";
        }
    }


}
