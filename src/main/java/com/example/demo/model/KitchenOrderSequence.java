package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name="kitchen_order_sequences")
public class KitchenOrderSequence {
 @Id
 @Column(name="kitchen_id")
 private Long kitchenId;
 @OneToOne @MapsId @JoinColumn(name="kitchen_id")
 private Kitchen kitchen;
 private Long currentValue=0L;
 public Long getKitchenId(){return kitchenId;} public Kitchen getKitchen(){return kitchen;} public void setKitchen(Kitchen k){kitchen=k;}
 public Long getCurrentValue(){return currentValue;} public void setCurrentValue(Long c){currentValue=c;}
}
