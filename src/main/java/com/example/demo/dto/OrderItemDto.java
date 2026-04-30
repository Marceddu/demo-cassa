package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class OrderItemDto {
    public Long id;

    @NotNull(message = "must be provided")
    public Long dishId;

    public Integer position;

    @NotNull(message = "must be provided")
    @Positive(message = "must be > 0")
    public Integer qty;

    public String itemNote;
}
