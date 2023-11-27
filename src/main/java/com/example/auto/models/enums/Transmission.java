package com.example.auto.models.enums;

public enum Transmission {
    MANUAL(0), AUTOMATIC(1);
    private int transmission;
    private Transmission(int transmission){
        this.transmission = transmission;
    }
    @Override
    public String toString() {
        return String.valueOf(transmission);
    }
}
