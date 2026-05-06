package com.example.masroofy;

/**
 * Represents a user in the system.
 */
public class User {
    private int id;
    private String username;

    /**
     * Constructs a new User.
     *
     * @param id       The unique identifier for the user.
     * @param username The name of the user.
     */
    public User(int id, String username) {
        this.id = id;
        this.username = username;
    }

    /**
     * Gets the user's ID.
     *
     * @return The user ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the user's username.
     *
     * @return The username.
     */
    public String getUsername() {
        return username;
    }
}