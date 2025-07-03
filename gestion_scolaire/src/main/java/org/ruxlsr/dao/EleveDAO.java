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

    public void updateAnonymat(int eleveId, String idAnonymat) throws SQLException {
        String sql = "UPDATE eleves SET id_anonymat = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idAnonymat);
            stmt.setInt(2, eleveId);
            stmt.executeUpdate();
        }
    }

    public void supprimerParClasse(int classeId) {
        String sql = "DELETE FROM eleves WHERE classe_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, classeId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int insertReturnId(Eleve e) {
        String sql = "INSERT INTO eleves (nom, classe_id, id_anonymat) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, e.getNom());
            stmt.setInt(2, e.getClasseId());
            stmt.setString(3, e.getIdAnonymat());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }
    public void update(int id, String nom, int classeId) {
        String sql = "UPDATE eleves SET nom = ?, classe_id = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nom);
            stmt.setInt(2, classeId);
            stmt.setInt(3, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void delete(int id) {
        String sql = "DELETE FROM eleves WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
