package com.example.demo.repo;

import com.example.demo.model.OrderOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderOptionRepository extends JpaRepository<OrderOption, Long> {
    List<OrderOption> findByActiveTrueOrderBySortOrderAscLabelAsc();
    java.util.Optional<OrderOption> findByLabelIgnoreCase(String label);
}
