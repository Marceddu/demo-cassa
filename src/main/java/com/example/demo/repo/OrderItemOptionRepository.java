package com.example.demo.repo;

import com.example.demo.model.OrderItemOption;
import com.example.demo.model.OrderItemOptionKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemOptionRepository extends JpaRepository<OrderItemOption, OrderItemOptionKey> {
    List<OrderItemOption> findByOrderItemId(Long orderItemId);
}
