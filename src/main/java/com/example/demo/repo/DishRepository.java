package com.example.demo.repo;

import com.example.demo.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DishRepository extends JpaRepository<Dish, Long> {
    List<Dish> findByActiveTrueOrderByNameAsc();
    List<Dish> findAllByOrderByNameAsc();
    Optional<Dish> findByNameIgnoreCase(String name);
}
