package com.example.demo.service;

import com.example.demo.dto.StatsSummaryDto;
import com.example.demo.model.OrderStatus;
import com.example.demo.model.StatsSnapshot;
import com.example.demo.repo.OrderItemRepository;
import com.example.demo.repo.OrderRepository;
import com.example.demo.repo.StatsSnapshotRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatsService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final StatsSnapshotRepository statsSnapshotRepository;
    private final ObjectMapper objectMapper;

    public StatsService(OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository,
                        StatsSnapshotRepository statsSnapshotRepository,
                        ObjectMapper objectMapper) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.statsSnapshotRepository = statsSnapshotRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public StatsSummaryDto refreshSnapshot() {
        StatsSummaryDto summary = computeCurrent();
        StatsSnapshot snapshot = new StatsSnapshot();
        snapshot.setTotalOrders(summary.totalOrders);
        snapshot.setTotalDishes(summary.totalDishes);
        snapshot.setTotalRevenue(summary.totalRevenue);
        snapshot.setNewOrders(summary.newOrders);
        snapshot.setInProgressOrders(summary.inProgressOrders);
        snapshot.setReadyOrders(summary.readyOrders);
        snapshot.setDoneOrders(summary.doneOrders);
        snapshot.setDishCounters(writeMap(summary.dishCounters));
        statsSnapshotRepository.save(snapshot);
        summary.generatedAt = snapshot.getCreatedAt();
        return summary;
    }

    @Transactional(readOnly = true)
    public StatsSummaryDto latestOrCurrent() {
        return statsSnapshotRepository.findTopByOrderByCreatedAtDesc()
                .map(this::fromSnapshot)
                .orElseGet(this::computeCurrent);
    }

    @Transactional(readOnly = true)
    public String exportAsTxt() {
        StatsSummaryDto s = latestOrCurrent();
        StringBuilder sb = new StringBuilder();
        sb.append("generatedAt=").append(s.generatedAt).append('\n');
        sb.append("totalOrders=").append(s.totalOrders).append('\n');
        sb.append("totalDishes=").append(s.totalDishes).append('\n');
        sb.append("totalRevenue=").append(s.totalRevenue).append('\n');
        sb.append("NEW=").append(s.newOrders).append('\n');
        sb.append("IN_PROGRESS=").append(s.inProgressOrders).append('\n');
        sb.append("READY=").append(s.readyOrders).append('\n');
        sb.append("DONE=").append(s.doneOrders).append('\n');
        s.dishCounters.forEach((k, v) -> sb.append("dish.").append(k).append('=').append(v).append('\n'));
        return sb.toString();
    }

    private StatsSummaryDto computeCurrent() {
        StatsSummaryDto s = new StatsSummaryDto();
        s.generatedAt = OffsetDateTime.now();
        s.totalOrders = orderRepository.count();
        s.totalDishes = orderRepository.sumTotalItems();
        s.totalRevenue = defaultMoney(orderRepository.sumTotalAmount());
        s.newOrders = orderRepository.countByStatus(OrderStatus.NEW);
        s.inProgressOrders = orderRepository.countByStatus(OrderStatus.IN_PROGRESS);
        s.readyOrders = orderRepository.countByStatus(OrderStatus.READY);
        s.doneOrders = orderRepository.countByStatus(OrderStatus.DONE);
        s.dishCounters = aggregateDishCounters();
        return s;
    }

    private StatsSummaryDto fromSnapshot(StatsSnapshot snap) {
        StatsSummaryDto s = new StatsSummaryDto();
        s.generatedAt = snap.getCreatedAt();
        s.totalOrders = snap.getTotalOrders();
        s.totalDishes = snap.getTotalDishes();
        s.totalRevenue = snap.getTotalRevenue();
        s.newOrders = snap.getNewOrders();
        s.inProgressOrders = snap.getInProgressOrders();
        s.readyOrders = snap.getReadyOrders();
        s.doneOrders = snap.getDoneOrders();
        s.dishCounters = readMap(snap.getDishCounters());
        return s;
    }

    private Map<String, Long> aggregateDishCounters() {
        Map<String, Long> out = new LinkedHashMap<>();
        List<Object[]> rows = orderItemRepository.aggregateDishCounters();
        for (Object[] row : rows) {
            out.put((String) row[0], ((Number) row[1]).longValue());
        }
        return out;
    }

    private String writeMap(Map<String, Long> data) {
        try {
            return objectMapper.writeValueAsString(data);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private Map<String, Long> readMap(String text) {
        try {
            return objectMapper.readValue(text, new TypeReference<>() {});
        } catch (Exception e) {
            return new LinkedHashMap<>();
        }
    }

    private BigDecimal defaultMoney(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}
