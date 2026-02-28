package com.example.demo.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "stats_snapshots")
public class StatsSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private OffsetDateTime createdAt;

    @Column(nullable = false)
    private long totalOrders;

    @Column(nullable = false)
    private long totalDishes;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal totalRevenue;

    @Column(nullable = false)
    private long newOrders;

    @Column(nullable = false)
    private long inProgressOrders;

    @Column(nullable = false)
    private long readyOrders;

    @Column(nullable = false)
    private long doneOrders;

    @Lob
    @Column(nullable = false)
    private String dishCounters;

    @PrePersist
    public void prePersist() {
        this.createdAt = OffsetDateTime.now();
    }

    public Long getId() { return id; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public long getTotalOrders() { return totalOrders; }
    public void setTotalOrders(long totalOrders) { this.totalOrders = totalOrders; }
    public long getTotalDishes() { return totalDishes; }
    public void setTotalDishes(long totalDishes) { this.totalDishes = totalDishes; }
    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }
    public long getNewOrders() { return newOrders; }
    public void setNewOrders(long newOrders) { this.newOrders = newOrders; }
    public long getInProgressOrders() { return inProgressOrders; }
    public void setInProgressOrders(long inProgressOrders) { this.inProgressOrders = inProgressOrders; }
    public long getReadyOrders() { return readyOrders; }
    public void setReadyOrders(long readyOrders) { this.readyOrders = readyOrders; }
    public long getDoneOrders() { return doneOrders; }
    public void setDoneOrders(long doneOrders) { this.doneOrders = doneOrders; }
    public String getDishCounters() { return dishCounters; }
    public void setDishCounters(String dishCounters) { this.dishCounters = dishCounters; }
}
