package com.example.orderservice.Services;

import com.example.orderservice.Models.status;
import com.example.orderservice.Repo.orderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.orderservice.Models.orderModel;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class orderService {
    @Autowired
    private orderRepo orderRepo;

//    public void createOrder(long orderId, long userId) {
//        if (orderRepo.existsByOrderId(String.valueOf(orderId))) {
//            throw new RuntimeException("Order with ID " + orderId + " already exists.");
//        }
//        orderModel newOrder = new orderModel();
//        newOrder.setOrderId(orderId);
//        newOrder.setUserId(userId);
//        newOrder.setOrderStatus(status.PENDING);
//
//        orderRepo.save(newOrder);
//    }

    public List<orderModel> getAllPastOrders(long userId) {
        return orderRepo.findAll().stream()
                .filter(order -> order.getUserId() == userId && (order.getOrderStatus() == status.COMPLETED || order.getOrderStatus() == status.CANCELLED))
                .collect(Collectors.toList());
    }

    public List<orderModel> getAllPendingOrders(long userId) {
        return orderRepo.findAll().stream()
                .filter(order -> order.getUserId() == userId && order.getOrderStatus() == status.PENDING)
                .collect(Collectors.toList());
    }

}
