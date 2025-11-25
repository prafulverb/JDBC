package com.gardening.platform.util;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

public class DatabaseSetup {

    public static void initializeDatabase() {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // 1. Users Table
            String createUsers = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT NOT NULL, " +
                    "email TEXT UNIQUE NOT NULL, " +
                    "password TEXT NOT NULL, " +
                    "role TEXT NOT NULL)"; // 'ADMIN' or 'GARDENER'
            stmt.execute(createUsers);

            // 2. Tips Table
            String createTips = "CREATE TABLE IF NOT EXISTS tips (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "user_id INTEGER, " +
                    "title TEXT NOT NULL, " +
                    "description TEXT NOT NULL, " +
                    "photo_path TEXT, " +
                    "FOREIGN KEY(user_id) REFERENCES users(id))";
            stmt.execute(createTips);

            // 3. Discussions Table
            String createDiscussions = "CREATE TABLE IF NOT EXISTS discussions (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "user_id INTEGER, " +
                    "topic TEXT NOT NULL, " +
                    "comment TEXT NOT NULL, " +
                    "FOREIGN KEY(user_id) REFERENCES users(id))";
            stmt.execute(createDiscussions);

            // 4. Projects Table
            String createProjects = "CREATE TABLE IF NOT EXISTS projects (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "user_id INTEGER, " +
                    "description TEXT NOT NULL, " +
                    "progress TEXT, " +
                    "FOREIGN KEY(user_id) REFERENCES users(id))";
            stmt.execute(createProjects);
            
            // 5. System Settings Table (for Admin)
            String createSettings = "CREATE TABLE IF NOT EXISTS settings (" +
                    "key TEXT PRIMARY KEY, " +
                    "value TEXT)";
            stmt.execute(createSettings);

            // Insert a default admin if not exists
            // Note: In a real app, passwords should be hashed.
            String checkAdmin = "SELECT count(*) FROM users WHERE email = 'admin@garden.com'";
            if (!stmt.executeQuery(checkAdmin).next()) {
                 // Simple check, for full implementation we'd use a proper check
            }
            
            // We will handle default data insertion in the main app or a separate method to avoid complexity here.
            System.out.println("Database initialized successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
