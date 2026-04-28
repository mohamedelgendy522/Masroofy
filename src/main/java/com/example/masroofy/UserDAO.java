package com.example.masroofy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;

class UserDAO {

    private DataBaseManager db;

    public UserDAO(DataBaseManager db) {
        this.db = db;
    }

    public int createUser(String name) {

        String sql = "INSERT INTO users(name) VALUES(?)";

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


    public User getUserById(int id) {

        String sql = "SELECT name FROM users WHERE id = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    String name = rs.getString("name");

                    return new User(id, name);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


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
