package com.example.orderservice.Services;

import com.example.orderservice.DTO.CreateOrderRequest;
import com.example.orderservice.Models.dishesModel;
import com.example.orderservice.Models.status;
import com.example.orderservice.Repo.orderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.example.orderservice.Models.orderModel;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class orderService {
    @Autowired
    private orderRepo orderRepo;

    private RestTemplate restTemplate = new RestTemplate();

    private final String DISH_SERVICE_URL = "http://localhost:8081/DishesService-1.0-SNAPSHOT/api/dishes/";

    public orderModel createOrder(CreateOrderRequest request) {
        List<dishesModel> dishList = new ArrayList<>();
        double totalPrice = 0;
        double MINIMUM_ORDER_AMOUNT = 50.0;

        for (Map.Entry<Long, Integer> entry : request.getDishIdQuantityMap().entrySet()) {
            Long dishId = entry.getKey();
            int quantity = entry.getValue();

            try {
                String url = DISH_SERVICE_URL + dishId;
                dishesModel fetchedDish = restTemplate.getForObject(url, dishesModel.class);
                System.out.println(fetchedDish.getDishName() + " " + fetchedDish.getPrice() + " " + fetchedDish.getQuantity() + " " + quantity);

                if (fetchedDish != null) {
                    dishesModel dish = new dishesModel();
                    dish.setDishName(fetchedDish.getDishName());
                    dish.setQuantity(quantity);
                    dish.setPrice(fetchedDish.getPrice() * quantity);
                    totalPrice += dish.getPrice();
                    dishList.add(dish);
                }
            } catch (Exception ex) {
                System.out.println("Error fetching dish with ID " + dishId + ": " + ex.getMessage());
            }
        }

        if (dishList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "None of the selected dishes are available or in stock.");
        }


        if (totalPrice < MINIMUM_ORDER_AMOUNT) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Minimum order amount must be: " + MINIMUM_ORDER_AMOUNT);
        }

        orderModel order = new orderModel();
        order.setUserId(request.getUserId());
        order.setDishesId(dishList);
        order.setTotalPrice(totalPrice);
        order.setOrderStatus(status.PENDING);

        return orderRepo.save(order);
    }

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
