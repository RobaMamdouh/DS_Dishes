package com.example.orderservice.Repo;

import com.example.orderservice.Models.dishesModel;
import com.example.orderservice.Models.orderModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishesRepo extends JpaRepository<dishesModel, Long> {
    dishesModel findById(long Id);
    boolean existsById(long Id);
}
