package com.example.orderservice.Controllers;

import com.example.orderservice.DTO.CreateOrderRequest;
import com.example.orderservice.DTO.SoldDishInfo;
import com.example.orderservice.Models.orderModel;
import com.example.orderservice.Services.orderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class orderController {
    @Autowired
    private orderService orderService;

    @PostMapping("/createOrder")
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequest request) {
        try {
            ResponseEntity<?> response = orderService.createOrder(request);
            return response;
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "ERROR");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

//    @GetMapping("/allPendingOrders")
//    public String getAllOrders(@RequestParam long userId) {
//        return orderService.getAllPendingOrders(userId)
//                .stream()
//                .map(order -> "Order ID: " + order.getOrderId() + ", User ID: " + order.getUserId() + ", Status: " + order.getOrderStatus() + ", Total Price: " + order.getTotalPrice() +
//                        ", Dishes: " + order.getDishes().stream().map(dish -> dish.getDishName() + " (x" + dish.getQuantity() + ")").collect(Collectors.joining(", ")))
//                .collect(Collectors.joining(", "));
//    }

    @GetMapping("/allPastOrders")
    public String getAllPastOrders(@RequestParam long userId) {
        return orderService.getAllPastOrders(userId)
                .stream()
                .map(order -> "Order ID: " + order.getOrderId() +
                        ", User ID: " + order.getUserId() +
                        ", Status: " + order.getOrderStatus() +
                        ", Total Price: " + order.getTotalPrice() +
                        ", Dishes: " + order.getDishes().stream()
                        .map(dish -> dish.getDishName() + " (x" + dish.getQuantity() + ")")
                        .collect(Collectors.joining(", ")))
                .collect(Collectors.joining("\n"));
    }

    private final String USER_SERVICE_URL = "http://localhost:8082/api/users/getUsernameById?userId=";
    private RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/alldishes")
    public String getAllDishes() {
        return orderService.getAllDishes()
                .stream()
                .map(dish -> "Dish ID: " + dish.getId() +
                        ", Dish Name: " + dish.getDishName() +
                        ", Price: " + dish.getPrice() +
                        ", Quantity: " + dish.getQuantity() +
                        ", User ID: " + dish.getUserId() +
                        ", Username: " + restTemplate.getForObject(USER_SERVICE_URL + dish.getUserId(), String.class))
                .collect(Collectors.joining("\n"));
    }

}
