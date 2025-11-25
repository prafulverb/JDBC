package com.gardening.platform.dao;

import com.gardening.platform.model.Project;
import com.gardening.platform.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectDAOImpl implements ProjectDAO {

    @Override
    public void addProject(Project project) {
        String sql = "INSERT INTO projects(user_id, description, progress) VALUES(?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, project.getUserId());
            pstmt.setString(2, project.getDescription());
            pstmt.setString(3, project.getProgress());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Project> getProjectsByUserId(int userId) {
        List<Project> projects = new ArrayList<>();
        String sql = "SELECT * FROM projects WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                projects.add(mapRowToProject(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projects;
    }

    @Override
    public void updateProjectProgress(int projectId, String progress) {
        String sql = "UPDATE projects SET progress = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, progress);
            pstmt.setInt(2, projectId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteProject(int id) {
        String sql = "DELETE FROM projects WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Project mapRowToProject(ResultSet rs) throws SQLException {
        // Note: The table schema in DatabaseSetup.java for projects is:
        // id, user_id, description, progress.
        // The Project model has: id, userId, name, description, progress.
        // The table is missing 'name'. I should probably update the table or the model.
        // For now, I will map 'name' to a default or empty string, or update the table schema.
        // Let's check DatabaseSetup.java again.
        // String createProjects = "CREATE TABLE IF NOT EXISTS projects (" +
        //            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
        //            "user_id INTEGER, " +
        //            "description TEXT NOT NULL, " +
        //            "progress TEXT, " +
        //            "FOREIGN KEY(user_id) REFERENCES users(id))";
        // It is missing 'name'. I will update the Project model to match the DB or vice versa.
        // The user requirement says: "Project details (description, progress)". It doesn't explicitly say 'name'.
        // But usually projects have names.
        // I'll stick to the DB schema for now and maybe use description as name or just ignore name.
        // Actually, I'll just pass "Project " + id as name for now to satisfy the constructor.
        
        int id = rs.getInt("id");
        return new Project(
                id,
                rs.getInt("user_id"),
                "Project " + id, 
                rs.getString("description"),
                rs.getString("progress")
        );
    }
}
