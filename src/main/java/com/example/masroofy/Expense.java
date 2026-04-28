package com.example.masroofy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Expense {

    private String type;
    private int id;
    private int cycleid;
    private double amount;
    private int categoryid;
    private LocalDateTime date;

    // full
    public Expense(int id,double amount, String type, int categoryid, LocalDateTime date, int cycleid){
        this.id = id;
        this.cycleid = cycleid;
        this.amount = amount;
        this.categoryid = categoryid;
        this.type = type;
        this.date = date;
    }

    // add
    public Expense(double amount, String type, int categoryid, LocalDateTime date, int cycleid) {
        this.cycleid = cycleid;
        this.amount = amount;
        this.categoryid = categoryid;
        this.type = type;
        this.date = date;
    }

    // edit
    public Expense(int id, double amount, int categoryid) {
        this.id = id;
        this.amount = amount;
        this.categoryid = categoryid;
    }
    public double getAllAmount(List<Expense> e) {
        double total = 0;

        for (int i = 0; i < e.size(); i++) {
            total += e.get(i).getAmount();
        }

        return total;
    }

    public int getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getDate(){
        return date;
    }
    public int getCategoryid() {
        return categoryid;
    }
    public String getType() {
        return type;
    }

     public int getCycleid() {
        return cycleid;
    }
}
