package com.example.masroofy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class DataBaseManager {
    public static Connection getConnection() {
        try {
            String url = "jdbc:sqlite:identifier.sqlite";
            return DriverManager.getConnection(url);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public void initDB() {
        String createUsers = """
        CREATE TABLE IF NOT EXISTS users (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            username TEXT NOT NULL
        );""";

        String createAuth = """
        CREATE TABLE IF NOT EXISTS auth (
                user_id INTEGER PRIMARY KEY,
                pin_hash TEXT NOT NULL,
                FOREIGN KEY (user_id) REFERENCES users(id)
        );""";

        String createCycles = """
        CREATE TABLE IF NOT EXISTS cycles (
             id INTEGER PRIMARY KEY AUTOINCREMENT,
             user_id INTEGER NOT NULL,
             total_budget REAL NOT NULL,
             start_date DATE NOT NULL,
             end_date DATE NOT NULL,
             
             FOREIGN KEY (user_id) REFERENCES users(id)
        );""";

        String createExpenses = """
        CREATE TABLE IF NOT EXISTS expenses (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            cycle_id INTEGER NOT NULL,
            category_id INTEGER NOT NULL,
            amount REAL NOT NULL,
            type TEXT NOT NULL, -- EXPENSE / INCOME
            date DATETIME NOT NULL,

             FOREIGN KEY (cycle_id) REFERENCES cycles(id),
             FOREIGN KEY (category_id) REFERENCES categories(id)
        );""";

        String createCategories = """
        CREATE TABLE IF NOT EXISTS categories (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            cycle_id INTEGER NOT NULL,
            name TEXT NOT NULL,
            
            FOREIGN KEY (cycle_id) REFERENCES cycles(id)
        );""";

        String unique= """
                CREATE UNIQUE INDEX IF NOT EXISTS idx_category_unique ON categories(cycle_id, name);
        """;

        try (Connection conn = getConnection()) {
            if (conn == null) {
                throw new SQLException("Failed to open SQLite connection");
            }

            try (Statement stmt = conn.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON");
                stmt.execute(createUsers);
                ensureUsernameColumn(stmt);
                stmt.execute(createAuth);
                stmt.execute(createCycles);
                stmt.execute(createCategories);
                stmt.execute(createExpenses);
                stmt.execute(unique);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void ensureUsernameColumn(Statement stmt) throws SQLException {
        boolean hasUsername = false;
        boolean hasName = false;

        try (ResultSet rs = stmt.executeQuery("PRAGMA table_info(users)")) {
            while (rs.next()) {
                String column = rs.getString("name");
                if ("username".equalsIgnoreCase(column)) {
                    hasUsername = true;
                } else if ("name".equalsIgnoreCase(column)) {
                    hasName = true;
                }
            }
        }

        if (!hasUsername) {
            stmt.execute("ALTER TABLE users ADD COLUMN username TEXT");
            if (hasName) {
                stmt.execute("UPDATE users SET username = name WHERE username IS NULL");
            }
        }
    }
}
