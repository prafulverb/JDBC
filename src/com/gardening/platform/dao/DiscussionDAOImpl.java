package com.gardening.platform.dao;

import com.gardening.platform.model.Discussion;
import com.gardening.platform.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DiscussionDAOImpl implements DiscussionDAO {

    @Override
    public void addDiscussion(Discussion discussion) {
        String sql = "INSERT INTO discussions(user_id, topic, comment) VALUES(?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, discussion.getUserId());
            pstmt.setString(2, discussion.getTopic());
            pstmt.setString(3, discussion.getComment());
            // Timestamp is usually handled by DB or we can add it if column exists.
            // The table schema in DatabaseSetup.java:
            // "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            // "user_id INTEGER, " +
            // "topic TEXT NOT NULL, " +
            // "comment TEXT NOT NULL, " +
            // "FOREIGN KEY(user_id) REFERENCES users(id))";
            // It doesn't have a timestamp column. I should probably add it or ignore it.
            // The model has it. I'll ignore it for persistence for now as the DB schema doesn't support it.
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Discussion> getAllDiscussions() {
        List<Discussion> discussions = new ArrayList<>();
        String sql = "SELECT * FROM discussions";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                discussions.add(mapRowToDiscussion(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return discussions;
    }

    @Override
    public void deleteDiscussion(int id) {
        String sql = "DELETE FROM discussions WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Discussion mapRowToDiscussion(ResultSet rs) throws SQLException {
        return new Discussion(
                rs.getInt("id"),
                rs.getInt("user_id"),
                rs.getString("topic"),
                rs.getString("comment"),
                null // Timestamp not in DB
        );
    }
}
