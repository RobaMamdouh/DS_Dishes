package com.example.orderservice.DTO;

public class SoldDishInfo {
    private String dishName;
    private int quantity;
    private double price;
    private String buyerUsername;

    public SoldDishInfo(String dishName, int quantity, double price, String buyerUsername) {
        this.dishName = dishName;
        this.quantity = quantity;
        this.price = price;
        this.buyerUsername = buyerUsername;
    }

    // Getters and Setters
    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
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

    public String getBuyerUsername() {
        return buyerUsername;
    }

    public void setBuyerUsername(String buyerUsername) {
        this.buyerUsername = buyerUsername;
    }
}
