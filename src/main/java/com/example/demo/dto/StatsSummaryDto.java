package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Map;

public class StatsSummaryDto {
    public OffsetDateTime generatedAt;
    public long totalOrders;
    public long totalDishes;
    public BigDecimal totalRevenue;
    public long newOrders;
    public long inProgressOrders;
    public long readyOrders;
    public long doneOrders;
    public Map<String, Long> dishCounters;
}
