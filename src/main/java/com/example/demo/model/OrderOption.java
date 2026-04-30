package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "order_options")
public class OrderOption {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String label;
    private boolean active = true;
    private int sortOrder = 0;
    public Long getId(){return id;} public String getLabel(){return label;} public void setLabel(String l){label=l;}
    public boolean isActive(){return active;} public void setActive(boolean a){active=a;}
    public int getSortOrder(){return sortOrder;} public void setSortOrder(int s){sortOrder=s;}
}
