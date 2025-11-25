package com.gardening.platform.model;

/**
 * Abstract class representing a generic User in the system.
 * This demonstrates OOP Inheritance and Abstraction.
 */
public abstract class User {
    protected int id;
    protected String name;
    protected String email;
    protected String password;
    protected String role; // Role can be "ADMIN" or "GARDENER"

    // Constructor for creating a user object from database data (with ID)
    public User(int id, String name, String email, String password, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Constructor for creating a new user object (without ID, ID is auto-generated)
    public User(String name, String email, String password, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Getters and Setters (Encapsulation)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    // Abstract method to demonstrate polymorphism - subclasses must implement this
    public abstract void displayDashboard();
}
