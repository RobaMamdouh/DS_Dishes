package com.example.orderservice.Models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
public class dishesModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @JsonProperty("name")
    private String dishName;
    private int quantity;
    private double price;
    private long userId;
    @ManyToOne
    @JoinColumn(name = "orderId")
    private orderModel order;

    public dishesModel(String dishName, int quantity, double price) {
        this.dishName = dishName;
        this.quantity = quantity;
        this.price = price;
    }
    public dishesModel() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
    public String getDishName() {
        return dishName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public orderModel getOrder() {
        return order;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setOrder(orderModel order) {
        this.order = order;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

}
