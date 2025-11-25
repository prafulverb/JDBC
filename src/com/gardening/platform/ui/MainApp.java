package com.gardening.platform.ui;

import com.gardening.platform.util.DatabaseSetup;
import javax.swing.SwingUtilities;

/**
 * Main entry point for the Gardening Community Platform application.
 * This class initializes the database and launches the Login screen.
 */
public class MainApp {
    public static void main(String[] args) {
        System.out.println("Starting Application...");

        // Step 1: Initialize the Database (Create tables if they don't exist)
        DatabaseSetup.initializeDatabase();

        // Step 2: Launch the GUI using SwingUtilities to ensure thread safety
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
