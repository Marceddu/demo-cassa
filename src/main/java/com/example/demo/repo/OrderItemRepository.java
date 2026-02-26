package com.example.demo.repo;

import com.example.demo.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // Recupero righe di un ordine in ordine stabile
    List<OrderItem> findByOrderIdOrderByPositionAscIdAsc(String orderId);

    // Pulizia veloce di tutte le righe di un ordine
    void deleteByOrderId(String orderId);

    // Utile per statistiche o controlli
    long countByOrderId(String orderId);
}
