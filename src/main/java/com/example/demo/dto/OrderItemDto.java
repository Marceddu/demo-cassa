package com.example.demo.dto;

public class OrderItemDto {
    public Long id;          // opzionale (per update)
    public Integer position; // opzionale, se null viene assegnata in append
    public String name;
    public Integer qty;
    public String itemNote;
}
