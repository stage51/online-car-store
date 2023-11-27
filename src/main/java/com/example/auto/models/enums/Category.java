package com.example.auto.models.enums;

public enum Category {
    CAR(0), BUSS(1), TRUCK(2), MOTORCYCLE(3);
    private int category;
    private Category(int category){
        this.category = category;
    }
    @Override
    public String toString() {
        return String.valueOf(category);
    }

}
