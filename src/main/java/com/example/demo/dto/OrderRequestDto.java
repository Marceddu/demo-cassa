package com.example.demo.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public class OrderRequestDto {
    public String id;
    public String tableNo;
    public String notes;

    @Pattern(regexp = "NEW|IN_PROGRESS|READY|DONE", message = "must be one of NEW, IN_PROGRESS, READY, DONE")
    public String status;

    public List<@Valid OrderItemDto> items;
}
