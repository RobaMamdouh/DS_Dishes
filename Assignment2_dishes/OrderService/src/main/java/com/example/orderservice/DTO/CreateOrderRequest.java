package com.example.orderservice.DTO;

import java.util.Map;

public class CreateOrderRequest {
    private long userId;
    private Map<Long, Integer> dishIdQuantityMap;

    public CreateOrderRequest(long userId, Map<Long, Integer> dishIdQuantityMap) {
        this.userId = userId;
        this.dishIdQuantityMap = dishIdQuantityMap;
    }

    public long getUserId() {
        return userId;
    }
    public void setUserId(long userId) {
        this.userId = userId;
    }
    public Map<Long, Integer> getDishIdQuantityMap() {
        return dishIdQuantityMap;
    }
    public void setDishIdQuantityMap(Map<Long, Integer> dishIdQuantityMap) {
        this.dishIdQuantityMap = dishIdQuantityMap;
    }
}
