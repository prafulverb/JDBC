package com.gardening.platform.dao;

import com.gardening.platform.model.Tip;
import com.gardening.platform.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TipDAOImpl implements TipDAO {

    @Override
    public void addTip(Tip tip) {
        String sql = "INSERT INTO tips(user_id, title, description, photo_path) VALUES(?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, tip.getUserId());
            pstmt.setString(2, tip.getTitle());
            pstmt.setString(3, tip.getDescription());
            pstmt.setString(4, tip.getPhotoUrl());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Tip> getAllTips() {
        List<Tip> tips = new ArrayList<>();
        String sql = "SELECT * FROM tips";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                tips.add(mapRowToTip(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tips;
    }

    @Override
    public List<Tip> getTipsByUserId(int userId) {
        List<Tip> tips = new ArrayList<>();
        String sql = "SELECT * FROM tips WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                tips.add(mapRowToTip(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tips;
    }

    @Override
    public void deleteTip(int id) {
        String sql = "DELETE FROM tips WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Tip mapRowToTip(ResultSet rs) throws SQLException {
        return new Tip(
                rs.getInt("id"),
                rs.getInt("user_id"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getString("photo_path")
        );
    }
}
