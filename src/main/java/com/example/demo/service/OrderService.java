package com.example.demo.service;

import com.example.demo.dto.OrderItemDto;
import com.example.demo.dto.OrderRequestDto;
import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;
import com.example.demo.model.OrderStatus;
import com.example.demo.repo.OrderItemRepository;
import com.example.demo.repo.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepo;
    private final OrderItemRepository itemRepo;

    public OrderService(OrderRepository orderRepo, OrderItemRepository itemRepo) {
        this.orderRepo = orderRepo;
        this.itemRepo = itemRepo;
    }

    // ---------- Letture ----------

    @Transactional(readOnly = true)
    public List<Order> listByStatuses(List<OrderStatus> statuses) {
        return orderRepo.findByStatusInOrderByCreatedAtAsc(statuses);
    }

    @Transactional(readOnly = true)
    public List<Order> listByStatusesSince(List<OrderStatus> statuses, OffsetDateTime since) {
        return orderRepo.findByStatusInAndCreatedAtAfterOrderByCreatedAtAsc(statuses, since);
    }

    @Transactional(readOnly = true)
    public Map<OrderStatus, Long> totalsByStatus() {
        Map<OrderStatus, Long> out = new EnumMap<>(OrderStatus.class);
        for (OrderStatus s : OrderStatus.values()) {
            out.put(s, orderRepo.countByStatus(s));
        }
        return out;
    }

    // ---------- Scritture via DTO ----------

    /** Create da DTO. */
    @Transactional
    public Order createFromDto(OrderRequestDto dto) {
        Order o = new Order();
        applyBaseFields(dto, o);
        if (dto.items != null) {
            replaceItemsFromDto(dto.items, o);
        }
        return orderRepo.save(o);
    }

    /** Update da DTO (richiede dto.id). Sostituisce gli items se dto.items != null. */
    @Transactional
    public Optional<Order> updateFromDto(OrderRequestDto dto) {
        if (dto.id == null || dto.id.isBlank()) return Optional.empty();

        return orderRepo.findById(dto.id).map(o -> {
            applyBaseFields(dto, o);
            if (dto.items != null) {
                // sostituzione atomica lista (orphanRemoval=true gestisce le righe obsolete)
                o.getItems().clear();
                replaceItemsFromDto(dto.items, o);
            }
            return orderRepo.save(o);
        });
    }

    /** Cambio stato semplice (utility). */
    @Transactional
    public Optional<Order> updateStatus(String orderId, OrderStatus status) {
        return orderRepo.findById(orderId).map(o -> {
            o.setStatus(status);
            return orderRepo.save(o);
        });
    }

    /** Delete ordine + righe. */
    @Transactional
    public void delete(String orderId) {
        itemRepo.deleteByOrderId(orderId);
        orderRepo.deleteById(orderId);
    }

    /** Purge dei DONE più vecchi di X ore. */
    @Transactional
    public void purgeDoneOlderThan(long hours) {
        OffsetDateTime threshold = OffsetDateTime.now().minusHours(hours);
        orderRepo.deleteByStatusAndCreatedAtBefore(OrderStatus.DONE, threshold);
    }

    // ---------- Helpers ----------

    private void applyBaseFields(OrderRequestDto dto, Order o) {
        if (dto.tableNo != null) o.setTableNo(dto.tableNo);
        if (dto.notes   != null) o.setNotes(dto.notes);
        if (dto.status  != null) o.setStatus(OrderStatus.valueOf(dto.status.toUpperCase()));
    }

    private void replaceItemsFromDto(List<OrderItemDto> items, Order o) {
        int pos = 0;
        for (OrderItemDto d : items) {
            OrderItem e = new OrderItem();
            e.setName(d.name);
            e.setQty(d.qty == null ? 1 : d.qty);
            e.setItemNote(d.itemNote);
            e.setPosition(d.position != null ? d.position : pos++);
            e.setOrder(o);
            o.getItems().add(e);
        }
    }
}
