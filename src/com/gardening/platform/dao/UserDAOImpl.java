package com.gardening.platform.dao;

import com.gardening.platform.model.Admin;
import com.gardening.platform.model.Gardener;
import com.gardening.platform.model.User;
import com.gardening.platform.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the UserDAO interface.
 * Handles all database operations related to Users using JDBC.
 */
public class UserDAOImpl implements UserDAO {

    @Override
    public void addUser(User user) {
        // Using PreparedStatement to prevent SQL Injection
        String sql = "INSERT INTO users(name, email, password, role) VALUES(?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getRole());
            pstmt.executeUpdate();
            System.out.println("User added successfully: " + user.getEmail());
        } catch (SQLException e) {
            System.err.println("Error adding user: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapRowToUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>(); // Using ArrayList (Collection framework)
        String sql = "SELECT * FROM users";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                users.add(mapRowToUser(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public void updateUser(User user) {
        String sql = "UPDATE users SET name = ?, password = ?, role = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getRole());
            pstmt.setInt(4, user.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteUser(int id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBConnection.getConnection();
            // Transaction Management: Start transaction manually
            conn.setAutoCommit(false);

            // 1. Delete related Tips first (Foreign Key constraint)
            String deleteTips = "DELETE FROM tips WHERE user_id = ?";
            pstmt = conn.prepareStatement(deleteTips);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            pstmt.close();

            // 2. Delete related Projects
            String deleteProjects = "DELETE FROM projects WHERE user_id = ?";
            pstmt = conn.prepareStatement(deleteProjects);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            pstmt.close();

            // 3. Delete related Discussions
            String deleteDiscussions = "DELETE FROM discussions WHERE user_id = ?";
            pstmt = conn.prepareStatement(deleteDiscussions);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            pstmt.close();

            // 4. Finally, Delete the User
            String deleteUser = "DELETE FROM users WHERE id = ?";
            pstmt = conn.prepareStatement(deleteUser);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();

            // Commit transaction if all steps succeed
            conn.commit();
            System.out.println("Transaction Committed: User and related data deleted.");

        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    System.out.println("Transaction Rolled Back due to error.");
                    conn.rollback(); // Rollback changes if any error occurs
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            // Clean up resources
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.setAutoCommit(true); // Reset to default
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Helper method to map ResultSet row to User object
    private User mapRowToUser(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String email = rs.getString("email");
        String password = rs.getString("password");
        String role = rs.getString("role");

        // Polymorphism: Return specific subclass based on role
        if ("ADMIN".equalsIgnoreCase(role)) {
            return new Admin(id, name, email, password);
        } else {
            return new Gardener(id, name, email, password);
        }
    }
}
