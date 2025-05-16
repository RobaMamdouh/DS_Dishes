package com.example.orderservice.DTO;

public class ReduceDishesDTO {
    private Long dishId;
    private int quantity;

    // Constructors
    public ReduceDishesDTO() {}
    public ReduceDishesDTO(Long dishId, int quantity) {
        this.dishId = dishId;
        this.quantity = quantity;
    }

    // Getters and Setters
    public Long getDishId() {
        return dishId;
    }

    public void setDishId(Long dishId) {
        this.dishId = dishId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
