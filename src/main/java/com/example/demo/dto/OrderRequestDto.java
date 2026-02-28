package com.example.demo.dto;

import java.util.List;

public class OrderRequestDto {
    public String id;
    public String tableNo;
    public String notes;
    public String status;
    public List<OrderItemDto> items;
}
