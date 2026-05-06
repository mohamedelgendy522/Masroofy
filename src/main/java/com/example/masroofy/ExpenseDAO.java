package com.example.masroofy;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;

class ExpenseDAO {

    private DataBaseManager db;

    public ExpenseDAO(DataBaseManager db) {
        this.db = db;
    }

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


    public boolean deleteExpense(int id) {

        String del ="DELETE FROM expenses WHERE id = ?";

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

    public List<Expense> getExpensesByDate(int cycleId, LocalDate date) {

        String sql = "SELECT * FROM expenses " +
                "WHERE cycle_id = ? AND DATE(date) = ? " +
                "ORDER BY date DESC";

        List<Expense> list = new ArrayList<>();

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cycleId);
            stmt.setString(2, date.toString()); // yyyy-MM-dd

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
