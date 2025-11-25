package com.gardening.platform.dao;

import com.gardening.platform.model.Discussion;
import java.util.List;

public interface DiscussionDAO {
    void addDiscussion(Discussion discussion);
    List<Discussion> getAllDiscussions();
    void deleteDiscussion(int id);
}
