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
    public int insertReturnId(Enseignant e) {
        String sql = "INSERT INTO enseignants (nom) VALUES (?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, e.getNom());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    public void updateNom(int id, String nom) {
        String sql = "UPDATE enseignants SET nom = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nom);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM enseignants WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int getIdByNom(String nom) {
        String sql = "SELECT id FROM enseignants WHERE nom = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nom);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public String getNomById(int id) {
        String sql = "SELECT nom FROM enseignants WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getString("nom");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
