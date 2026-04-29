package com.example.demo.service;

import com.example.demo.dto.OrderItemDto;
import com.example.demo.dto.OrderRequestDto;
import com.example.demo.model.Dish;
import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;
import com.example.demo.model.OrderStatus;
import com.example.demo.repo.DishRepository;
import com.example.demo.repo.OrderItemRepository;
import com.example.demo.repo.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepo;
    private final OrderItemRepository itemRepo;
    private final DishRepository dishRepository;
    private final StatsService statsService;

    public OrderService(OrderRepository orderRepo,
                        OrderItemRepository itemRepo,
                        DishRepository dishRepository,
                        StatsService statsService) {
        this.orderRepo = orderRepo;
        this.itemRepo = itemRepo;
        this.dishRepository = dishRepository;
        this.statsService = statsService;
    }

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

    @Transactional
    public Order createFromDto(OrderRequestDto dto) {
        Order order = new Order();
        applyBaseFields(dto, order);
        replaceItemsFromDto(dto.items, order);
        calculateOrderTotals(order);
        Order saved = orderRepo.save(order);
        statsService.refreshSnapshot();
        return saved;
    }

    @Transactional
    public Optional<Order> updateFromDto(OrderRequestDto dto) {
        if (dto.id == null || dto.id.isBlank()) return Optional.empty();
        return orderRepo.findById(dto.id).map(order -> {
            applyBaseFields(dto, order);
            if (dto.items != null) {
                order.getItems().clear();
                replaceItemsFromDto(dto.items, order);
            }
            calculateOrderTotals(order);
            Order saved = orderRepo.save(order);
            statsService.refreshSnapshot();
            return saved;
        });
    }

    @Transactional
    public void delete(String orderId) {
        itemRepo.deleteByOrderId(orderId);
        orderRepo.deleteById(orderId);
        statsService.refreshSnapshot();
    }

    private void applyBaseFields(OrderRequestDto dto, Order o) {
        if (dto.tableNo != null) o.setTableNo(dto.tableNo);
        if (dto.notes != null) o.setNotes(dto.notes);
        if (dto.status != null) o.setStatus(OrderStatus.valueOf(dto.status.toUpperCase()));
    }

    private void replaceItemsFromDto(List<OrderItemDto> items, Order o) {
        if (items == null) return;
        int pos = 0;
        for (OrderItemDto d : items) {
            Dish dish = dishRepository.findById(d.dishId).orElseThrow();
            int qty = d.qty == null || d.qty < 1 ? 1 : d.qty;
            OrderItem e = new OrderItem();
            e.setName(dish.getName());
            e.setQty(qty);
            e.setItemNote(d.itemNote);
            e.setPosition(d.position != null ? d.position : pos++);
            e.setUnitPrice(dish.getPrice());
            e.setLineTotal(dish.getPrice().multiply(BigDecimal.valueOf(qty)));
            e.setOrder(o);
            o.getItems().add(e);
        }
    }

    private void calculateOrderTotals(Order order) {
        int totalItems = 0;
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderItem item : order.getItems()) {
            int qty = item.getQty() == null || item.getQty() < 1 ? 1 : item.getQty();
            item.setQty(qty);
            BigDecimal line = item.getUnitPrice().multiply(BigDecimal.valueOf(qty));
            item.setLineTotal(line);
            totalItems += qty;
            totalAmount = totalAmount.add(line);
        }
        order.setTotalItems(totalItems);
        order.setTotalAmount(totalAmount);
    }
}
