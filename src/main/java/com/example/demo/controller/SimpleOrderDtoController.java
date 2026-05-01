package com.example.demo.controller;

import com.example.demo.dto.DishDto;
import com.example.demo.dto.OrderDto;
import com.example.demo.dto.OrderRequestDto;
import com.example.demo.dto.OrderResponseDto;
import com.example.demo.dto.StatsSummaryDto;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.model.Dish;
import com.example.demo.model.Order;
import com.example.demo.model.OrderStatus;
import com.example.demo.service.DishService;
import com.example.demo.service.OrderService;
import com.example.demo.service.PrintService;
import com.example.demo.service.StatsService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
public class SimpleOrderDtoController {

	private final OrderService orderService;
	private final DishService dishService;
	private final StatsService statsService;
	private final PrintService printService;
	private final KitchenOrderController kitchenOrderController;

	public SimpleOrderDtoController(OrderService orderService, DishService dishService, StatsService statsService,
			PrintService printService, KitchenOrderController kitchenOrderController) {
		this.orderService = orderService;
		this.dishService = dishService;
		this.statsService = statsService;
		this.printService = printService;
		this.kitchenOrderController = kitchenOrderController;
	}

	@GetMapping("/getordini")
	public List<OrderResponseDto> getOrdini(@RequestParam(required = false) String status,
			@RequestParam(required = false) Integer sinceMinutes) {
		List<OrderStatus> statuses = (status == null || status.isBlank()) ? Arrays.asList(OrderStatus.values())
				: Arrays.stream(status.split(",")).map(String::trim).map(String::toUpperCase).map(OrderStatus::valueOf)
						.toList();

		List<Order> list = (sinceMinutes != null)
				? orderService.listByStatusesSince(statuses, OffsetDateTime.now().minusMinutes(sinceMinutes))
				: orderService.listByStatuses(statuses);

		return list.stream().map(OrderMapper::toResponse).collect(Collectors.toList());
	}

	@PostMapping("/setordine")
	public ResponseEntity<?> setOrdine(@Valid @RequestBody OrderRequestDto dto) {
		if (dto.id != null && !dto.id.isBlank()) {
			return orderService.updateFromDto(dto).map(o -> ResponseEntity.ok(OrderMapper.toResponse(o)))
					.orElseGet(() -> ResponseEntity.notFound().build());
		}
		OrderDto orderDto = orderService.createFromDto(dto);

		orderDto.getIdKitchenOrder().forEach(ko -> {
			
			try {
				String receipt = kitchenOrderController.receipt(String.valueOf(ko));
				printService.printReceipt(receipt);
				receipt = kitchenOrderController.receipt(String.valueOf(ko));
				printService.printReceipt(receipt);
			} catch (Exception ignored) {
			}

		});
		return ResponseEntity.ok(OrderMapper.toResponse(orderDto.getOrder()));
	}

	@GetMapping("/gettotali")
	public Map<String, Object> getTotali() {
		Map<OrderStatus, Long> totals = orderService.totalsByStatus();
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

	@DeleteMapping("/deleteordine/{id}")
	public ResponseEntity<?> deleteOrdine(@PathVariable String id) {
		orderService.delete(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/getPiatti")
	public List<DishDto> getPiatti() {
		return dishService.listActive().stream().map(this::toDishDto).toList();
	}

	@GetMapping("/admin/piatti")
	public List<DishDto> getPiattiAdmin() {
		return dishService.listAll().stream().map(this::toDishDto).toList();
	}

	@PostMapping("/admin/piatti")
	public DishDto createPiatto(@RequestBody DishDto dto) {
		return toDishDto(dishService.create(dto));
	}

	@PutMapping("/admin/piatti/{id}")
	public DishDto updatePiatto(@PathVariable Long id, @RequestBody DishDto dto) {
		return toDishDto(dishService.update(id, dto));
	}

	@DeleteMapping("/admin/piatti/{id}")
	public ResponseEntity<?> deletePiatto(@PathVariable Long id) {
		dishService.delete(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/admin/counters")
	public StatsSummaryDto getCounters() {
		return statsService.latestOrCurrent();
	}

	@PostMapping("/admin/counters/refresh")
	public StatsSummaryDto refreshCounters() {
		return statsService.refreshSnapshot();
	}

	@GetMapping("/admin/counters/export")
	public ResponseEntity<String> exportCounters() {
		String txt = statsService.exportAsTxt();
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=counters.txt")
				.contentType(MediaType.TEXT_PLAIN).body(txt);
	}

	private DishDto toDishDto(Dish dish) {
		DishDto dto = new DishDto();
		dto.id = dish.getId();
		dto.name = dish.getName();
		dto.price = dish.getPrice();
		dto.active = dish.isActive();
		dto.kitchenId = dish.getKitchen() != null ? dish.getKitchen().getId() : null;
		dto.weightGrams = dish.getWeightGrams();
		return dto;
	}
}
