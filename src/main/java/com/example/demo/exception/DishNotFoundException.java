package com.example.demo.exception;

public class DishNotFoundException extends RuntimeException {
    public DishNotFoundException(Long dishId) {
        super("Dish not found: " + dishId);
    }
}
