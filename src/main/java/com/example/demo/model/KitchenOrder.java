package com.example.demo.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "kitchen_orders")
public class KitchenOrder {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne(optional=false) @JoinColumn(name="customer_order_id")
  private CustomerOrderV2 customerOrder;
  @ManyToOne(optional=false) @JoinColumn(name="kitchen_id")
  private Kitchen kitchen;
  private Long progressiveNumber;
  @Enumerated(EnumType.STRING)
  private KitchenOrderStatus status=KitchenOrderStatus.NEW;
  private BigDecimal kitchenTotalAmount=BigDecimal.ZERO;
  private OffsetDateTime cancelledAt;
  private OffsetDateTime createdAt;
  private OffsetDateTime updatedAt;
  @PrePersist void p(){createdAt=OffsetDateTime.now();updatedAt=createdAt;}
  @PreUpdate void u(){updatedAt=OffsetDateTime.now();}
  public Long getId(){return id;} public CustomerOrderV2 getCustomerOrder(){return customerOrder;} public void setCustomerOrder(CustomerOrderV2 c){customerOrder=c;}
  public Kitchen getKitchen(){return kitchen;} public void setKitchen(Kitchen k){kitchen=k;} public Long getProgressiveNumber(){return progressiveNumber;} public void setProgressiveNumber(Long p){progressiveNumber=p;}
  public KitchenOrderStatus getStatus(){return status;} public void setStatus(KitchenOrderStatus s){status=s;} public BigDecimal getKitchenTotalAmount(){return kitchenTotalAmount;} public void setKitchenTotalAmount(BigDecimal v){kitchenTotalAmount=v;}
  public OffsetDateTime getCancelledAt(){return cancelledAt;} public void setCancelledAt(OffsetDateTime c){cancelledAt=c;}
}
