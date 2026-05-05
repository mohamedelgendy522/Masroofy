package com.example.masroofy;

public class Category {
    private int id;
    private String name;
    private int cycleId;

    public Category(int id, String name, int cycleId) {
        this.id = id;
        this.name = name;
        this.cycleId = cycleId;
    }
    public int getId(){
        return id;
    }
    public String getName() {
        return name;
    }
    public int getCycleId() {
        return cycleId;
    }
}