package com.gardening.platform.servlet;

import com.gardening.platform.model.User;
import com.gardening.platform.service.CommunityService;
import com.gardening.platform.model.Tip;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * Servlet for Gardener dashboard.
 * Demonstrates: Session validation, data display.
 */
@WebServlet("/gardener/dashboard")
public class GardenerDashboardServlet extends HttpServlet {
    
    private CommunityService communityService;
    
    @Override
    public void init() throws ServletException {
        super.init();
        communityService = new CommunityService();
        System.out.println("[SERVLET] GardenerDashboardServlet initialized");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Session management: Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        
        // Authorization check: Ensure user is a gardener
        if (!"GARDENER".equalsIgnoreCase(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Load data for dashboard
        List<Tip> allTips = communityService.getAllTips();
        request.setAttribute("tips", allTips);
        request.setAttribute("userName", user.getName());
        
        request.getRequestDispatcher("/WEB-INF/jsp/gardener-dashboard.jsp").forward(request, response);
    }
}
