package com.gardening.platform.ui;

import com.gardening.platform.model.User;
import com.gardening.platform.service.CommunityService;
import com.gardening.platform.service.ProjectService;

import javax.swing.*;
import java.awt.*;

public class GardenerDashboard extends JFrame {
    private User gardenerUser;
    private CommunityService communityService;
    private ProjectService projectService;
    
    // Use a thread pool for loading images to prevent OutOfMemory errors
    // and to limit the number of concurrent threads.
    private java.util.concurrent.ExecutorService imageLoader;

    public GardenerDashboard(User user) {
        this.gardenerUser = user;
        this.communityService = new CommunityService();
        this.projectService = new ProjectService();
        this.imageLoader = java.util.concurrent.Executors.newFixedThreadPool(3); // Limit to 3 concurrent downloads

        setTitle("Gardener Dashboard - " + user.getName());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Ensure we shut down the thread pool when the app closes
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                imageLoader.shutdown();
            }
        });

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("My Projects", createProjectsPanel());
        tabbedPane.addTab("Tips", createTipsPanel());
        tabbedPane.addTab("Discussions", createDiscussionsPanel());
        tabbedPane.addTab("My Profile", createProfilePanel());

        add(tabbedPane);
    }

    private JPanel createProjectsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea projectArea = new JTextArea();
        projectArea.setEditable(false);
        panel.add(new JScrollPane(projectArea), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton refreshBtn = new JButton("Refresh");
        JButton addBtn = new JButton("New Project");
        JButton updateBtn = new JButton("Update Progress");

        refreshBtn.addActionListener(e -> {
            java.util.List<com.gardening.platform.model.Project> projects = projectService.getUserProjects(gardenerUser.getId());
            StringBuilder sb = new StringBuilder();
            if (projects.isEmpty()) sb.append("No projects yet.");
            for (com.gardening.platform.model.Project p : projects) {
                sb.append("ID: ").append(p.getId())
                  .append(" | Name: ").append(p.getName())
                  .append(" | Desc: ").append(p.getDescription())
                  .append(" | Status: ").append(p.getProgress())
                  .append("\n-----------------------\n");
            }
            projectArea.setText(sb.toString());
        });

        addBtn.addActionListener(e -> {
            String desc = JOptionPane.showInputDialog("Enter Project Description:");
            if (desc != null && !desc.isEmpty()) {
                com.gardening.platform.model.Project p = new com.gardening.platform.model.Project(
                    gardenerUser.getId(), "My Project", desc, "Started"
                );
                projectService.createProject(p);
                refreshBtn.doClick();
            }
        });

        updateBtn.addActionListener(e -> {
            String idStr = JOptionPane.showInputDialog("Enter Project ID to update:");
            String status = JOptionPane.showInputDialog("Enter new status (e.g. Completed):");
            if (idStr != null && status != null) {
                try {
                    projectService.updateProjectProgress(Integer.parseInt(idStr), status);
                    refreshBtn.doClick();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error updating project.");
                }
            }
        });

        btnPanel.add(refreshBtn);
        btnPanel.add(addBtn);
        btnPanel.add(updateBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);
        
        refreshBtn.doClick();
        return panel;
    }

    private JPanel createDiscussionsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea discussArea = new JTextArea();
        discussArea.setEditable(false);
        panel.add(new JScrollPane(discussArea), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton refreshBtn = new JButton("Refresh");
        JButton postBtn = new JButton("Post Comment");

        refreshBtn.addActionListener(e -> {
            java.util.List<com.gardening.platform.model.Discussion> list = communityService.getAllDiscussions();
            StringBuilder sb = new StringBuilder();
            for (com.gardening.platform.model.Discussion d : list) {
                sb.append("Topic: ").append(d.getTopic())
                  .append("\nComment: ").append(d.getComment())
                  .append("\n-----------------------\n");
            }
            discussArea.setText(sb.toString());
        });

        postBtn.addActionListener(e -> {
            String topic = JOptionPane.showInputDialog("Enter Topic:");
            String comment = JOptionPane.showInputDialog("Enter Comment:");
            if (topic != null && comment != null) {
                com.gardening.platform.model.Discussion d = new com.gardening.platform.model.Discussion(
                    gardenerUser.getId(), topic, comment
                );
                communityService.postDiscussion(d);
                refreshBtn.doClick();
            }
        });

        btnPanel.add(refreshBtn);
        btnPanel.add(postBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);
        
        refreshBtn.doClick();
        return panel;
    }

    private JPanel createTipsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Use a JPanel with BoxLayout for vertical list of tips
        JPanel tipsContainer = new JPanel();
        tipsContainer.setLayout(new BoxLayout(tipsContainer, BoxLayout.Y_AXIS));
        
        JScrollPane scrollPane = new JScrollPane(tipsContainer);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Bottom panel with buttons
        JPanel bottomPanel = new JPanel(new FlowLayout());
        
        JButton addTipButton = new JButton("Share New Tip");
        addTipButton.addActionListener(e -> showAddTipDialog(tipsContainer));
        
        JButton exportButton = new JButton("Export to CSV");
        exportButton.addActionListener(e -> exportTipsToCSV());
        
        JTextField searchField = new JTextField(15);
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchTips(tipsContainer, searchField.getText()));
        
        bottomPanel.add(new JLabel("Search:"));
        bottomPanel.add(searchField);
        bottomPanel.add(searchButton);
        bottomPanel.add(addTipButton);
        bottomPanel.add(exportButton);
        
        panel.add(bottomPanel, BorderLayout.SOUTH);

        // Multithreading demonstration: Load tips in background
        loadTips(tipsContainer);

        return panel;
    }

    private void loadTips(JPanel tipsContainer) {
        // Multithreading: Using a separate thread to load data so the UI doesn't freeze
        new Thread(() -> {
            try {
                // Simulate network delay
                Thread.sleep(1000); 
                
                // Fetch data from database
                java.util.List<com.gardening.platform.model.Tip> tips = communityService.getAllTips();
                
                // Update UI on the Event Dispatch Thread (EDT)
                SwingUtilities.invokeLater(() -> {
                    tipsContainer.removeAll();
                    if (tips.isEmpty()) {
                        tipsContainer.add(new JLabel("No tips available yet."));
                    } else {
                        displayTips(tipsContainer, tips);
                    }
                    tipsContainer.revalidate();
                    tipsContainer.repaint();
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void showAddTipDialog(JPanel tipsContainer) {
        JDialog dialog = new JDialog(this, "Share a Tip", true);
        dialog.setSize(300, 250);
        dialog.setLayout(new GridLayout(4, 2));
        dialog.setLocationRelativeTo(this);

        JTextField titleField = new JTextField();
        JTextArea descField = new JTextArea();
        JTextField photoField = new JTextField();

        dialog.add(new JLabel("Title:"));
        dialog.add(titleField);
        dialog.add(new JLabel("Description:"));
        dialog.add(new JScrollPane(descField));
        dialog.add(new JLabel("Photo URL:"));
        dialog.add(photoField);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String title = titleField.getText().trim();
            String desc = descField.getText().trim();
            String photo = photoField.getText().trim();
            
            // Input validation
            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Title is required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (desc.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Description is required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (title.length() > 100) {
                JOptionPane.showMessageDialog(dialog, "Title is too long (max 100 characters)", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                com.gardening.platform.model.Tip newTip = new com.gardening.platform.model.Tip(
                    gardenerUser.getId(), title, desc, photo
                );
                communityService.shareTip(newTip);
                JOptionPane.showMessageDialog(dialog, "Tip Shared Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
                loadTips(tipsContainer); // Reload tips
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error sharing tip: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        dialog.add(submitButton);

        dialog.setVisible(true);
    }
    
    // INNOVATIVE FEATURE: Search tips by keyword
    private void searchTips(JPanel tipsContainer, String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            loadTips(tipsContainer); // Reload all if empty
            return;
        }
        
        new Thread(() -> {
            try {
                java.util.List<com.gardening.platform.model.Tip> allTips = communityService.getAllTips();
                java.util.List<com.gardening.platform.model.Tip> filteredTips = new java.util.ArrayList<>();
                
                // Filter tips by keyword
                for (com.gardening.platform.model.Tip tip : allTips) {
                    if (tip.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                        tip.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                        filteredTips.add(tip);
                    }
                }
                
                SwingUtilities.invokeLater(() -> {
                    tipsContainer.removeAll();
                    if (filteredTips.isEmpty()) {
                        tipsContainer.add(new JLabel("No tips found matching: " + keyword));
                    } else {
                        displayTips(tipsContainer, filteredTips);
                    }
                    tipsContainer.revalidate();
                    tipsContainer.repaint();
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> 
                    JOptionPane.showMessageDialog(this, "Search error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE)
                );
            }
        }).start();
    }
    
    // INNOVATIVE FEATURE: Export tips to CSV file
    private void exportTipsToCSV() {
        try {
            java.util.List<com.gardening.platform.model.Tip> tips = communityService.getAllTips();
            
            if (tips.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No tips to export!", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Tips as CSV");
            fileChooser.setSelectedFile(new java.io.File("gardening_tips.csv"));
            
            int userSelection = fileChooser.showSaveDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                java.io.File fileToSave = fileChooser.getSelectedFile();
                
                try (java.io.PrintWriter writer = new java.io.PrintWriter(fileToSave)) {
                    writer.println("ID,Title,Description,Photo URL");
                    for (com.gardening.platform.model.Tip tip : tips) {
                        writer.println(String.format("%d,\"%s\",\"%s\",\"%s\"",
                            tip.getId(),
                            tip.getTitle().replace("\"", "\"\""),
                            tip.getDescription().replace("\"", "\"\""),
                            tip.getPhotoUrl() != null ? tip.getPhotoUrl() : ""
                        ));
                    }
                    JOptionPane.showMessageDialog(this, "Tips exported successfully to:\n" + fileToSave.getAbsolutePath(), 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (java.io.IOException e) {
                    JOptionPane.showMessageDialog(this, "Error writing file: " + e.getMessage(), 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Export error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Helper method to display tips (refactored for reuse)
    private void displayTips(JPanel tipsContainer, java.util.List<com.gardening.platform.model.Tip> tips) {
        for (com.gardening.platform.model.Tip tip : tips) {
            JPanel tipPanel = new JPanel();
            tipPanel.setLayout(new BoxLayout(tipPanel, BoxLayout.Y_AXIS));
            tipPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));
            tipPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            tipPanel.setBackground(Color.WHITE);
            
            JLabel titleLabel = new JLabel("Title: " + tip.getTitle());
            titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
            tipPanel.add(titleLabel);
            
            JLabel descLabel = new JLabel("<html><body style='width: 400px'>" + tip.getDescription() + "</body></html>");
            tipPanel.add(descLabel);
            
            // Handle Image Loading
            if (tip.getPhotoUrl() != null && !tip.getPhotoUrl().trim().isEmpty()) {
                tipPanel.add(Box.createRigidArea(new Dimension(0, 5)));
                JLabel imageLabel = new JLabel("Loading image...");
                tipPanel.add(imageLabel);
                
                // Load image using the thread pool
                imageLoader.submit(() -> {
                    try {
                        java.net.URL url = new java.net.URL(tip.getPhotoUrl());
                        ImageIcon icon = new ImageIcon(url);
                        Image img = icon.getImage().getScaledInstance(200, -1, Image.SCALE_SMOOTH);
                        ImageIcon scaledIcon = new ImageIcon(img);
                        icon.getImage().flush();
                        
                        SwingUtilities.invokeLater(() -> {
                            imageLabel.setText("");
                            imageLabel.setIcon(scaledIcon);
                        });
                    } catch (Exception e) {
                        SwingUtilities.invokeLater(() -> imageLabel.setText("[Image failed to load]"));
                    }
                });
            }
            
            tipsContainer.add(tipPanel);
            tipsContainer.add(Box.createRigidArea(new Dimension(0, 10)));
        }
    }
    
    // INNOVATIVE FEATURE: User Profile Management
    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Display current user info
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("User ID:"), gbc);
        gbc.gridx = 1;
        JLabel idLabel = new JLabel(String.valueOf(gardenerUser.getId()));
        panel.add(idLabel, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        JTextField nameField = new JTextField(gardenerUser.getName(), 20);
        panel.add(nameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        JLabel emailLabel = new JLabel(gardenerUser.getEmail());
        panel.add(emailLabel, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Role:"), gbc);
        gbc.gridx = 1;
        JLabel roleLabel = new JLabel(gardenerUser.getRole());
        panel.add(roleLabel, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("New Password:"), gbc);
        gbc.gridx = 1;
        JPasswordField passwordField = new JPasswordField(20);
        panel.add(passwordField, gbc);
        
        // Update button
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        JButton updateButton = new JButton("Update Profile");
        updateButton.addActionListener(e -> {
            String newName = nameField.getText().trim();
            String newPassword = new String(passwordField.getPassword()).trim();
            
            if (newName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name cannot be empty!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                gardenerUser.setName(newName);
                if (!newPassword.isEmpty()) {
                    gardenerUser.setPassword(newPassword);
                }
                
                com.gardening.platform.service.UserService userService = new com.gardening.platform.service.UserService();
                userService.updateUser(gardenerUser);
                
                setTitle("Gardener Dashboard - " + gardenerUser.getName());
                JOptionPane.showMessageDialog(this, "Profile updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                passwordField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Update failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(updateButton, gbc);
        
        // Statistics panel
        gbc.gridy = 6;
        gbc.insets = new Insets(30, 10, 10, 10);
        JPanel statsPanel = createStatisticsPanel();
        panel.add(statsPanel, gbc);
        
        return panel;
    }
    
    // INNOVATIVE FEATURE: Statistics Dashboard
    private JPanel createStatisticsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("My Statistics"));
        
        JLabel tipsLabel = new JLabel("Loading statistics...");
        JLabel projectsLabel = new JLabel("");
        JLabel discussionsLabel = new JLabel("");
        
        panel.add(tipsLabel);
        panel.add(projectsLabel);
        panel.add(discussionsLabel);
        
        // Load stats in background
        new Thread(() -> {
            try {
                int tipCount = communityService.getAllTips().stream()
                    .filter(t -> t.getUserId() == gardenerUser.getId())
                    .toArray().length;
                int projectCount = projectService.getUserProjects(gardenerUser.getId()).size();
                int discussionCount = communityService.getAllDiscussions().stream()
                    .filter(d -> d.getUserId() == gardenerUser.getId())
                    .toArray().length;
                
                SwingUtilities.invokeLater(() -> {
                    tipsLabel.setText("My Tips Shared: " + tipCount);
                    projectsLabel.setText("My Projects: " + projectCount);
                    discussionsLabel.setText("My Discussions: " + discussionCount);
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> tipsLabel.setText("Error loading statistics"));
            }
        }).start();
        
        return panel;
    }}