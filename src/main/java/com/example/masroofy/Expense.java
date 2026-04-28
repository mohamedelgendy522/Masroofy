package com.example.masroofy;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Expense {

    private String type;
    private int id;
    private int cycleid;
    private double amount;
    private int categoryid;
    private LocalDateTime date;

    public Expense(int id,double amount, String type, int categoryid, LocalDateTime date, int cycleid){
        this.id = id;
        this.cycleid = cycleid;
        this.amount = amount;
        this.categoryid = categoryid;
        this.type = type;
        this.date = date;
    }

    public Expense(double amount, String type, int categoryid, LocalDateTime date, int cycleid) {
        this.cycleid = cycleid;
        this.amount = amount;
        this.categoryid = categoryid;
        this.type = type;
        this.date = date;
    }

    public Expense(int id, double amount, int categoryid) {
        this.id = id;
        this.amount = amount;
        this.categoryid = categoryid;
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
