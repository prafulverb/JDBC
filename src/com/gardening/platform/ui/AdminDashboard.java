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
        tabbedPane.addTab("Statistics", createAdminStatisticsPanel());

        add(tabbedPane);
    }

    private JPanel createUserManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JTextArea userListArea = new JTextArea();
        userListArea.setEditable(false);
        userListArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        panel.add(new JScrollPane(userListArea), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton refreshButton = new JButton("Refresh List");
        JButton deleteButton = new JButton("Delete User by ID");
        JButton exportButton = new JButton("Export Users");

        refreshButton.addActionListener(e -> refreshUserList(userListArea));
        
        deleteButton.addActionListener(e -> {
            String idStr = JOptionPane.showInputDialog(this, "Enter User ID to delete:");
            if (idStr != null && !idStr.trim().isEmpty()) {
                try {
                    int id = Integer.parseInt(idStr.trim());
                    int confirm = JOptionPane.showConfirmDialog(this, 
                        "Are you sure you want to delete user ID " + id + "?\nThis will also delete all their tips, projects, and discussions.", 
                        "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    
                    if (confirm == JOptionPane.YES_OPTION) {
                        userService.deleteUser(id);
                        JOptionPane.showMessageDialog(this, "User deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        refreshUserList(userListArea);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid ID format. Please enter a number.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error deleting user: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        exportButton.addActionListener(e -> exportUsersToFile(userListArea));

        buttonPanel.add(refreshButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(exportButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Initial load
        refreshUserList(userListArea);

        return panel;
    }

    private void refreshUserList(JTextArea area) {
        try {
            java.util.List<User> users = userService.getAllUsers();
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%-5s | %-20s | %-30s | %-10s%n", "ID", "Name", "Email", "Role"));
            // Java 8 compatible way to repeat characters
            for (int i = 0; i < 75; i++) sb.append("=");
            sb.append("\n");
            for (User u : users) {
                sb.append(String.format("%-5d | %-20s | %-30s | %-10s%n", 
                    u.getId(), 
                    u.getName().length() > 20 ? u.getName().substring(0, 17) + "..." : u.getName(),
                    u.getEmail().length() > 30 ? u.getEmail().substring(0, 27) + "..." : u.getEmail(),
                    u.getRole()));
            }
            area.setText(sb.toString());
            System.out.println("[ADMIN] Loaded " + users.size() + " users");
        } catch (Exception e) {
            area.setText("Error loading users: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Failed to load users: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // INNOVATIVE FEATURE: Export user list
    private void exportUsersToFile(JTextArea area) {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save User List");
            fileChooser.setSelectedFile(new java.io.File("user_list.txt"));
            
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                java.io.File file = fileChooser.getSelectedFile();
                java.nio.file.Files.write(file.toPath(), area.getText().getBytes());
                JOptionPane.showMessageDialog(this, "Exported to: " + file.getAbsolutePath(), "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Export failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
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
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        panel.add(new JLabel("System Name:"));
        JTextField sysName = new JTextField("Gardening Community Platform");
        panel.add(sysName);
        
        panel.add(new JLabel("Maintenance Mode:"));
        JCheckBox maintenance = new JCheckBox("Enable");
        panel.add(maintenance);
        
        panel.add(new JLabel("Max Upload Size (MB):"));
        JTextField maxUpload = new JTextField("10");
        panel.add(maxUpload);
        
        JButton saveBtn = new JButton("Save Settings");
        saveBtn.addActionListener(e -> {
            try {
                // Validate max upload
                int maxSize = Integer.parseInt(maxUpload.getText());
                if (maxSize < 1 || maxSize > 100) {
                    throw new IllegalArgumentException("Max upload size must be between 1-100 MB");
                }
                
                // In a real app, save to DB 'settings' table
                System.out.println("[ADMIN] Settings Updated:");
                System.out.println("  - System Name: " + sysName.getText());
                System.out.println("  - Maintenance Mode: " + maintenance.isSelected());
                System.out.println("  - Max Upload Size: " + maxSize + "MB");
                
                JOptionPane.showMessageDialog(this, "Settings Saved Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid upload size. Please enter a number.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(saveBtn);

        return panel;
    }
    
    // INNOVATIVE FEATURE: Admin Statistics Dashboard
    private JPanel createAdminStatisticsPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 15, 15));
        panel.setBorder(BorderFactory.createTitledBorder("Platform Statistics"));
        
        JLabel totalUsersLabel = new JLabel("Total Users:");
        JLabel totalUsersValue = new JLabel("Loading...");
        JLabel totalTipsLabel = new JLabel("Total Tips:");
        JLabel totalTipsValue = new JLabel("Loading...");
        JLabel totalProjectsLabel = new JLabel("Total Projects:");
        JLabel totalProjectsValue = new JLabel("Loading...");
        JLabel totalDiscussionsLabel = new JLabel("Total Discussions:");
        JLabel totalDiscussionsValue = new JLabel("Loading...");
        JLabel adminCountLabel = new JLabel("Admin Count:");
        JLabel adminCountValue = new JLabel("Loading...");
        
        panel.add(totalUsersLabel);
        panel.add(totalUsersValue);
        panel.add(totalTipsLabel);
        panel.add(totalTipsValue);
        panel.add(totalProjectsLabel);
        panel.add(totalProjectsValue);
        panel.add(totalDiscussionsLabel);
        panel.add(totalDiscussionsValue);
        panel.add(adminCountLabel);
        panel.add(adminCountValue);
        
        // Load statistics in background thread
        new Thread(() -> {
            try {
                int userCount = userService.getAllUsers().size();
                int tipCount = communityService.getAllTips().size();
                int discussionCount = communityService.getAllDiscussions().size();
                int adminCount = (int) userService.getAllUsers().stream()
                    .filter(u -> "ADMIN".equalsIgnoreCase(u.getRole()))
                    .count();
                
                // Note: We'd need to add a getAllProjects method to ProjectService
                // For now, we'll show a placeholder
                int projectCount = 0;
                
                SwingUtilities.invokeLater(() -> {
                    totalUsersValue.setText(String.valueOf(userCount));
                    totalTipsValue.setText(String.valueOf(tipCount));
                    totalProjectsValue.setText(String.valueOf(projectCount));
                    totalDiscussionsValue.setText(String.valueOf(discussionCount));
                    adminCountValue.setText(String.valueOf(adminCount));
                    
                    System.out.println("[ADMIN STATS] Users: " + userCount + ", Tips: " + tipCount + 
                        ", Discussions: " + discussionCount + ", Admins: " + adminCount);
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    totalUsersValue.setText("Error");
                    JOptionPane.showMessageDialog(AdminDashboard.this, 
                        "Failed to load statistics: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
        
        return panel;
    }
}
