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
    public int addCategory(int cycleId, String name) {
        if (categoryExists(cycleId, name)) return -1;
        String sql = "INSERT INTO categories (cycle_id, name) VALUES (?, ?)";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, cycleId);
            ps.setString(2, name);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return -1;
    }

    // بتجيب category بالـ ID
    public Category getCategoryById(int id) {
        String sql = "SELECT * FROM categories WHERE id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    // بتجيب كل الـ categories بتاعة cycle معينة
    public List<Category> getAllCategories(int cycleId) {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM categories WHERE cycle_id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cycleId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // بتحذف category بالـ ID
    public boolean deleteCategory(int id) {
        String sql = "DELETE FROM categories WHERE id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    // بتحذف كل الـ categories بتاعة cycle — بتتكلم لما resetCycle يتعمل
    public boolean deleteAllCategories(int cycleId) {
        String sql = "DELETE FROM categories WHERE cycle_id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cycleId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    // بتتحقق إن الـ category مش موجودة قبل ما تضيفها
    public boolean categoryExists(int cycleId, String name) {
        String sql = "SELECT 1 FROM categories WHERE cycle_id = ? AND name = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cycleId);
            ps.setString(2, name);
            return ps.executeQuery().next();
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    // بتحول row في الdatabase لcategory object بيبقي كل واحد ليه (id,name,cycle_id) بتاعه
    private Category mapRow(ResultSet rs) throws SQLException {
        return new Category(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("cycle_id")
        );
    }
}

