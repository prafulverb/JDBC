package com.gardening.platform.service;

import com.gardening.platform.dao.DiscussionDAO;
import com.gardening.platform.dao.DiscussionDAOImpl;
import com.gardening.platform.dao.TipDAO;
import com.gardening.platform.dao.TipDAOImpl;
import com.gardening.platform.model.Discussion;
import com.gardening.platform.model.Tip;

import java.util.List;

/**
 * Service class for managing community features like Tips and Discussions.
 * Acts as a bridge between the GUI and the DAO layer.
 */
public class CommunityService {
    private TipDAO tipDAO;
    private DiscussionDAO discussionDAO;
    
    // Synchronization demonstration: Counter for tips shared in the current session
    private static int totalTipsSharedSession = 0;

    public CommunityService() {
        this.tipDAO = new TipDAOImpl();
        this.discussionDAO = new DiscussionDAOImpl();
    }

    public void shareTip(Tip tip) {
        tipDAO.addTip(tip);
        incrementTipCount(); // Update the synchronized counter
    }
    
    // Synchronized method to handle concurrent access to the counter
    // This ensures thread safety if multiple threads try to share tips at once
    private synchronized void incrementTipCount() {
        totalTipsSharedSession++;
        System.out.println("Total Tips Shared in this Session (Synchronized): " + totalTipsSharedSession);
    }

    public List<Tip> getAllTips() {
        return tipDAO.getAllTips();
    }

    public void postDiscussion(Discussion discussion) {
        discussionDAO.addDiscussion(discussion);
    }

    public List<Discussion> getAllDiscussions() {
        return discussionDAO.getAllDiscussions();
    }
    
    // Admin moderation methods
    public void deleteTip(int id) {
        tipDAO.deleteTip(id);
    }
    
    public void deleteDiscussion(int id) {
        discussionDAO.deleteDiscussion(id);
    }
}
