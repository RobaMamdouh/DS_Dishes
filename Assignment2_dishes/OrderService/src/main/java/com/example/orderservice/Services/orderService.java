package com.example.orderservice.Services;

import com.example.orderservice.Models.status;
import com.example.orderservice.Repo.orderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.orderservice.Models.orderModel;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class orderService {
    @Autowired
    private orderRepo orderRepo;

    private RestTemplate restTemplate = new RestTemplate();


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
