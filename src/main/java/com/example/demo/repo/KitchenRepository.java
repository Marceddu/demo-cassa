package com.example.demo.repo;

import com.example.demo.model.Kitchen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface KitchenRepository extends JpaRepository<Kitchen, Long> {
    List<Kitchen> findByActiveTrueOrderBySortOrderAscNameAsc();
    Optional<Kitchen> findByNameIgnoreCase(String name);
}
