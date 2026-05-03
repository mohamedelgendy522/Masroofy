package com.example.masroofy;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

class CategoryDAO {

    private DataBaseManager db;

    public CategoryDAO(DataBaseManager db) {
        this.db = db;
    }

    // بتضيف category جديدة للـ cycle وبترجع الـ ID
    // not complete
    public int addCategory(int cycleId, String name) {
        String sql = "INSERT INTO categories(cycle_id, name) VALUES(?, ?)";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, cycleId);
            stmt.setString(2, name);

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

    // بتجيب category بالـ ID
    // not complete
    public Category getCategoryById(int id) {
        String sql = "SELECT id, cycle_id, name FROM categories WHERE id = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int cycleId = rs.getInt("cycle_id");
                    String name = rs.getString("name");
                    return new Category(id, name, cycleId);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // بتجيب كل الـ categories بتاعة cycle معينة
    // not complete
    public List<Category> getAllCategories(int cycleId) {
        String sql = "SELECT id, cycle_id, name FROM categories WHERE cycle_id = ? ORDER BY name";

        List<Category> list = new ArrayList<>();

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cycleId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    list.add(new Category(id, name, cycleId));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // بتحذف category بالـ ID
    // not complete
    public boolean deleteCategory(int id) {
        String sql = "DELETE FROM categories WHERE id = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();

            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // بتحذف كل الـ categories بتاعة cycle — بتتكلم لما resetCycle يتعمل
    // not complete
    public boolean deleteAllCategories(int cycleId) {
        String sql = "DELETE FROM categories WHERE cycle_id = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cycleId);
            int rows = stmt.executeUpdate();

            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // بتتحقق إن الـ category مش موجودة قبل ما تضيفها
    // not complete
    public boolean categoryExists(int cycleId, String name) {
        String sql = "SELECT 1 FROM categories WHERE cycle_id = ? AND name = ? LIMIT 1";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cycleId);
            stmt.setString(2, name);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
