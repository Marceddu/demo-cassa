package com.example.demo.mapper;

import com.example.demo.dto.*;
import com.example.demo.model.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public final class OrderMapper {

    private OrderMapper() {}

    // ---- to Entity (create) ----
    public static Order toEntityForCreate(OrderRequestDto dto) {
        Order o = new Order();
        o.setTableNo(dto.tableNo);
        o.setNotes(dto.notes);
        if (dto.status != null) {
            o.setStatus(OrderStatus.valueOf(dto.status.toUpperCase()));
        }
        if (dto.items != null && !dto.items.isEmpty()) {
            List<OrderItem> items = new ArrayList<>();
            int pos = 0;
            for (OrderItemDto it : dto.items) {
                OrderItem e = new OrderItem();
                e.setName(it.name);
                e.setQty(it.qty == null ? 1 : it.qty);
                e.setItemNote(it.itemNote);
                e.setPosition(it.position != null ? it.position : pos++);
                e.setOrder(o);
                items.add(e);
            }
            o.setItems(items);
        }
        return o;
    }

    // ---- merge su entity (update) ----
    public static void mergeIntoEntityForUpdate(OrderRequestDto dto, Order o) {
        if (dto.tableNo != null) o.setTableNo(dto.tableNo);
        if (dto.notes  != null) o.setNotes(dto.notes);
        if (dto.status != null) o.setStatus(OrderStatus.valueOf(dto.status.toUpperCase()));

        if (dto.items != null) {
            // rimpiazza lista mantenendo orphanRemoval
            o.getItems().clear();
            int pos = 0;
            for (OrderItemDto it : dto.items) {
                OrderItem e = new OrderItem();
                e.setName(it.name);
                e.setQty(it.qty == null ? 1 : it.qty);
                e.setItemNote(it.itemNote);
                e.setPosition(it.position != null ? it.position : pos++);
                e.setOrder(o);
                o.getItems().add(e);
            }
            // ordine stabile
            o.getItems().sort(Comparator.comparing(OrderItem::getPosition)
                                        .thenComparing(x -> x.getId() == null ? 0L : x.getId()));
        }
    }

    // ---- to DTO (response) ----
    public static OrderResponseDto toResponse(Order o) {
        OrderResponseDto r = new OrderResponseDto();
        r.id = o.getId();
        r.tableNo = o.getTableNo();
        r.notes = o.getNotes();
        r.status = o.getStatus().name();
        r.createdAt = o.getCreatedAt();
        r.updatedAt = o.getUpdatedAt();

        if (o.getItems() != null) {
            r.items = o.getItems().stream().map(OrderMapper::toItemResponse).collect(Collectors.toList());
        }
        return r;
    }

    private static OrderItemResponseDto toItemResponse(OrderItem it) {
        OrderItemResponseDto r = new OrderItemResponseDto();
        r.id = it.getId();
        r.position = it.getPosition();
        r.name = it.getName();
        r.qty = it.getQty();
        r.itemNote = it.getItemNote();
        return r;
    }
}
