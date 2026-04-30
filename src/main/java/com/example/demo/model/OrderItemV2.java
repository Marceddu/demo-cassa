package com.example.demo.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name="order_items_v2")
public class OrderItemV2 {
 @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
 @ManyToOne(optional=false) @JoinColumn(name="customer_order_id") private CustomerOrderV2 customerOrder;
 @ManyToOne(optional=false) @JoinColumn(name="kitchen_order_id") private KitchenOrder kitchenOrder;
 @ManyToOne(optional=false) @JoinColumn(name="kitchen_id") private Kitchen kitchen;
 private Long dishId; private String dishNameSnapshot; private Integer qty;
 private BigDecimal unitPriceSnapshot; private BigDecimal lineTotal; private Integer weightGramsSnapshot; private String itemNote;
 private OffsetDateTime createdAt; @PrePersist void p(){createdAt=OffsetDateTime.now();}
}
