package com.gardening.platform.model;

public class Admin extends User {

    public Admin(int id, String name, String email, String password) {
        super(id, name, email, password, "ADMIN");
    }

    public Admin(String name, String email, String password) {
        super(name, email, password, "ADMIN");
    }

    @Override
    public void displayDashboard() {
        System.out.println("Displaying Admin Dashboard: User Management, Content Moderation, System Settings.");
    }
}
