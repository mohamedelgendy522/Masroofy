package com.example.masroofy;

/**
 * Represents a spending category associated with a budget cycle.
 * Each category belongs to a specific cycle and is used to classify expenses.
 */
public class Category {

    /** The unique identifier of this category. */
    private int id;

    /** The display name of this category (e.g., "Food", "Transport"). */
    private String name;

    /** The ID of the cycle this category belongs to. */
    private int cycleId;

    /**
     * Constructs a {@code Category} with the specified ID, name, and cycle association.
     *
     * @param id      the unique identifier of the category
     * @param name    the display name of the category
     * @param cycleId the ID of the cycle this category belongs to
     */
    public Category(int id, String name, int cycleId) {
        this.id = id;
        this.name = name;
        this.cycleId = cycleId;
    }

    /**
     * Returns the unique identifier of this category.
     *
     * @return the category ID
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the display name of this category.
     *
     * @return the category name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the ID of the budget cycle this category belongs to.
     *
     * @return the cycle ID
     */
    public int getCycleId() {
        return cycleId;
    }
}