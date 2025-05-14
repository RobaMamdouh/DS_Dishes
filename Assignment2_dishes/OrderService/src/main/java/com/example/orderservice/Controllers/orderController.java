package com.example.orderservice.Controllers;

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

//    @GetMapping("/allPastOrders")
//    public String getAllPastOrders(@RequestBody long userId) {
//        return orderService.getAllPastOrders(userId)
//                .stream()
//                .map(order -> "Order ID: " + order.getOrderId() + ", User ID: " + order.getUserId() + ", Status: " + order.getOrderStatus())
//                .collect(Collectors.joining(", "));
//    }
//
//    @GetMapping("/allPendingOrders")
//    public String getAllPendingOrders(@RequestBody long userId) {
//        return orderService.getAllPendingOrders(userId)
//                .stream()
//                .map(order -> "Order ID: " + order.getOrderId() + ", User ID: " + order.getUserId() + ", Status: " + order.getOrderStatus())
//                .collect(Collectors.joining(", "));
//    }

    @GetMapping("/allPendingOrders")
    public String getAllOrders(@RequestParam long userId) {
        return orderService.getAllPendingOrders(userId)
                .stream()
                .map(order -> "Order ID: " + order.getOrderId() + ", User ID: " + order.getUserId() + ", Status: " + order.getOrderStatus())
                .collect(Collectors.joining(", "));
    }

    @GetMapping("/allPastOrders")
    public String getAllPastOrders(@RequestParam long userId) {
        return orderService.getAllPastOrders(userId)
                .stream()
                .map(order -> "Order ID: " + order.getOrderId() + ", User ID: " + order.getUserId() + ", Status: " + order.getOrderStatus())
                .collect(Collectors.joining(", "));
    }
}
