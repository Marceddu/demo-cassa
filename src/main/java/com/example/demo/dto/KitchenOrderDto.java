package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public class KitchenOrderDto {
    public Long id;
    public Long customerOrderId;
    public Long kitchenId;
    public Long progressiveNumber;
    public String status;
    public BigDecimal kitchenTotalAmount;
    public OffsetDateTime cancelledAt;
    public String totalWeightFormatted;
    public String tableNo;
    public String orderNote;
    public List<KitchenOrderItemDto> items;

    public static class KitchenOrderItemDto {
        public Integer qty;
        public String dishName;
        public String itemNote;
        public List<String> optionLabels;
    }
}
