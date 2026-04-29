package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public class OrderResponseDto {
    public String id;
    public String tableNo;
    public String notes;
    public String status;
    public Integer totalItems;
    public BigDecimal totalAmount;
    public OffsetDateTime createdAt;
    public OffsetDateTime updatedAt;
    public List<OrderItemResponseDto> items;
}
