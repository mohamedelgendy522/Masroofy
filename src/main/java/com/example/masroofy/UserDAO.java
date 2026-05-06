package com.example.masroofy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;

/**
 * Data Access Object for User operations.
 * Handles database interactions related to user management.
 */
class UserDAO {

    private DataBaseManager db;

    /**
     * Constructs a UserDAO with the specified DataBaseManager.
     *
     * @param db The database manager used for connecting to the database.
     */
    public UserDAO(DataBaseManager db) {
        this.db = db;
    }

    /**
     * Creates a new user in the database.
     *
     * @param name The username of the new user.
     * @return The generated user ID, or -1 if the operation fails.
     */
    public int createUser(String name) {

        String sql = "INSERT INTO users(username) VALUES(?)";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, name);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * Retrieves a user by their unique ID.
     *
     * @param id The user ID.
     * @return A User object if found, otherwise null.
     */
    public User getUserById(int id) {

        String sql = "SELECT username FROM users WHERE id = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    String name = rs.getString("username");

                    return new User(id, name);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Deletes a user from the database.
     *
     * @param id The ID of the user to delete.
     * @return True if the user was successfully deleted, false otherwise.
     */
    public boolean deleteUser(int id) {
        String del ="DELETE FROM users WHERE id = ?";
        var index = id;
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(del)) {

            stmt.setInt(1, index);
            int rows = stmt.executeUpdate();

            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}