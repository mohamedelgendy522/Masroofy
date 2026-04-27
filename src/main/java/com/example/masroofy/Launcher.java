package com.example.masroofy;

import javafx.application.Application;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;

public class Launcher {
    public static void main(String[] args) {
            String del ="DELETE FROM USERS WHERE ID = ?";
            var id = 3;

            try (Connection conn = DBConnection.connect();
                 PreparedStatement stmt = conn.prepareStatement(del);) {
                stmt.setInt(1,id);
                stmt.executeUpdate();

            } catch (Exception e) {
                e.printStackTrace();
            }

       // Application.launch(HelloApplication.class, args);
    }
}
