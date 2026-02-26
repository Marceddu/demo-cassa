package com.example.demo.dto;

import java.util.List;

public class OrderRequestDto {
    public String id;            // se presente -> UPDATE, altrimenti CREATE
    public String tableNo;
    public String notes;
    public String status;        // NEW|IN_PROGRESS|READY|DONE (opzionale in create)
    public List<OrderItemDto> items; // opzionale in update: se presente rimpiazza la lista
}
