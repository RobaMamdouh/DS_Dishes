package com.example.orderservice.Models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import java.util.List;


@ToString
@Entity
public class orderModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderId;
    private long userId;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "orderId")
    private List<dishesModel> dishes;
    private double totalPrice;
    @Enumerated(EnumType.STRING)
    private status orderStatus;
    public orderModel() {}
    public orderModel(long userId, double totalPrice) {
        this.userId = userId;
        this.totalPrice = totalPrice;
    }

    public long getOrderId() {
        return orderId;
    }

    public long getUserId() {
        return userId;
    }

    public List<dishesModel> getDishes() {
        return dishes;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public status getOrderStatus() {
        return orderStatus;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setDishesId(List<dishesModel> dishes) {
        this.dishes = dishes;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setOrderStatus(status orderStatus) {
        this.orderStatus = orderStatus;
    }

}
