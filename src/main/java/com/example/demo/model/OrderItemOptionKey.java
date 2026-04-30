package com.example.demo.model;

import java.io.Serializable;
import java.util.Objects;

public class OrderItemOptionKey implements Serializable {
    public Long orderItemId;
    public Long optionId;
    public OrderItemOptionKey() {}
    public OrderItemOptionKey(Long orderItemId, Long optionId){this.orderItemId=orderItemId;this.optionId=optionId;}
    @Override public boolean equals(Object o){ if(this==o) return true; if(!(o instanceof OrderItemOptionKey k)) return false; return Objects.equals(orderItemId,k.orderItemId)&&Objects.equals(optionId,k.optionId);} 
    @Override public int hashCode(){return Objects.hash(orderItemId,optionId);} 
}
