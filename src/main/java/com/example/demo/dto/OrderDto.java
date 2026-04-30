package com.example.demo.dto;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.model.Order;

public class OrderDto {
	Order order;
	List<String> idKitchenOrder = new ArrayList<String>();
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	public List<String> getIdKitchenOrder() {
		return idKitchenOrder;
	}
	public void setIdKitchenOrder(List<String> idKitchenOrder) {
		this.idKitchenOrder = idKitchenOrder;
	}

}
