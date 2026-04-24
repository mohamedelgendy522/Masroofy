package com.example.masroofy;

import javafx.application.Application;
import java.sql.Connection;
import java.sql.Statement;

public class Launcher {
    public static void main(String[] args) {
        try {
            Connection conn = DBConnection.connect();

            Statement stmt = conn.createStatement();

            stmt.execute("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY, name TEXT)");

            System.out.println("Table created 🔥");

        } catch (Exception e) {
            e.printStackTrace();
        }

        Application.launch(HelloApplication.class, args);
    }
}
