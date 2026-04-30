package com.example.demo.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "customer_orders")
public class CustomerOrderV2 {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String tableName;
  private String orderNote;
  private BigDecimal totalAmount = BigDecimal.ZERO;
  private OffsetDateTime createdAt;
  private OffsetDateTime updatedAt;
  @PrePersist void p(){createdAt=OffsetDateTime.now();updatedAt=createdAt;}
  @PreUpdate void u(){updatedAt=OffsetDateTime.now();}
  public Long getId(){return id;} public String getTableName(){return tableName;} public void setTableName(String v){tableName=v;}
  public String getOrderNote(){return orderNote;} public void setOrderNote(String v){orderNote=v;}
  public BigDecimal getTotalAmount(){return totalAmount;} public void setTotalAmount(BigDecimal v){totalAmount=v;}
}
