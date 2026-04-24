package com.example.masroofy;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public static Connection connect() {
        try {
            String url = "jdbc:sqlite:identifier.sqlite";
            Connection conn = DriverManager.getConnection(url);
            System.out.println("Connected 🔥");
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}