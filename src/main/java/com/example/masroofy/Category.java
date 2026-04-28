package com.example.masroofy;
import java.util.*;

public class Category {
    private int id;
    private String name;
    private int cycleId;
    public int getid(){
        return id;
    }
    public String getName() {
        return name;
    }
    public int getCycleId() {
        return cycleId;
    }
    public List<String >getcategories(List<Category> C){
        List<String> categoryNames = new ArrayList<>();
        for (Category category : C) {
            categoryNames.add(category.getName());
        }
        return categoryNames;
    }
}
