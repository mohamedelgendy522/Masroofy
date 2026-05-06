package com.example.masroofy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a single financial transaction — either an expense or an income deposit —
 * associated with a budget cycle and a spending category.
 */
public class Expense {

    /** The type of transaction: {@code "EXPENSE"}, {@code "INCOME"}, or {@code "DEPOSIT"}. */
    private String type;

    /** The unique identifier of this expense record. */
    private int id;

    /** The ID of the cycle this expense belongs to. */
    private int cycleid;

    /** The monetary amount of this transaction. */
    private double amount;

    /** The ID of the category this expense is classified under. */
    private int categoryid;

    /** The date and time when this transaction occurred. */
    private LocalDateTime date;

    /**
     * Constructs a fully populated {@code Expense}, typically used when retrieving records from the database.
     *
     * @param id         the unique identifier of the expense
     * @param amount     the monetary amount
     * @param type       the transaction type ({@code "EXPENSE"}, {@code "INCOME"}, or {@code "DEPOSIT"})
     * @param categoryid the ID of the associated category
     * @param date       the date and time of the transaction
     * @param cycleid    the ID of the cycle this expense belongs to
     */
    public Expense(int id, double amount, String type, int categoryid, LocalDateTime date, int cycleid) {
        this.id = id;
        this.cycleid = cycleid;
        this.amount = amount;
        this.categoryid = categoryid;
        this.type = type;
        this.date = date;
    }

    /**
     * Constructs a new {@code Expense} without an ID, typically used when adding a new record.
     *
     * @param amount     the monetary amount
     * @param type       the transaction type ({@code "EXPENSE"}, {@code "INCOME"}, or {@code "DEPOSIT"})
     * @param categoryid the ID of the associated category
     * @param date       the date and time of the transaction
     * @param cycleid    the ID of the cycle this expense belongs to
     */
    public Expense(double amount, String type, int categoryid, LocalDateTime date, int cycleid) {
        this.cycleid = cycleid;
        this.amount = amount;
        this.categoryid = categoryid;
        this.type = type;
        this.date = date;
    }

    /**
     * Constructs an {@code Expense} with only the fields required for an update operation.
     *
     * @param id         the unique identifier of the expense to update
     * @param amount     the new monetary amount
     * @param categoryid the new category ID
     */
    public Expense(int id, double amount, int categoryid) {
        this.id = id;
        this.amount = amount;
        this.categoryid = categoryid;
    }

    /**
     * Calculates the total amount across a list of expenses.
     *
     * @param e the list of {@link Expense} objects to sum
     * @return the total combined amount of all expenses in the list
     */
    public double getAllAmount(List<Expense> e) {
        double total = 0;
        for (int i = 0; i < e.size(); i++) {
            total += e.get(i).getAmount();
        }
        return total;
    }

    /**
     * Returns the unique identifier of this expense.
     *
     * @return the expense ID
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the monetary amount of this transaction.
     *
     * @return the amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Returns the date and time when this transaction occurred.
     *
     * @return the transaction {@link LocalDateTime}
     */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * Returns the ID of the category this expense is classified under.
     *
     * @return the category ID
     */
    public int getCategoryid() {
        return categoryid;
    }

    /**
     * Returns the type of this transaction.
     *
     * @return the type string ({@code "EXPENSE"}, {@code "INCOME"}, or {@code "DEPOSIT"})
     */
    public String getType() {
        return type;
    }

    /**
     * Returns the ID of the cycle this expense belongs to.
     *
     * @return the cycle ID
     */
    public int getCycleid() {
        return cycleid;
    }
}