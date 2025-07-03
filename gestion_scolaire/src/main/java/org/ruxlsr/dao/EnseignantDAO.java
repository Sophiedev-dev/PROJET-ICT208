package org.ruxlsr.dao;

import org.ruxlsr.model.Enseignant;
import org.ruxlsr.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnseignantDAO {

    public void insert(Enseignant enseignant) throws SQLException {
        String sql = "INSERT INTO enseignants(nom) VALUES (?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, enseignant.getNom());
            stmt.executeUpdate();
        }
    }

    public List<Enseignant> getAll() throws SQLException {
        List<Enseignant> enseignants = new ArrayList<>();
        String sql = "SELECT * FROM enseignants";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                enseignants.add(new Enseignant(rs.getInt("id"), rs.getString("nom")));
            }
        }
        return enseignants;
    }
}
