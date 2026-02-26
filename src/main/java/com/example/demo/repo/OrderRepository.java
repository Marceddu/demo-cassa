package com.example.demo.repo;

import com.example.demo.model.Order;
import com.example.demo.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {

    // Vista cucina: stati attivi ordinati per tempo
    List<Order> findByStatusInOrderByCreatedAtAsc(List<OrderStatus> statuses);

    // Variante con finestra temporale (es. ultimi N minuti)
    List<Order> findByStatusInAndCreatedAtAfterOrderByCreatedAtAsc(
            List<OrderStatus> statuses,
            OffsetDateTime since
    );

    // Totali rapidi per stato
    long countByStatus(OrderStatus status);

    // Pulizia degli ordini "serviti" più vecchi di X ore
    void deleteByStatusAndCreatedAtBefore(OrderStatus status, OffsetDateTime threshold);
}