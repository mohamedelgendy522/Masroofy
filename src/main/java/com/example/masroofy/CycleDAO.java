package com.example.masroofy;

import java.sql.*;
import java.time.LocalDate;

/**
 * Data Access Object for Cycle operations.
 * Manages database interactions for financial cycles.
 */
class CycleDAO {

    private DataBaseManager db;

    /**
     * Constructs a CycleDAO with the specified database manager.
     *
     * @param db The DataBaseManager instance.
     */
    public CycleDAO(DataBaseManager db) {
        this.db = db;
    }

    /**
     * Sets up a new cycle for a user for the first time.
     *
     * @param c The Cycle object containing setup details.
     */

    public void setupCycle(Cycle c) {

        String sql = """
        INSERT INTO cycles(user_id,total_budget,start_date,end_date)
        VALUES(?,?,?,?)
        """;

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, c.getUserId());
            stmt.setDouble(2, c.getTotalBudget());
            stmt.setString(3, c.getStartDate().toString());
            stmt.setString(4, c.getEndDate().toString());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the cycle associated with a specific user.
     *
     * @param userId The ID of the user.
     * @return The Cycle object if found, otherwise null.
     */

    public Cycle getCycleByUser(int userId) {

        String sql = "SELECT * FROM cycles WHERE user_id=?";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Cycle(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getDouble("total_budget"),
                        LocalDate.parse(rs.getString("start_date")),
                        LocalDate.parse(rs.getString("end_date"))
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Retrieves a cycle by its unique ID.
     *
     * @param id The cycle ID.
     * @return The Cycle object if found, otherwise null.
     */

    public Cycle getCycleById(int id) {

        String sql = "SELECT * FROM cycles WHERE id = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                return new Cycle(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getDouble("total_budget"),
                        LocalDate.parse(rs.getString("start_date")),
                        LocalDate.parse(rs.getString("end_date"))
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Updates an existing cycle's information.
     *
     * @param c The Cycle object containing updated information.
     * @return True if the update was successful, false otherwise.
     */

    public boolean updateCycle(Cycle c) {

        String sql = """
        UPDATE cycles
        SET total_budget=?, start_date=?, end_date=?
        WHERE id=?
        """;

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, c.getTotalBudget());
            stmt.setString(2, c.getStartDate().toString());
            stmt.setString(3, c.getEndDate().toString());
            stmt.setInt(4, c.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Resets a user's cycle by deleting all related expenses, categories, and the cycle itself.
     *
     * @param userId The ID of the user whose cycle needs to be reset.
     */
    public void resetCycle(int userId) {
        try (Connection conn = db.getConnection()) {

            try (PreparedStatement stmt1 = conn.prepareStatement(
                    "DELETE FROM expenses WHERE cycle_id=(SELECT id FROM cycles WHERE user_id=?)")) {
                stmt1.setInt(1, userId);
                stmt1.executeUpdate();
            }

            try (PreparedStatement stmt2 = conn.prepareStatement(
                    "DELETE FROM categories WHERE cycle_id=(SELECT id FROM cycles WHERE user_id=?)")) {
                stmt2.setInt(1, userId);
                stmt2.executeUpdate();
            }

            try (PreparedStatement stmt3 = conn.prepareStatement(
                    "DELETE FROM cycles WHERE user_id=?")) {
                stmt3.setInt(1, userId);
                stmt3.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes all cycles associated with a specific user.
     *
     * @param userId The ID of the user.
     * @return True if the cycles were successfully deleted, false otherwise.
     */
    public boolean deleteAllCyclesByUserId(int userId) {
        String sql = "DELETE FROM cycles WHERE user_id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Adds a specific amount to the total budget of a cycle.
     *
     * @param cycleId The ID of the cycle.
     * @param amount  The amount to be added to the budget.
     */

    public void addToBudget(int cycleId, double amount) {
        String sql = "UPDATE cycles SET total_budget = total_budget + ? WHERE id = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, amount);
            stmt.setInt(2, cycleId);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}