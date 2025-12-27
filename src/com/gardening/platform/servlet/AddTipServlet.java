package com.gardening.platform.servlet;

import com.gardening.platform.model.Tip;
import com.gardening.platform.model.User;
import com.gardening.platform.service.CommunityService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * Servlet for adding new tips.
 * Demonstrates: Form handling, session validation.
 */
@WebServlet("/gardener/addTip")
public class AddTipServlet extends HttpServlet {
    
    private CommunityService communityService;
    
    @Override
    public void init() throws ServletException {
        super.init();
        communityService = new CommunityService();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Session validation
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        
        // Form handling
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String photoUrl = request.getParameter("photoUrl");
        
        // Validation
        if (title == null || title.trim().isEmpty()) {
            request.setAttribute("error", "Title is required");
            response.sendRedirect(request.getContextPath() + "/gardener/dashboard?error=title_required");
            return;
        }
        
        if (description == null || description.trim().isEmpty()) {
            request.setAttribute("error", "Description is required");
            response.sendRedirect(request.getContextPath() + "/gardener/dashboard?error=desc_required");
            return;
        }
        
        // Create and save tip
        Tip tip = new Tip(user.getId(), title.trim(), description.trim(), photoUrl != null ? photoUrl.trim() : "");
        communityService.shareTip(tip);
        
        System.out.println("[SERVLET] Tip added by user: " + user.getEmail());
        
        response.sendRedirect(request.getContextPath() + "/gardener/dashboard?success=tip_added");
    }
}
