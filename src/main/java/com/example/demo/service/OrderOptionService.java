package com.example.demo.service;

import com.example.demo.dto.OrderOptionDto;
import com.example.demo.model.OrderOption;
import com.example.demo.repo.OrderOptionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderOptionService {
    private final OrderOptionRepository repository;
    public OrderOptionService(OrderOptionRepository repository){this.repository=repository;}
    public List<OrderOption> listActive(){return repository.findByActiveTrueOrderBySortOrderAscLabelAsc();}
    public OrderOption create(OrderOptionDto dto){
        var existing = repository.findByLabelIgnoreCase(dto.label);
        if (existing.isPresent()) return existing.get();
        OrderOption o = new OrderOption();
        o.setLabel(dto.label);
        o.setActive(dto.active == null || dto.active);
        o.setSortOrder(dto.sortOrder == null ? 0 : dto.sortOrder);
        return repository.save(o);
    }
    public OrderOption toggle(Long id){
        OrderOption o = repository.findById(id).orElseThrow();
        o.setActive(!o.isActive());
        return repository.save(o);
    }
}
