package com.example.orderservice.Controllers;

import com.example.orderservice.DTO.CreateOrderRequest;
import com.example.orderservice.Models.orderModel;
import com.example.orderservice.Services.orderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class orderController {
    @Autowired
    private orderService orderService;

    @PostMapping("/createOrder")
    public String createOrder(@RequestBody CreateOrderRequest request) {
        orderModel order = orderService.createOrder(request);
        return "Order created with ID: " + order.getOrderId();
    }

    @GetMapping("/allPendingOrders")
    public String getAllOrders(@RequestParam long userId) {
        return orderService.getAllPendingOrders(userId)
                .stream()
                .map(order -> "Order ID: " + order.getOrderId() + ", User ID: " + order.getUserId() + ", Status: " + order.getOrderStatus() + ", Total Price: " + order.getTotalPrice() +
                        ", Dishes: " + order.getDishes().stream().map(dish -> dish.getDishName() + " (x" + dish.getQuantity() + ")").collect(Collectors.joining(", ")))
                .collect(Collectors.joining(", "));
    }

    @GetMapping("/allPastOrders")
    public String getAllPastOrders(@RequestParam long userId) {
        return orderService.getAllPastOrders(userId)
                .stream()
                .map(order -> "Order ID: " + order.getOrderId() + ", User ID: " + order.getUserId() + ", Status: " + order.getOrderStatus() + ", Total Price: " + order.getTotalPrice() +
                        ", Dishes: " + order.getDishes().stream().map(dish -> dish.getDishName() + " (x" + dish.getQuantity() + ")").collect(Collectors.joining(", ")))
                .collect(Collectors.joining(", "));
    }
}
