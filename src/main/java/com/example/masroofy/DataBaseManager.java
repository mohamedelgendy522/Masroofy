package com.example.masroofy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Manages the SQLite database connection and schema initialization for the application.
 * <p>
 * Provides a static method to obtain a database connection and an instance method
 * to set up all required tables on first launch.
 * </p>
 */
public class DataBaseManager {

    /**
     * Opens and returns a new SQLite database connection.
     * <p>
     * The database file is located at {@code identifier.sqlite} in the working directory.
     * </p>
     *
     * @return a {@link Connection} to the SQLite database, or {@code null} if the connection fails
     */
    public static Connection getConnection() {
        try {
            String url = "jdbc:sqlite:identifier.sqlite";
            return DriverManager.getConnection(url);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Initializes the database schema by creating all required tables if they do not already exist.
     * <p>
     * Tables created:
     * <ul>
     *   <li>{@code users} — stores user account information</li>
     *   <li>{@code auth} — stores hashed PINs linked to users</li>
     *   <li>{@code cycles} — stores budget cycles linked to users</li>
     *   <li>{@code categories} — stores expense categories linked to cycles</li>
     *   <li>{@code expenses} — stores individual expense and income records</li>
     * </ul>
     * A unique index on {@code (cycle_id, name)} in the {@code categories} table
     * is also created to prevent duplicate category names within a cycle.
     * </p>
     */
    public void initDB() {
        String createUsers = """
        CREATE TABLE IF NOT EXISTS users (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            username text
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

        String unique = """
                CREATE UNIQUE INDEX idx_category_unique ON categories(cycle_id, name);
        """;

        try (Connection conn = getConnection()) {
            if (conn == null) {
                throw new SQLException("Failed to open SQLite connection");
            }

            try (Statement stmt = conn.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON");
                stmt.execute(createUsers);
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
}