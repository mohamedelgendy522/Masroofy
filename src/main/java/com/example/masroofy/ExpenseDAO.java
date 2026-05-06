package com.example.masroofy;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;

/**
 * Data Access Object (DAO) for managing expense records in the
 * {@code expenses} database table.
 * <p>
 * Provides CRUD operations as well as aggregation queries for
 * totals, weekly summaries, and category breakdowns.
 * </p>
 */
class ExpenseDAO {

    /** The database manager used to obtain connections. */
    private DataBaseManager db;

    /**
     * Constructs an {@code ExpenseDAO} with the given database manager.
     *
     * @param db the {@link DataBaseManager} used to obtain database connections
     */
    public ExpenseDAO(DataBaseManager db) {
        this.db = db;
    }

    /**
     * Inserts a new expense record into the database.
     *
     * @param e the {@link Expense} object containing transaction data to persist
     * @return the generated ID of the new expense record, or {@code -1} on failure
     */
    public int addExpense(Expense e) {
        String cre = "Insert INTO expenses(cycle_id,category_id,amount,type,date) VALUES(?,?,?,?,?)";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(cre, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, e.getCycleid());
            stmt.setInt(2, e.getCategoryid());
            stmt.setDouble(3, e.getAmount());
            stmt.setString(4, e.getType());
            stmt.setTimestamp(5, Timestamp.valueOf(e.getDate()));

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }

        } catch (Exception ee) {
            ee.printStackTrace();
        }
        return -1;
    }

    /**
     * Updates the amount and category of an existing expense record.
     *
     * @param e the {@link Expense} object containing the updated amount, category ID, and the target expense ID
     * @return {@code true} if the update affected at least one row; {@code false} otherwise
     */
    public boolean updateExpense(Expense e) {
        String upd = "UPDATE expenses SET amount = ? , category_id = ? WHERE id = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(upd)) {

            stmt.setDouble(1, e.getAmount());
            stmt.setInt(2, e.getCategoryid());
            stmt.setInt(3, e.getId());

            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (Exception ee) {
            ee.printStackTrace();
        }
        return false;
    }

    /**
     * Deletes an expense record by its unique ID.
     *
     * @param id the ID of the expense to delete
     * @return {@code true} if the deletion affected at least one row; {@code false} otherwise
     */
    public boolean deleteExpense(int id) {
        String del = "DELETE FROM expenses WHERE id = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(del)) {

            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Retrieves a single expense record by its unique ID.
     *
     * @param id the ID of the expense to retrieve
     * @return the {@link Expense} object if found, or {@code null} if not found
     */
    public Expense getExpenseById(int id) {
        String sql = "SELECT cycle_id,category_id,amount,type,date FROM expenses WHERE id = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int cycle = rs.getInt("cycle_id");
                    int category = rs.getInt("category_id");
                    double amount = rs.getDouble("amount");
                    String type = rs.getString("type");
                    LocalDateTime date = rs.getTimestamp("date").toLocalDateTime();

                    return new Expense(id, amount, type, category, date, cycle);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Retrieves all expense records for the specified cycle, ordered by date descending.
     *
     * @param cycleId the ID of the cycle whose expenses are to be retrieved
     * @return a list of {@link Expense} objects; an empty list if none exist
     */
    public List<Expense> getAllExpenses(int cycleId) {
        String sql = "SELECT * FROM expenses WHERE cycle_id = ? ORDER BY date DESC";
        List<Expense> list = new ArrayList<>();

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cycleId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    int cycle = rs.getInt("cycle_id");
                    int category = rs.getInt("category_id");
                    double amount = rs.getDouble("amount");
                    String type = rs.getString("type");
                    LocalDateTime date = rs.getTimestamp("date").toLocalDateTime();

                    list.add(new Expense(id, amount, type, category, date, cycle));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Retrieves all expense records for a specific cycle on a given date, ordered by date descending.
     *
     * @param cycleId the ID of the cycle to search within
     * @param date    the specific date to filter by
     * @return a list of {@link Expense} objects for that date; an empty list if none exist
     */
    public List<Expense> getExpensesByDate(int cycleId, LocalDate date) {
        String sql = "SELECT * FROM expenses " +
                "WHERE cycle_id = ? AND DATE(date) = ? " +
                "ORDER BY date DESC";

        List<Expense> list = new ArrayList<>();

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cycleId);
            stmt.setString(2, date.toString());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    int cycle = rs.getInt("cycle_id");
                    int category = rs.getInt("category_id");
                    double amount = rs.getDouble("amount");
                    String type = rs.getString("type");
                    LocalDateTime dt = rs.getTimestamp("date").toLocalDateTime();

                    list.add(new Expense(id, amount, type, category, dt, cycle));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Calculates the total amount of expenses within a specified date range for a cycle.
     *
     * @param cycleId the ID of the cycle to query
     * @param from    the start date of the range (inclusive)
     * @param to      the end date of the range (inclusive)
     * @return the sum of expense amounts within the range, or {@code 0.0} if none found
     */
    public double getWeeklyTotal(int cycleId, LocalDate from, LocalDate to) {
        String sql = "SELECT SUM(amount) AS total FROM expenses " +
                "WHERE cycle_id = ? AND type = 'EXPENSE' " +
                "AND DATE(date) BETWEEN ? AND ?";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cycleId);
            stmt.setString(2, from.toString());
            stmt.setString(3, to.toString());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    /**
     * Deletes all expense records associated with a specific cycle.
     *
     * @param cycleId the ID of the cycle whose expenses are to be deleted
     * @return {@code true} if at least one expense was deleted; {@code false} otherwise
     */
    public boolean deleteAllExpenses(int cycleId) {
        String sql = "DELETE FROM expenses WHERE cycle_id = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cycleId);
            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Deletes all expense records belonging to all cycles of a specified user.
     * Used during full account deletion.
     *
     * @param userId the ID of the user whose expenses are to be deleted
     * @return {@code true} if the operation completed successfully; {@code false} on error
     */
    public boolean deleteAllExpensesByUserId(int userId) {
        String sql = "DELETE FROM expenses WHERE cycle_id IN (SELECT id FROM cycles WHERE user_id = ?)";
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
     * Calculates the total amount spent (type = {@code "EXPENSE"}) within a specific cycle.
     *
     * @param cycleId the ID of the cycle to total
     * @return the sum of all expense amounts in the cycle, or {@code 0.0} if none found
     */
    public double getTotalByCycle(int cycleId) {
        String sql = "SELECT SUM(amount) AS total FROM expenses WHERE cycle_id = ? AND type = 'EXPENSE'";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cycleId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0.0;
    }

    /**
     * Returns the total amount spent per category for the given cycle.
     * Only transactions of type {@code "EXPENSE"} are included.
     *
     * @param cycleId the ID of the cycle to query
     * @return a map of category names to their total expense amounts;
     *         an empty map if no data exists
     */
    public Map<String, Double> getCategoryTotals(int cycleId) {
        String sql = "SELECT c.name, SUM(e.amount) AS total " +
                "FROM expenses e " +
                "JOIN categories c ON e.category_id = c.id " +
                "WHERE e.cycle_id = ? AND e.type = 'EXPENSE' " +
                "GROUP BY c.name";

        Map<String, Double> map = new HashMap<>();

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cycleId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("name");
                    double total = rs.getDouble("total");
                    map.put(name, total);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return map;
    }
}