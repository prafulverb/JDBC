package com.gardening.platform.service;

import com.gardening.platform.dao.UserDAO;
import com.gardening.platform.dao.UserDAOImpl;
import com.gardening.platform.model.User;

import java.util.List;

public class UserService {
    private UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAOImpl();
    }

    public User login(String email, String password) {
        User user = userDAO.getUserByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public boolean registerUser(User user) {
        if (userDAO.getUserByEmail(user.getEmail()) != null) {
            return false; // User already exists
        }
        userDAO.addUser(user);
        return true;
    }

    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    public void updateUser(User user) {
        userDAO.updateUser(user);
    }

    public void deleteUser(int id) {
        userDAO.deleteUser(id);
    }
}
