package com.example.masroofy;
import java.sql.*;

class AuthDAO {

    private DataBaseManager db;

    public AuthDAO(DataBaseManager db) {
        this.db = db;
    }

    public boolean savePin(int userId, String pinHash) {

        try (
                Connection conn = db.getConnection();
                PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO auth (user_id, pin_hash) VALUES (?, ?)"
                )
        ){
            stmt.setInt(1,userId);
            stmt.setString(2,pinHash);

            if (stmt.executeUpdate() == 1 )
                return true;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false ;
    }

    public String getPin(int userId) {

        try (
                Connection conn = db.getConnection();
                PreparedStatement stmt = conn.prepareStatement(
                        "SELECT pin_hash FROM auth WHERE user_id = ?"
                )
        ){

            stmt.setInt(1,userId);

            ResultSet rs =  stmt.executeQuery();

            if ( rs.next() )
                return rs.getString("pin_hash") ;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public boolean updatePin(int userId, String newPinHash) {
        String sql = "UPDATE auth SET pin_hash = ? WHERE user_id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newPinHash);
            stmt.setInt(2, userId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAuth(int userId) {
        String sql = "DELETE FROM auth WHERE user_id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}