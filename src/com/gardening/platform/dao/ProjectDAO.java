package com.gardening.platform.dao;

import com.gardening.platform.model.Project;
import java.util.List;

public interface ProjectDAO {
    void addProject(Project project);
    List<Project> getProjectsByUserId(int userId);
    void updateProjectProgress(int projectId, String progress);
    void deleteProject(int id);
}
