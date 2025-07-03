package org.ruxlsr.dao;

import org.ruxlsr.model.CoursClasse;
import org.ruxlsr.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CoursClasseDAO {
    public void insert(CoursClasse cc) throws SQLException {
        String sql = "INSERT INTO cours_classes(cours_id, classe_id) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cc.getCoursId());
            stmt.setInt(2, cc.getClasseId());
            stmt.executeUpdate();
        }
    }

    public List<CoursClasse> getByClasse(int classeId) throws SQLException {
        List<CoursClasse> list = new ArrayList<>();
        String sql = "SELECT * FROM cours_classes WHERE classe_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, classeId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new CoursClasse(
                        rs.getInt("id"),
                        rs.getInt("cours_id"),
                        rs.getInt("classe_id")
                ));
            }
        }
        return list;
    }
}
