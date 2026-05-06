package com.example.masroofy;

import java.sql.*;
import java.time.LocalDate;

class CycleDAO {

    private DataBaseManager db;

    public CycleDAO(DataBaseManager db) {
        this.db = db;
    }
    // بتنشئ الـ cycle لأول مرة لليوزر
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
    // بتجيب الـ cycle الخاصة بيوزر معين
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

    // بتجيب الـ cycle بالـ ID بتاعها
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
    // بتحدث بيانات الـ cycle
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

    // بتضيف مبلغ للـ budget بتاع الـ cycle
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