package com.example.masroofy;
import java.sql.*;

class AuthDAO {

    private DataBaseManager db;

    public AuthDAO(DataBaseManager db) {
        this.db = db;
    }

    // بتحفظ الـ pin hash في DB للـ user
    // completed
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

    // بتجيب الـ من DB عشان تتحقق منه
    // completed
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

    // بتحدث الـ pin hash لما اليوزر يغير الـ PIN
    // completed
    public boolean updatePin(int userId, String newPinHash) {

        try (
                Connection conn = db.getConnection();
                PreparedStatement stmt = conn.prepareStatement(
                        "UPDATE auth SET pin_hash = ? WHERE user_id = ?"
                )
                ){
            stmt.setString(1,newPinHash);
            stmt.setInt(2,userId);

            if(stmt.executeUpdate() == 1 )
                return true ;

        } catch (SQLException e){
            throw new RuntimeException(e);
        }

        return false;
    }
}
