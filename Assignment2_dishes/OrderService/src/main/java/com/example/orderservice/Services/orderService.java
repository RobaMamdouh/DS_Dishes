package com.example.orderservice.Services;

import com.example.orderservice.DTO.CreateOrderRequest;
import com.example.orderservice.DTO.SoldDishInfo;
import com.example.orderservice.Models.dishesModel;
import com.example.orderservice.Models.status;
import com.example.orderservice.Repo.DishesRepo;
import com.example.orderservice.Repo.orderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.orderservice.Models.orderModel;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class orderService {
    @Autowired
    private orderRepo orderRepo;

    @Autowired
    private DishesRepo dishesRepo;

    private RestTemplate restTemplate = new RestTemplate();

    private final String DISH_SERVICE_URL = "http://localhost:8081/DishesService-1.0-SNAPSHOT/api/dishes/";

//    public orderModel createOrder(CreateOrderRequest request) {
//        List<dishesModel> dishList = new ArrayList<>();
//        double totalPrice = 0;
//        double MINIMUM_ORDER_AMOUNT = 50.0;
//
//        for (Map.Entry<Long, Integer> entry : request.getDishIdQuantityMap().entrySet()) {
//            Long dishId = entry.getKey();
//            int quantity = entry.getValue();
//
//            try {
//                String url = DISH_SERVICE_URL + dishId;
//                dishesModel fetchedDish = restTemplate.getForObject(url, dishesModel.class);
//
//                if(quantity > fetchedDish.getQuantity()) {
//                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Requested quantity exceeds available stock for dish ID: " + dishId);
//                }
//
//                if (fetchedDish != null) {
//                    dishesModel dish = new dishesModel();
//                    dish.setDishName(fetchedDish.getDishName());
//                    dish.setQuantity(quantity);
//                    dish.setPrice(fetchedDish.getPrice() * quantity);
//                    dish.setUserId(request.getUserId());
//                    totalPrice += dish.getPrice();
//                    dishList.add(dish);
//                }
//            } catch (Exception ex) {
//                System.out.println("Error fetching dish with ID " + dishId + ": " + ex.getMessage());
//            }
//        }
//
//        if (dishList.isEmpty()) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "None of the selected dishes are available or in stock.");
//        }
//
//
//        if (totalPrice < MINIMUM_ORDER_AMOUNT) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Minimum order amount must be: " + MINIMUM_ORDER_AMOUNT);
//        }
//
//        orderModel order = new orderModel();
//        order.setUserId(request.getUserId());
//        order.setDishesId(dishList);
//        order.setTotalPrice(totalPrice);
//        order.setOrderStatus(status.COMPLETED);
//
//        return orderRepo.save(order);
//    }

    public ResponseEntity<?> createOrder(CreateOrderRequest request) {
        List<dishesModel> dishList = new ArrayList<>();
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
                        continue;
                    }

                    dishesModel dish = new dishesModel();
                    dish.setDishName(fetchedDish.getDishName());
                    dish.setQuantity(quantity);
                    dish.setPrice(fetchedDish.getPrice() * quantity);
                    dish.setUserId(request.getUserId());
                    totalPrice += dish.getPrice();
                    dishList.add(dish);
                }
            } catch (Exception ex) {
                System.out.println("Error fetching dish with ID " + dishId + ": " + ex.getMessage());
            }
        }

        if (dishList.isEmpty()) {
            shouldCancel = true;
            cancelReason = "None of the selected dishes are available or in stock.";
        }

        if (totalPrice < MINIMUM_ORDER_AMOUNT) {
            shouldCancel = true;
            cancelReason = "Minimum order amount must be: " + MINIMUM_ORDER_AMOUNT;
        }

        orderModel order = new orderModel();
        order.setUserId(request.getUserId());

        if (shouldCancel) {
            order.setOrderStatus(status.CANCELLED);
            orderRepo.save(order);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "CANCELLED");
            response.put("message", "Order cancelled due to: " + cancelReason);
            return ResponseEntity.badRequest().body(response);
        } else {
            order.setDishesId(dishList);
            order.setTotalPrice(totalPrice);
            order.setOrderStatus(status.COMPLETED);

            orderModel savedOrder = orderRepo.save(order);
            return ResponseEntity.ok(savedOrder);
        }
    }

    public List<orderModel> getAllPastOrders(long userId) {
        return orderRepo.findAll().stream()
                .filter(order -> order.getUserId() == userId && (order.getOrderStatus() == status.COMPLETED || order.getOrderStatus() == status.CANCELLED))
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
