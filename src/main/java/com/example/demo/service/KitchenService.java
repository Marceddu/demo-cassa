package com.example.demo.service;

import com.example.demo.model.Dish;
import com.example.demo.model.Kitchen;
import com.example.demo.repo.DishRepository;
import com.example.demo.repo.KitchenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class KitchenService {

    private final KitchenRepository kitchenRepository;
    private final DishRepository dishRepository;

    public KitchenService(KitchenRepository kitchenRepository, DishRepository dishRepository) {
        this.kitchenRepository = kitchenRepository;
        this.dishRepository = dishRepository;
    }

    @Transactional(readOnly = true)
    public List<Kitchen> listActive() {
        return kitchenRepository.findByActiveTrueOrderBySortOrderAscNameAsc();
    }

    @Transactional(readOnly = true)
    public List<Dish> listActiveDishesByKitchen(Long kitchenId) {
        return dishRepository.findByKitchenIdAndActiveTrueOrderByNameAsc(kitchenId);
    }
}
