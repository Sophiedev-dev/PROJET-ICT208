package org.ruxlsr.dao;

import org.ruxlsr.model.Eleve;
import org.ruxlsr.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EleveDAO {

    public void insert(Eleve eleve) throws SQLException {
        String sql = "INSERT INTO eleves(nom, classe_id, id_anonymat) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, eleve.getNom());
            stmt.setInt(2, eleve.getClasseId());
            stmt.setString(3, eleve.getIdAnonymat());
            stmt.executeUpdate();
        }
    }

    public List<Eleve> getByClasse(int classeId) throws SQLException {
        List<Eleve> eleves = new ArrayList<>();
        String sql = "SELECT * FROM eleves WHERE classe_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, classeId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                eleves.add(new Eleve(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getInt("classe_id"),
                        rs.getString("id_anonymat")
                ));
            }
        }
        return eleves;
    }

    public List<Eleve> getAll() throws SQLException {
        List<Eleve> eleves = new ArrayList<>();
        String sql = "SELECT * FROM eleves";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                eleves.add(new Eleve(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getInt("classe_id"),
                        rs.getString("id_anonymat")
                ));
            }
        }
        return eleves;
    }
}
