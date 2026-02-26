package com.example.demo.dto;

import java.math.BigDecimal;

public class OrderItemResponseDto {
    public Long id;
    public Integer position;
    public String name;
    public Integer qty;
    public BigDecimal unitPrice;
    public BigDecimal lineTotal;
    public String itemNote;
}
