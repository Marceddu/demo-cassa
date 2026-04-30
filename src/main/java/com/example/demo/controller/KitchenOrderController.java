package com.example.demo.controller;

import com.example.demo.dto.KitchenOrderDto;
import com.example.demo.model.KitchenOrder;
import com.example.demo.model.KitchenOrderStatus;
import com.example.demo.repo.KitchenOrderRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class KitchenOrderController {
    private final KitchenOrderRepository kitchenOrderRepository;
    public KitchenOrderController(KitchenOrderRepository kitchenOrderRepository){this.kitchenOrderRepository=kitchenOrderRepository;}

    @GetMapping("/kitchens/{kitchenId}/orders")
    public List<KitchenOrderDto> list(@PathVariable Long kitchenId){
        return kitchenOrderRepository.findByKitchenIdAndStatusNotOrderByCreatedAtAsc(kitchenId, KitchenOrderStatus.CANCELLED).stream().map(this::map).toList();
    }

    @DeleteMapping("/kitchen-orders/{id}")
    @Transactional
    public ResponseEntity<Void> cancel(@PathVariable Long id){
        KitchenOrder ko=kitchenOrderRepository.findById(id).orElseThrow();
        ko.setStatus(KitchenOrderStatus.CANCELLED);
        ko.setCancelledAt(OffsetDateTime.now());
        kitchenOrderRepository.save(ko);
        return ResponseEntity.noContent().build();
    }

    private KitchenOrderDto map(KitchenOrder k){
        KitchenOrderDto d=new KitchenOrderDto();
        d.id=k.getId(); d.customerOrderId=k.getCustomerOrder().getId(); d.kitchenId=k.getKitchen().getId();
        d.progressiveNumber=k.getProgressiveNumber(); d.status=k.getStatus().name(); d.kitchenTotalAmount=k.getKitchenTotalAmount(); d.cancelledAt=k.getCancelledAt();
        return d;
    }
}
