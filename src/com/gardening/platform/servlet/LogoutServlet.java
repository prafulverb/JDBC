package com.gardening.platform.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * Servlet for handling user logout.
 * Demonstrates: Session management and invalidation.
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Session management: Invalidate user session
        HttpSession session = request.getSession(false);
        if (session != null) {
            String email = session.getAttribute("user") != null ? 
                ((com.gardening.platform.model.User) session.getAttribute("user")).getEmail() : "Unknown";
            session.invalidate();
            System.out.println("[SERVLET] User logged out: " + email);
        }
        
        response.sendRedirect(request.getContextPath() + "/login");
    }
}
