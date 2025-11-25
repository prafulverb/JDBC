package com.gardening.platform.ui;

import com.gardening.platform.model.User;
import com.gardening.platform.service.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private UserService userService;

    public LoginFrame() {
        userService = new UserService();

        setTitle("Gardening Community - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 2, 10, 10));

        add(new JLabel("Email:"));
        emailField = new JTextField();
        add(emailField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
        add(loginButton);

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> showRegisterDialog());
        add(registerButton);
    }

    private void showRegisterDialog() {
        JDialog dialog = new JDialog(this, "Register", true);
        dialog.setSize(300, 300);
        dialog.setLayout(new GridLayout(5, 2));
        dialog.setLocationRelativeTo(this);

        JTextField nameField = new JTextField();
        JTextField regEmailField = new JTextField();
        JPasswordField regPasswordField = new JPasswordField();
        String[] roles = {"GARDENER", "ADMIN"};
        JComboBox<String> roleCombo = new JComboBox<>(roles);

        dialog.add(new JLabel("Name:"));
        dialog.add(nameField);
        dialog.add(new JLabel("Email:"));
        dialog.add(regEmailField);
        dialog.add(new JLabel("Password:"));
        dialog.add(regPasswordField);
        dialog.add(new JLabel("Role:"));
        dialog.add(roleCombo);

        JButton submitButton = new JButton("Register");
        submitButton.addActionListener(e -> {
            String name = nameField.getText();
            String email = regEmailField.getText();
            String password = new String(regPasswordField.getPassword());
            String role = (String) roleCombo.getSelectedItem();

            if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                User newUser;
                if ("ADMIN".equals(role)) {
                    newUser = new com.gardening.platform.model.Admin(name, email, password);
                } else {
                    newUser = new com.gardening.platform.model.Gardener(name, email, password);
                }

                if (userService.registerUser(newUser)) {
                    JOptionPane.showMessageDialog(dialog, "Registration Successful!");
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Email already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "All fields are required.");
            }
        });
        dialog.add(submitButton);

        dialog.setVisible(true);
    }

    private void handleLogin() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        User user = userService.login(email, password);
        if (user != null) {
            JOptionPane.showMessageDialog(this, "Login Successful! Welcome " + user.getName());
            dispose(); // Close login window
            
            if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                new AdminDashboard(user).setVisible(true);
            } else {
                new GardenerDashboard(user).setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid Credentials", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
