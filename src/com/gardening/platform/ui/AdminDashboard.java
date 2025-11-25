package com.gardening.platform.ui;

import com.gardening.platform.model.User;
import com.gardening.platform.service.CommunityService;
import com.gardening.platform.service.UserService;

import javax.swing.*;
import java.awt.*;

public class AdminDashboard extends JFrame {
    private User adminUser;
    private UserService userService;
    private CommunityService communityService;

    public AdminDashboard(User user) {
        this.adminUser = user;
        this.userService = new UserService();
        this.communityService = new CommunityService();

        setTitle("Admin Dashboard - " + user.getName());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("User Management", createUserManagementPanel());
        tabbedPane.addTab("Content Moderation", createContentModerationPanel());
        tabbedPane.addTab("System Settings", createSystemSettingsPanel());

        add(tabbedPane);
    }

    private JPanel createUserManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JTextArea userListArea = new JTextArea();
        userListArea.setEditable(false);
        panel.add(new JScrollPane(userListArea), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton refreshButton = new JButton("Refresh List");
        JButton deleteButton = new JButton("Delete User by ID");

        refreshButton.addActionListener(e -> refreshUserList(userListArea));
        
        deleteButton.addActionListener(e -> {
            String idStr = JOptionPane.showInputDialog(this, "Enter User ID to delete:");
            if (idStr != null && !idStr.isEmpty()) {
                try {
                    int id = Integer.parseInt(idStr);
                    userService.deleteUser(id);
                    JOptionPane.showMessageDialog(this, "User deleted.");
                    refreshUserList(userListArea);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid ID.");
                }
            }
        });

        buttonPanel.add(refreshButton);
        buttonPanel.add(deleteButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Initial load
        refreshUserList(userListArea);

        return panel;
    }

    private void refreshUserList(JTextArea area) {
        java.util.List<User> users = userService.getAllUsers();
        StringBuilder sb = new StringBuilder();
        for (User u : users) {
            sb.append("ID: ").append(u.getId())
              .append(" | Name: ").append(u.getName())
              .append(" | Email: ").append(u.getEmail())
              .append(" | Role: ").append(u.getRole())
              .append("\n-----------------------------------\n");
        }
        area.setText(sb.toString());
    }

    private JPanel createContentModerationPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea contentArea = new JTextArea();
        contentArea.setEditable(false);
        panel.add(new JScrollPane(contentArea), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton refreshButton = new JButton("Load Content");
        JButton deleteTipButton = new JButton("Delete Tip by ID");

        refreshButton.addActionListener(e -> {
            java.util.List<com.gardening.platform.model.Tip> tips = communityService.getAllTips();
            StringBuilder sb = new StringBuilder("--- All Tips ---\n");
            for (com.gardening.platform.model.Tip t : tips) {
                sb.append("ID: ").append(t.getId())
                  .append(" | Title: ").append(t.getTitle())
                  .append(" | Desc: ").append(t.getDescription())
                  .append("\n");
            }
            contentArea.setText(sb.toString());
        });

        deleteTipButton.addActionListener(e -> {
            String idStr = JOptionPane.showInputDialog(this, "Enter Tip ID to delete (Moderate):");
            if (idStr != null && !idStr.isEmpty()) {
                try {
                    int id = Integer.parseInt(idStr);
                    communityService.deleteTip(id);
                    JOptionPane.showMessageDialog(this, "Content removed.");
                    refreshButton.doClick();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid ID.");
                }
            }
        });

        buttonPanel.add(refreshButton);
        buttonPanel.add(deleteTipButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    private JPanel createSystemSettingsPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        
        panel.add(new JLabel("System Name:"));
        JTextField sysName = new JTextField("Gardening Community Platform");
        panel.add(sysName);
        
        panel.add(new JLabel("Maintenance Mode:"));
        JCheckBox maintenance = new JCheckBox("Enable");
        panel.add(maintenance);
        
        JButton saveBtn = new JButton("Save Settings");
        saveBtn.addActionListener(e -> {
            // In a real app, save to DB 'settings' table
            JOptionPane.showMessageDialog(this, "Settings Saved Successfully!");
        });
        panel.add(saveBtn);

        return panel;
    }
}
