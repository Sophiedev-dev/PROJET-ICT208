package org.ruxlsr.dao;

import org.ruxlsr.model.Classe;
import org.ruxlsr.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClasseDAO {

    public void insert(Classe classe) throws SQLException {
        String sql = "INSERT INTO classes(nom, niveau_id) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, classe.getNom());
            stmt.setInt(2, classe.getNiveauId());
            stmt.executeUpdate();
        }
    }

    public List<Classe> getAll() throws SQLException {
        List<Classe> classes = new ArrayList<>();
        String sql = "SELECT * FROM classes";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                classes.add(new Classe(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getInt("niveau_id")
                ));
            }
        }
        return classes;
    }
}
