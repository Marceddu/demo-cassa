package com.example.demo.controller;

import com.example.demo.dto.OrderOptionDto;
import com.example.demo.model.OrderOption;
import com.example.demo.service.OrderOptionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order-options")
public class OrderOptionController {
    private final OrderOptionService service;
    public OrderOptionController(OrderOptionService service){this.service=service;}

    @GetMapping
    public List<OrderOptionDto> list(){ return service.listActive().stream().map(this::map).toList(); }

    @PostMapping
    public OrderOptionDto create(@RequestBody OrderOptionDto dto){ return map(service.create(dto)); }

    @PatchMapping("/{id}/toggle")
    public OrderOptionDto toggle(@PathVariable Long id){ return map(service.toggle(id)); }

    private OrderOptionDto map(OrderOption o){
        OrderOptionDto d = new OrderOptionDto();
        d.id = o.getId(); d.label = o.getLabel(); d.active = o.isActive(); d.sortOrder = o.getSortOrder();
        return d;
    }
}
