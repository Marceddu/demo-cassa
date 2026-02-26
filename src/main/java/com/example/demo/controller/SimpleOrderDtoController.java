package com.example.demo.controller;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.OrderRequestDto;
import com.example.demo.dto.OrderResponseDto;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.model.Order;
import com.example.demo.model.OrderStatus;
import com.example.demo.service.OrderService;

@RestController
@CrossOrigin
public class SimpleOrderDtoController {

    private final OrderService service;
    
    private Map<String, Integer> contatori = new HashMap<String, Integer>();

    public SimpleOrderDtoController(OrderService service) {
        this.service = service;
    }

    // -------- GET /getordini?status=NEW,READY&sinceMinutes=240
    @GetMapping("/getordini")
    public List<OrderResponseDto> getOrdini(@RequestParam(required = false) String status,
                                            @RequestParam(required = false) Integer sinceMinutes) {

        List<OrderStatus> statuses = (status == null || status.isBlank())
                ? Arrays.asList(OrderStatus.values())
                : Arrays.stream(status.split(","))
                        .map(String::trim).map(String::toUpperCase)
                        .map(OrderStatus::valueOf)
                        .collect(Collectors.toList());

        List<Order> list = (sinceMinutes != null)
                ? service.listByStatusesSince(statuses, OffsetDateTime.now().minusMinutes(sinceMinutes))
                : service.listByStatuses(statuses);

        return list.stream().map(OrderMapper::toResponse).collect(Collectors.toList());
    }

    // -------- POST /setordine  (create/update)
    @PostMapping("/setordine")
    public ResponseEntity<?> setOrdine(@RequestBody OrderRequestDto dto) {
    	
    	dto.items.stream().forEach(a -> {
    		Integer conto = contatori.get(a.name);
    		if (conto == null || conto == 0) {
    			conto = 1;
    		} else {
    			conto+=a.qty;
    		}
    		contatori.put(a.name, conto);
    	});
        if (dto.id != null && !dto.id.isBlank()) {
            // UPDATE
            return service.updateFromDto(dto)
                    .map(o -> ResponseEntity.ok(OrderMapper.toResponse(o)))
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } else {
            // CREATE
            Order saved = service.createFromDto(dto);
            System.out.println(contatori);
            return ResponseEntity.ok(OrderMapper.toResponse(saved));
        }
    }

    // -------- GET /gettotali
    @GetMapping("/gettotali")
    public Map<String, Object> getTotali() {
        Map<OrderStatus, Long> totals = service.totalsByStatus();
        Map<String, Object> resp = new LinkedHashMap<>();

        long total = 0;
        for (OrderStatus s : OrderStatus.values()) {
            long n = totals.getOrDefault(s, 0L);
            resp.put(s.name(), n);
            total += n;
        }
        resp.put("TOTAL", total);
        return resp;
    }

    // -------- DELETE /deleteordine/{id}
    @DeleteMapping("/deleteordine/{id}")
    public ResponseEntity<?> deleteOrdine(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/getPiatti")
    public List<String> getPiatti() {
    	
        List<String> resp = new ArrayList<String>();
        
        resp.addAll(Arrays.asList("PRUPPEDDA", "PANE VRATTAU", "COMPLETO", "PANE VRATTAU SENZA LATTOSIO", "COMPLETO SENZA LATTOSIO"));
        
        return resp;
    }
}
