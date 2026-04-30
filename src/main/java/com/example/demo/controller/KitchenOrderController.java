package com.example.demo.controller;

import com.example.demo.dto.KitchenOrderDto;
import com.example.demo.model.KitchenOrder;
import com.example.demo.model.KitchenOrderStatus;
import com.example.demo.model.OrderItemV2;
import com.example.demo.repo.KitchenOrderRepository;
import com.example.demo.repo.OrderItemOptionRepository;
import com.example.demo.repo.OrderItemV2Repository;
import com.example.demo.util.WeightFormatUtil;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class KitchenOrderController {
    private final KitchenOrderRepository kitchenOrderRepository;
    private final OrderItemV2Repository orderItemV2Repository;
    private final OrderItemOptionRepository orderItemOptionRepository;

    public KitchenOrderController(KitchenOrderRepository kitchenOrderRepository, OrderItemV2Repository orderItemV2Repository, OrderItemOptionRepository orderItemOptionRepository){
        this.kitchenOrderRepository=kitchenOrderRepository;
        this.orderItemV2Repository = orderItemV2Repository;
        this.orderItemOptionRepository = orderItemOptionRepository;
    }

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

    @GetMapping(value = "/kitchen-orders/{id}/receipt", produces = MediaType.TEXT_PLAIN_VALUE)
    public String receipt(@PathVariable Long id) {
        KitchenOrder ko = kitchenOrderRepository.findById(id).orElseThrow();
        List<OrderItemV2> items = orderItemV2Repository.findByKitchenOrderId(id);
        int width = 16;
        List<String> lines = new ArrayList<>();
        lines.add("Ordine");
        lines.add("N " + ko.getProgressiveNumber());
        for (OrderItemV2 it : items) {
            String row = it.getQty() + " " + (it.getDishNameSnapshot() == null ? "" : it.getDishNameSnapshot());
            if (row.length() > width - 2) row = row.substring(0, width - 2);
            lines.add(row);
        }
        return box(lines, width);
    }

    private String box(List<String> lines, int width) { String border = "*".repeat(width + 2); StringBuilder sb = new StringBuilder(); sb.append(border).append('\n'); sb.append("*").append(" ".repeat(width)).append("*").append('\n'); for (String line : lines) sb.append("*").append(center(line, width)).append("*").append('\n'); sb.append("*").append(" ".repeat(width)).append("*").append('\n'); sb.append(border); return sb.toString(); }
    static String center(String text, int width) { if (text == null) text = ""; if (text.length() >= width) return text.substring(0, width); int left = (width - text.length()) / 2; int right = width - text.length() - left; return " ".repeat(left) + text + " ".repeat(right); }

    private KitchenOrderDto map(KitchenOrder k){
        KitchenOrderDto d=new KitchenOrderDto();
        d.id=k.getId(); d.customerOrderId=k.getCustomerOrder().getId(); d.kitchenId=k.getKitchen().getId();
        d.progressiveNumber=k.getProgressiveNumber(); d.status=k.getStatus().name(); d.kitchenTotalAmount=k.getKitchenTotalAmount(); d.cancelledAt=k.getCancelledAt();
        d.tableNo = k.getCustomerOrder().getTableName();
        d.orderNote = k.getCustomerOrder().getOrderNote();
        List<OrderItemV2> items = orderItemV2Repository.findByKitchenOrderId(k.getId());
        d.items = items.stream().map(i -> {
            KitchenOrderDto.KitchenOrderItemDto ii = new KitchenOrderDto.KitchenOrderItemDto();
            ii.qty = i.getQty(); ii.dishName = i.getDishNameSnapshot(); ii.itemNote = i.getItemNote();
            ii.optionLabels = orderItemOptionRepository.findByOrderItemId(i.getId()).stream().map(x -> x.getLabelSnapshot()).toList();
            return ii;
        }).toList();
        int total = items.stream().mapToInt(i -> (i.getWeightGramsSnapshot() == null ? 0 : i.getWeightGramsSnapshot()) * (i.getQty() == null ? 0 : i.getQty())).sum();
        d.totalWeightFormatted = WeightFormatUtil.format(total);
        return d;
    }
}
