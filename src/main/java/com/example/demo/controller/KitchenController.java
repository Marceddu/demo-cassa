package com.example.demo.controller;

import com.example.demo.dto.DishDto;
import com.example.demo.dto.KitchenDto;
import com.example.demo.model.Dish;
import com.example.demo.model.Kitchen;
import com.example.demo.service.KitchenService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/kitchens")
public class KitchenController {

    private final KitchenService kitchenService;

    public KitchenController(KitchenService kitchenService) {
        this.kitchenService = kitchenService;
    }

    @GetMapping
    public List<KitchenDto> listKitchens() {
        return kitchenService.listActive().stream().map(this::toKitchenDto).toList();
    }

    @GetMapping("/{kitchenId}/dishes")
    public List<DishDto> listDishes(@PathVariable Long kitchenId) {
        return kitchenService.listActiveDishesByKitchen(kitchenId).stream().map(this::toDishDto).toList();
    }

    private KitchenDto toKitchenDto(Kitchen kitchen) {
        KitchenDto dto = new KitchenDto();
        dto.id = kitchen.getId();
        dto.name = kitchen.getName();
        dto.active = kitchen.isActive();
        dto.sortOrder = kitchen.getSortOrder();
        return dto;
    }

    private DishDto toDishDto(Dish dish) {
        DishDto dto = new DishDto();
        dto.id = dish.getId();
        dto.name = dish.getName();
        dto.price = dish.getPrice();
        dto.active = dish.isActive();
        dto.kitchenId = dish.getKitchen() != null ? dish.getKitchen().getId() : null;
        return dto;
    }
}
