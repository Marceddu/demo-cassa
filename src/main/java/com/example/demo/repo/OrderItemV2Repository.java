package com.example.demo.repo;

import com.example.demo.model.OrderItemV2;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemV2Repository extends JpaRepository<OrderItemV2, Long> {
    List<OrderItemV2> findByKitchenOrderId(Long kitchenOrderId);
}
