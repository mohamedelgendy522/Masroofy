package com.example.masroofy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

class CycleDAO {

    private DataBaseManager db;

    public CycleDAO(DataBaseManager db) {
        this.db = db;
    }

    // بتنشئ الـ cycle لأول مرة لليوزر
    // not complete
    public void setupCycle(Cycle c) {
        String sql = "INSERT INTO cycles(user_id, total_budget, start_date, end_date) VALUES(?, ?, ?, ?)";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, c.getUserId());
            stmt.setDouble(2, c.getTotalBudget());
            stmt.setString(3, c.getStartDate().toString());
            stmt.setString(4, c.getEndDate().toString());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // بتجيب الـ cycle الخاصة بيوزر معين
    // not complete
    public Cycle getCycleByUser(int userId) {
        String sql = "SELECT id, user_id, total_budget, start_date, end_date FROM cycles WHERE user_id = ? ORDER BY id DESC LIMIT 1";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    double totalBudget = rs.getDouble("total_budget");
                    LocalDate start = LocalDate.parse(rs.getString("start_date"));
                    LocalDate end = LocalDate.parse(rs.getString("end_date"));
                    return new Cycle(id, userId, totalBudget, start, end);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // بتجيب الـ cycle بالـ ID بتاعها
    // not complete
    public Cycle getCycleById(int id) {
        String sql = "SELECT id, user_id, total_budget, start_date, end_date FROM cycles WHERE id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int userId = rs.getInt("user_id");
                    double totalBudget = rs.getDouble("total_budget");
                    LocalDate start = LocalDate.parse(rs.getString("start_date"));
                    LocalDate end = LocalDate.parse(rs.getString("end_date"));
                    return new Cycle(id, userId, totalBudget, start, end);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // بتحدث بيانات الـ cycle
    // not complete
    public boolean updateCycle(Cycle c) {
        String sql = "UPDATE cycles SET total_budget = ?, start_date = ?, end_date = ? WHERE id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, c.getTotalBudget());
            stmt.setString(2, c.getStartDate().toString());
            stmt.setString(3, c.getEndDate().toString());
            stmt.setInt(4, c.getId());
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // بتعمل reset للـ cycle — بتمسح الـ expenses والـ categories
    // وبترجع الـ budget والتواريخ لـ default
    // not complete
    public void resetCycle(int userId) {
        Cycle cycle = getCycleByUser(userId);
        if (cycle == null) {
            return;
        }
        ExpenseDAO expenseDAO = new ExpenseDAO(db);
        CategoryDAO categoryDAO = new CategoryDAO(db);
        expenseDAO.deleteAllExpenses(cycle.getId());
        categoryDAO.deleteAllCategories(cycle.getId());
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(30);
        Cycle reset = new Cycle(cycle.getId(), userId, 0.0, start, end);
        updateCycle(reset);
    }

    // بتضيف مبلغ للـ budget بتاع الـ cycle
    // not complete
    public void addToBudget(int cycleId, double amount) {
        String sql = "UPDATE cycles SET total_budget = total_budget + ? WHERE id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, amount);
            stmt.setInt(2, cycleId);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
