package com.example.masroofy;

import java.sql.*;

/**
 * Data Access Object (DAO) for managing PIN-based authentication records
 * in the {@code auth} database table.
 * <p>
 * Provides operations for saving, retrieving, updating, and deleting
 * hashed PIN entries associated with users.
 * </p>
 */
class AuthDAO {

    /** The database manager used to obtain connections. */
    private DataBaseManager db;

    /**
     * Constructs an {@code AuthDAO} with the given database manager.
     *
     * @param db the {@link DataBaseManager} used to obtain database connections
     */
    public AuthDAO(DataBaseManager db) {
        this.db = db;
    }

    /**
     * Saves a hashed PIN for the specified user in the {@code auth} table.
     *
     * @param userId  the ID of the user whose PIN is being saved
     * @param pinHash the hashed PIN string to store
     * @return {@code true} if the PIN was saved successfully; {@code false} otherwise
     */
    public boolean savePin(int userId, String pinHash) {
        try (
                Connection conn = db.getConnection();
                PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO auth (user_id, pin_hash) VALUES (?, ?)"
                )
        ) {
            stmt.setInt(1, userId);
            stmt.setString(2, pinHash);

            if (stmt.executeUpdate() == 1)
                return true;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    /**
     * Retrieves the stored PIN hash for a given user from the {@code auth} table.
     *
     * @param userId the ID of the user whose PIN hash is to be retrieved
     * @return the stored PIN hash string, or {@code null} if no record is found
     */
    public String getPin(int userId) {
        try (
                Connection conn = db.getConnection();
                PreparedStatement stmt = conn.prepareStatement(
                        "SELECT pin_hash FROM auth WHERE user_id = ?"
                )
        ) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next())
                return rs.getString("pin_hash");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * Updates the stored PIN hash for the specified user.
     * Used when a user changes their PIN.
     *
     * @param userId     the ID of the user whose PIN is being updated
     * @param newPinHash the new hashed PIN string to store
     * @return {@code true} if the update affected at least one row; {@code false} otherwise
     */
    public boolean updatePin(int userId, String newPinHash) {
        String sql = "UPDATE auth SET pin_hash = ? WHERE user_id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newPinHash);
            stmt.setInt(2, userId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes the authentication record for the specified user.
     *
     * @param userId the ID of the user whose auth record is to be deleted
     * @return {@code true} if the deletion affected at least one row; {@code false} otherwise
     */
    public boolean deleteAuth(int userId) {
        String sql = "DELETE FROM auth WHERE user_id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}