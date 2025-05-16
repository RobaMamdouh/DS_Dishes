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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class orderController {
    @Autowired
    private orderService orderService;

    @PostMapping("/createOrder")
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequest request) {
        try {
            ResponseEntity<?> response = orderService.createOrder(request);
            if (response.getStatusCode().is2xxSuccessful()) {
                Object body = response.getBody();
                if (body instanceof Map) {
                    return response;
                } else if (body instanceof com.example.orderservice.Models.orderModel) {
                    com.example.orderservice.Models.orderModel order = (com.example.orderservice.Models.orderModel) body;
                    Map<String, Object> map = new HashMap<>();
                    map.put("orderId", order.getOrderId());
                    map.put("totalPrice", order.getTotalPrice());
                    map.put("status", order.getOrderStatus());
                    return ResponseEntity.ok(map);
                } else {
                    Map<String, Object> map = new HashMap<>();
                    map.put("orderId", ((Map)body).get("orderId"));
                    map.put("totalPrice", ((Map)body).get("totalPrice"));
                    map.put("status", ((Map)body).get("status"));
                    return ResponseEntity.ok(map);
                }
            }
            return response;
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "ERROR");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

//    @GetMapping("/order/{orderId}")
//    public Map<String, Object> getOrderById(@PathVariable long orderId) {
//        orderModel order = orderService.getOrderById(orderId);
//        Map<String, Object> map = new HashMap<>();
//        map.put("orderId", order.getOrderId());
//        map.put("status", order.getOrderStatus());
//        map.put("totalPrice", order.getTotalPrice());
//        map.put("dishes", order.getDishes());
//        return map;
//    }

    @GetMapping("/allPastOrders")
    public List<Map<String, Object>> getAllPastOrders(@RequestParam long userId) {
        return orderService.getAllPastOrders(userId)
                .stream()
                .map(order -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("orderId", order.getOrderId());
                    map.put("totalPrice", order.getTotalPrice());
                    map.put("status", order.getOrderStatus());
                    return map;
                })
                .collect(Collectors.toList());
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

@GetMapping("/sold-dishes")
public List<SoldDishInfo> getSoldDishesBySeller(@RequestParam Long sellerId) {
    List<SoldDishInfo> soldDishes = new ArrayList<>();
    // Get all completed orders
    List<orderModel> completedOrders = orderService.getAllOrders().stream()
        .filter(order -> order.getOrderStatus() == com.example.orderservice.Models.status.COMPLETED)
        .collect(Collectors.toList());
    for (orderModel order : completedOrders) {
        String buyerUsername;
        try {
            buyerUsername = new RestTemplate().getForObject("http://localhost:8082/api/users/getUsernameById?userId=" + order.getUserId(), String.class);
        } catch (Exception e) {
            buyerUsername = "Unknown";
        }
        for (com.example.orderservice.Models.dishesModel dish : order.getDishes()) {
            if (dish.getUserId() == sellerId) {
                soldDishes.add(new SoldDishInfo(
                    dish.getDishName(),
                    dish.getQuantity(),
                    dish.getPrice(),
                    buyerUsername
                ));
            }
        }
    }
    return soldDishes;
}

}
