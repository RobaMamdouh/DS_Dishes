package com.dishes.dishes_service.Models;

import jakarta.persistence.*;

@Entity
@Table(name = "dishes")
public class DishesModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int quantity;
    private double price;
    private Long sellerId;



    public DishesModel() {
    }

    public DishesModel(String name, int quantity, double price, Long sellerId) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.sellerId = sellerId;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }



}
