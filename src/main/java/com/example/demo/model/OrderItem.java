package com.example.demo.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(
    name = "order_items",
    indexes = {
        @Index(name = "idx_item_order_id", columnList = "order_id")
    }
)
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Integer position;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false)
    private Integer qty = 1;

    @Column(length = 300)
    private String itemNote;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    @JsonBackReference
    private Order order;

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getPosition() { return position; }
    public void setPosition(Integer position) { this.position = position; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getQty() { return qty; }
    public void setQty(Integer qty) { this.qty = qty; }

    public String getItemNote() { return itemNote; }
    public void setItemNote(String itemNote) { this.itemNote = itemNote; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
}
