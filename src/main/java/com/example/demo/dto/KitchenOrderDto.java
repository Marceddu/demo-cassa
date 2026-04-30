package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class KitchenOrderDto {
    public Long id;
    public Long customerOrderId;
    public Long kitchenId;
    public Long progressiveNumber;
    public String status;
    public BigDecimal kitchenTotalAmount;
    public OffsetDateTime cancelledAt;
}
