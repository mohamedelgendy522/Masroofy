package com.example.masroofy;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Data Access Object (DAO) for managing spending categories in the
 * {@code categories} database table.
 * <p>
 * Provides CRUD operations for categories associated with budget cycles.
 * </p>
 */
class CategoryDAO {

    /** The database manager used to obtain connections. */
    private DataBaseManager db;

    /**
     * Constructs a {@code CategoryDAO} with the given database manager.
     *
     * @param db the {@link DataBaseManager} used to obtain database connections
     */
    public CategoryDAO(DataBaseManager db) {
        this.db = db;
    }

    /**
     * Adds a new category to the specified cycle.
     * Returns {@code -1} if a category with the same name already exists in that cycle.
     *
     * @param cycleId the ID of the cycle to which the category belongs
     * @param name    the name of the new category
     * @return the generated ID of the new category, or {@code -1} if it already exists or an error occurred
     */
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Retrieves a category by its unique ID.
     *
     * @param id the ID of the category to retrieve
     * @return the {@link Category} object if found, or {@code null} if not found
     */
    public Category getCategoryById(int id) {
        String sql = "SELECT * FROM categories WHERE id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves all categories belonging to a specific cycle.
     *
     * @param cycleId the ID of the cycle whose categories are to be fetched
     * @return a list of {@link Category} objects; an empty list if none exist
     */
    public List<Category> getAllCategories(int cycleId) {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM categories WHERE cycle_id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cycleId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Deletes a category by its unique ID.
     *
     * @param id the ID of the category to delete
     * @return {@code true} if the deletion was successful; {@code false} otherwise
     */
    public boolean deleteCategory(int id) {
        String sql = "DELETE FROM categories WHERE id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Deletes all categories associated with a specific cycle.
     * Typically invoked when a cycle is being reset.
     *
     * @param cycleId the ID of the cycle whose categories are to be deleted
     * @return {@code true} if at least one category was deleted; {@code false} otherwise
     */
    public boolean deleteAllCategories(int cycleId) {
        String sql = "DELETE FROM categories WHERE cycle_id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cycleId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes all categories belonging to all cycles of the specified user.
     * Used during full account deletion.
     *
     * @param userId the ID of the user whose categories are to be deleted
     * @return {@code true} if the operation completed successfully; {@code false} on error
     */
    public boolean deleteAllCategoriesByUserId(int userId) {
        String sql = "DELETE FROM categories WHERE cycle_id IN (SELECT id FROM cycles WHERE user_id = ?)";
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
     * Checks whether a category with the given name already exists in a specific cycle.
     *
     * @param cycleId the ID of the cycle to search within
     * @param name    the category name to check for
     * @return {@code true} if the category exists; {@code false} otherwise
     */
    public boolean categoryExists(int cycleId, String name) {
        String sql = "SELECT 1 FROM categories WHERE cycle_id = ? AND name = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cycleId);
            ps.setString(2, name);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Maps a single row from a {@link ResultSet} to a {@link Category} object.
     *
     * @param rs the {@link ResultSet} positioned at the row to map
     * @return a {@link Category} populated with data from the current row
     * @throws SQLException if a database access error occurs while reading the row
     */
    private Category mapRow(ResultSet rs) throws SQLException {
        return new Category(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("cycle_id")
        );
    }
}