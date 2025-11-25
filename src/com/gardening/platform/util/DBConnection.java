package com.gardening.platform.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // Using SQLite for simplicity. Change the URL for MySQL/PostgreSQL if needed.
    // e.g., jdbc:mysql://localhost:3306/gardening_db
    private static final String URL = "jdbc:sqlite:gardening.db";
    
    public static Connection getConnection() throws SQLException {
        try {
            // Ensure driver is loaded
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            // Fallback or just proceed if driver is automatically loaded
            // System.out.println("SQLite JDBC Driver not found.");
        }
        return DriverManager.getConnection(URL);
    }
}
