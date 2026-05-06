package com.example.masroofy;

import java.time.LocalDate;

/**
 * Represents a budget cycle for a user, defining the total budget,
 * start date, and end date of the budgeting period.
 * <p>
 * Provides utility methods for calculating remaining balance
 * and the recommended daily spending limit.
 * </p>
 */
public class Cycle {

    /** The unique identifier of this cycle. */
    private int id;

    /** The ID of the user this cycle belongs to. */
    private int user_id;

    /** The total budget allocated for this cycle. */
    private double total_budget;

    /** The start date of this cycle. */
    private LocalDate start_date;

    /** The end date of this cycle. */
    private LocalDate end_date;

    /**
     * Calculates the remaining balance by subtracting total spent from the total budget.
     *
     * @param totalspent the total amount spent so far in this cycle
     * @return the remaining balance
     */
    public double getRemainigBalance(double totalspent) {
        return total_budget - totalspent;
    }

    /**
     * Calculates the recommended daily budget by dividing the remaining balance
     * by the number of days left until the cycle ends.
     *
     * @param totalspent the total amount spent so far in this cycle
     * @return the recommended daily budget, or {@code 0} if the cycle has ended
     */
    public double calulatedailyBudget(double totalspent) {
        long daysLeft = java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), end_date);
        if (daysLeft <= 0) return 0;
        return getRemainigBalance(totalspent) / daysLeft;
    }

    /**
     * Constructs a fully populated {@code Cycle} including its database ID.
     *
     * @param id           the unique identifier of the cycle
     * @param user_id      the ID of the user this cycle belongs to
     * @param total_budget the total budget for the cycle
     * @param start_date   the start date of the cycle
     * @param end_date     the end date of the cycle
     */
    public Cycle(int id, int user_id, double total_budget, LocalDate start_date, LocalDate end_date) {
        this.id = id;
        this.user_id = user_id;
        this.total_budget = total_budget;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    /**
     * Constructs a new {@code Cycle} without an ID, typically used before persisting to the database.
     *
     * @param user_id      the ID of the user this cycle belongs to
     * @param total_budget the total budget for the cycle
     * @param start_date   the start date of the cycle
     * @param end_date     the end date of the cycle
     */
    public Cycle(int user_id, double total_budget, LocalDate start_date, LocalDate end_date) {
        this.user_id = user_id;
        this.total_budget = total_budget;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    /**
     * Returns the unique identifier of this cycle.
     *
     * @return the cycle ID
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the ID of the user this cycle belongs to.
     *
     * @return the user ID
     */
    public int getUserId() {
        return user_id;
    }

    /**
     * Returns the total budget allocated for this cycle.
     *
     * @return the total budget
     */
    public double getTotalBudget() {
        return total_budget;
    }

    /**
     * Returns the start date of this cycle.
     *
     * @return the start date
     */
    public LocalDate getStartDate() {
        return start_date;
    }

    /**
     * Returns the end date of this cycle.
     *
     * @return the end date
     */
    public LocalDate getEndDate() {
        return end_date;
    }
}