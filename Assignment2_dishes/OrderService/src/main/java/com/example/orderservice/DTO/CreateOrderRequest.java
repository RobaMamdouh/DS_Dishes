package com.example.orderservice.DTO;

import java.util.Map;

public class CreateOrderRequest {
    private long userId;
    private Map<String, Integer> dishIdQuantityMap;

    public CreateOrderRequest(long userId, Map<String, Integer> dishIdQuantityMap) {
        this.userId = userId;
        this.dishIdQuantityMap = dishIdQuantityMap;
    }

    public long getUserId() {
        return userId;
    }
    public void setUserId(long userId) {
        this.userId = userId;
    }
    public Map<String, Integer> getDishIdQuantityMap() {
        return dishIdQuantityMap;
    }
    public void setDishIdQuantityMap(Map<String, Integer> dishIdQuantityMap) {
        this.dishIdQuantityMap = dishIdQuantityMap;
    }
}
