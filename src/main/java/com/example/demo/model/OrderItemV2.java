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
 public Long getId(){return id;}
 public KitchenOrder getKitchenOrder(){return kitchenOrder;}
 public Integer getQty(){return qty;}
 public Long getDishId(){return dishId;}
 public String getDishNameSnapshot(){return dishNameSnapshot;}
 public String getItemNote(){return itemNote;}
 public Integer getWeightGramsSnapshot(){return weightGramsSnapshot;}
 public void setCustomerOrder(CustomerOrderV2 v){customerOrder=v;}
 public void setKitchenOrder(KitchenOrder v){kitchenOrder=v;}
 public void setKitchen(Kitchen v){kitchen=v;}
 public void setDishId(Long v){dishId=v;}
 public void setDishNameSnapshot(String v){dishNameSnapshot=v;}
 public void setQty(Integer v){qty=v;}
 public void setUnitPriceSnapshot(BigDecimal v){unitPriceSnapshot=v;}
 public void setLineTotal(BigDecimal v){lineTotal=v;}
 public void setWeightGramsSnapshot(Integer v){weightGramsSnapshot=v;}
 public void setItemNote(String v){itemNote=v;}
}
