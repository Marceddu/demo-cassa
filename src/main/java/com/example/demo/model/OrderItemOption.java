package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "order_item_options")
@IdClass(OrderItemOptionKey.class)
public class OrderItemOption {
    @Id
    @Column(name = "order_item_id")
    private Long orderItemId;
    @Id
    @Column(name = "option_id")
    private Long optionId;
    private String labelSnapshot;
    public Long getOrderItemId(){return orderItemId;} public void setOrderItemId(Long v){orderItemId=v;}
    public Long getOptionId(){return optionId;} public void setOptionId(Long v){optionId=v;}
    public String getLabelSnapshot(){return labelSnapshot;} public void setLabelSnapshot(String v){labelSnapshot=v;}
}
