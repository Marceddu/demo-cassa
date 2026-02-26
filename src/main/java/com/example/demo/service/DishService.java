package com.example.demo.service;

import com.example.demo.dto.DishDto;
import com.example.demo.model.Dish;
import com.example.demo.repo.DishRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishService {

    private final DishRepository dishRepository;

    public DishService(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    @Transactional(readOnly = true)
    public List<Dish> listActive() {
        return dishRepository.findByActiveTrueOrderByNameAsc();
    }

    @Transactional(readOnly = true)
    public List<Dish> listAll() {
        return dishRepository.findAllByOrderByNameAsc();
    }

    @Transactional
    public Dish create(DishDto dto) {
        Dish dish = new Dish();
        dish.setName(dto.name.trim());
        dish.setPrice(dto.price);
        dish.setActive(dto.active == null || dto.active);
        return dishRepository.save(dish);
    }

    @Transactional
    public Dish update(Long id, DishDto dto) {
        Dish dish = dishRepository.findById(id).orElseThrow();
        if (dto.name != null) dish.setName(dto.name.trim());
        if (dto.price != null) dish.setPrice(dto.price);
        if (dto.active != null) dish.setActive(dto.active);
        return dishRepository.save(dish);
    }

    @Transactional
    public void delete(Long id) {
        dishRepository.deleteById(id);
    }
}
