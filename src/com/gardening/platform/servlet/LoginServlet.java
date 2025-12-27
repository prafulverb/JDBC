package com.gardening.platform.servlet;

import com.gardening.platform.model.User;
import com.gardening.platform.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * Servlet for handling user authentication (Login/Logout).
 * Demonstrates: Servlet lifecycle, session management, form handling.
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    
    private UserService userService;
    
    @Override
    public void init() throws ServletException {
        // Servlet lifecycle: init() called once when servlet is first loaded
        super.init();
        userService = new UserService();
        System.out.println("[SERVLET] LoginServlet initialized");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Display login form
        request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Form handling: Process login form submission
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        // Input validation
        if (email == null || email.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Email and password are required");
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
            return;
        }
        
        // Authenticate user
        User user = userService.login(email.trim(), password);
        
        if (user != null) {
            // Session management: Create session and store user
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setAttribute("userId", user.getId());
            session.setAttribute("userRole", user.getRole());
            session.setMaxInactiveInterval(30 * 60); // 30 minutes
            
            System.out.println("[SERVLET] User logged in: " + user.getEmail() + " (Role: " + user.getRole() + ")");
            
            // Redirect based on role
            if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/gardener/dashboard");
            }
        } else {
            request.setAttribute("error", "Invalid email or password");
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
        }
    }
    
    @Override
    public void destroy() {
        // Servlet lifecycle: cleanup when servlet is unloaded
        System.out.println("[SERVLET] LoginServlet destroyed");
        super.destroy();
    }
}
