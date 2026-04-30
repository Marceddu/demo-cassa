package com.example.demo.repo;

import com.example.demo.model.KitchenOrder;
import com.example.demo.model.KitchenOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KitchenOrderRepository extends JpaRepository<KitchenOrder, Long> {
    List<KitchenOrder> findByKitchenIdAndStatusNotOrderByCreatedAtAsc(Long kitchenId, KitchenOrderStatus status);
}
