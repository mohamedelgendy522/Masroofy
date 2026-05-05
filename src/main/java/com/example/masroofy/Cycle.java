package com.example.masroofy;
import java.time.LocalDate;


public class Cycle {
    private int id;
    private int user_id;
    private double total_budget;
    private LocalDate start_date;
    private LocalDate end_date;

    public double getRemainigBalance(double totalspent){
        return total_budget - totalspent;
    }

    public double calulatedailyBudget(double totalspent){
        long daysLeft = java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), end_date);
        if (daysLeft <= 0) return 0;
        return getRemainigBalance(totalspent) / daysLeft;
    }

     public Cycle(int id, int user_id, double total_budget, LocalDate start_date, LocalDate end_date) {
        this.id = id;
        this.user_id = user_id;
        this.total_budget = total_budget;
        this.start_date = start_date;
        this.end_date = end_date;
    }
    public Cycle(int user_id, double total_budget, LocalDate start_date, LocalDate end_date) {
        this.user_id = user_id;
        this.total_budget = total_budget;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    public int getId(){
        return id;
    }

    public int getUserId() {
        return user_id;
    }

    public double getTotalBudget() {
        return total_budget;
    }

    public LocalDate getStartDate() {
        return start_date;
    }

    public LocalDate getEndDate() {
        return end_date;
    }
}
