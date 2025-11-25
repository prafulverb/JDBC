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

    public GardenerDashboard(User user) {
        this.gardenerUser = user;
        this.communityService = new CommunityService();
        this.projectService = new ProjectService();

        setTitle("Gardener Dashboard - " + user.getName());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("My Projects", createProjectsPanel());
        tabbedPane.addTab("Tips", createTipsPanel());
        tabbedPane.addTab("Discussions", createDiscussionsPanel());

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

        JButton addTipButton = new JButton("Share New Tip");
        addTipButton.addActionListener(e -> showAddTipDialog(tipsContainer));
        panel.add(addTipButton, BorderLayout.SOUTH);

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
                                
                                // Load image in a separate thread to avoid blocking EDT
                                new Thread(() -> {
                                    try {
                                        java.net.URL url = new java.net.URL(tip.getPhotoUrl());
                                        ImageIcon icon = new ImageIcon(url);
                                        Image img = icon.getImage().getScaledInstance(200, -1, Image.SCALE_SMOOTH);
                                        ImageIcon scaledIcon = new ImageIcon(img);
                                        SwingUtilities.invokeLater(() -> {
                                            imageLabel.setText("");
                                            imageLabel.setIcon(scaledIcon);
                                        });
                                    } catch (Exception e) {
                                        SwingUtilities.invokeLater(() -> imageLabel.setText("[Image failed to load]"));
                                    }
                                }).start();
                            }
                            
                            tipsContainer.add(tipPanel);
                            tipsContainer.add(Box.createRigidArea(new Dimension(0, 10))); // Spacer
                        }
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
            String title = titleField.getText();
            String desc = descField.getText();
            String photo = photoField.getText();
            
            if (!title.isEmpty() && !desc.isEmpty()) {
                com.gardening.platform.model.Tip newTip = new com.gardening.platform.model.Tip(
                    gardenerUser.getId(), title, desc, photo
                );
                communityService.shareTip(newTip);
                JOptionPane.showMessageDialog(dialog, "Tip Shared Successfully!");
                dialog.dispose();
                loadTips(tipsContainer); // Reload tips
            } else {
                JOptionPane.showMessageDialog(dialog, "Title and Description are required.");
            }
        });
        dialog.add(submitButton);

        dialog.setVisible(true);
    }
}
