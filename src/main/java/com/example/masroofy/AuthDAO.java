package com.example.masroofy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

class AuthDAO {

    private DataBaseManager db;

    public AuthDAO(DataBaseManager db) {
        this.db = db;
    }

    // بتحفظ الـ pin hash في DB للـ user
    public boolean savePin(int userId, String pinHash) {
        String sql = "INSERT OR REPLACE INTO auth(user_id, pin_hash) VALUES(?, ?)";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setString(2, pinHash);

            int rows = stmt.executeUpdate();

            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // بتجيب الـ من DB عشان تتحقق منه
    public String getPin(int userId) {
        String sql = "SELECT pin_hash FROM auth WHERE user_id = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("pin_hash");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // بتحدث الـ pin hash لما اليوزر يغير الـ PIN
    public boolean updatePin(int userId, String newPinHash) {
        String sql = "UPDATE auth SET pin_hash = ? WHERE user_id = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newPinHash);
            stmt.setInt(2, userId);

            int rows = stmt.executeUpdate();

            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
