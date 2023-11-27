package com.example.auto.models.enums;

public enum Engine {
    GASOLINE(0), DIESEL(1), ELECTRIC(2), HYBRID(3);
    private int engine;
    private Engine(int engine){
        this.engine = engine;
    }
    @Override
    public String toString() {
        return String.valueOf(engine);
    }

}
