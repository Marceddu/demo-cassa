package com.example.demo.mapper;

import com.example.demo.dto.OrderItemResponseDto;
import com.example.demo.dto.OrderResponseDto;
import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;

import java.util.stream.Collectors;

public final class OrderMapper {
    private OrderMapper() {}

    public static OrderResponseDto toResponse(Order o) {
        OrderResponseDto r = new OrderResponseDto();
        r.id = o.getId();
        r.tableNo = o.getTableNo();
        r.notes = o.getNotes();
        r.status = o.getStatus().name();
        r.totalItems = o.getTotalItems();
        r.totalAmount = o.getTotalAmount();
        r.createdAt = o.getCreatedAt();
        r.updatedAt = o.getUpdatedAt();
        r.items = o.getItems().stream().map(OrderMapper::toItemResponse).collect(Collectors.toList());
        return r;
    }

    private static OrderItemResponseDto toItemResponse(OrderItem it) {
        OrderItemResponseDto r = new OrderItemResponseDto();
        r.id = it.getId();
        r.position = it.getPosition();
        r.name = it.getName();
        r.qty = it.getQty();
        r.unitPrice = it.getUnitPrice();
        r.lineTotal = it.getLineTotal();
        r.itemNote = it.getItemNote();
        return r;
    }
}
