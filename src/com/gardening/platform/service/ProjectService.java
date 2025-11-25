package com.gardening.platform.service;

import com.gardening.platform.dao.ProjectDAO;
import com.gardening.platform.dao.ProjectDAOImpl;
import com.gardening.platform.model.Project;

import java.util.List;

public class ProjectService {
    private ProjectDAO projectDAO;

    public ProjectService() {
        this.projectDAO = new ProjectDAOImpl();
    }

    public void createProject(Project project) {
        projectDAO.addProject(project);
    }

    public List<Project> getUserProjects(int userId) {
        return projectDAO.getProjectsByUserId(userId);
    }

    public void updateProjectProgress(int projectId, String progress) {
        projectDAO.updateProjectProgress(projectId, progress);
    }
}
