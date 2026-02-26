package com.example.demo.repo;

import com.example.demo.model.Order;
import com.example.demo.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findByStatusInOrderByCreatedAtAsc(List<OrderStatus> statuses);
    List<Order> findByStatusInAndCreatedAtAfterOrderByCreatedAtAsc(List<OrderStatus> statuses, OffsetDateTime since);
    long countByStatus(OrderStatus status);
    void deleteByStatusAndCreatedAtBefore(OrderStatus status, OffsetDateTime threshold);

    @Query("select coalesce(sum(o.totalItems),0) from Order o")
    Long sumTotalItems();

    @Query("select coalesce(sum(o.totalAmount),0) from Order o")
    BigDecimal sumTotalAmount();
}
