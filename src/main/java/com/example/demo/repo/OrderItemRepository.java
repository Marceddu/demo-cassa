package com.example.demo.repo;

import com.example.demo.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrderIdOrderByPositionAscIdAsc(String orderId);
    void deleteByOrderId(String orderId);
    long countByOrderId(String orderId);

    @Query("select i.name, coalesce(sum(i.qty),0) from OrderItem i group by i.name order by i.name asc")
    List<Object[]> aggregateDishCounters();
}
