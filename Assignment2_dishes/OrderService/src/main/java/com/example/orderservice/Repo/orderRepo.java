package com.example.orderservice.Repo;

import com.example.orderservice.Models.orderModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface orderRepo extends JpaRepository<orderModel, Long> {
    orderModel findByOrderId(long orderId);
    boolean existsByOrderId(long orderId);
}
