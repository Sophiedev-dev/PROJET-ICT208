package org.ruxlsr.dao;

import org.ruxlsr.model.Niveau;
import org.ruxlsr.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NiveauDAO {

    public void insert(Niveau niveau) throws SQLException {
        String sql = "INSERT INTO niveaux(nom) VALUES (?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, niveau.getNom());
            stmt.executeUpdate();
        }
    }

    public List<Niveau> getAll() throws SQLException {
        List<Niveau> niveaux = new ArrayList<>();
        String sql = "SELECT * FROM niveaux";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                niveaux.add(new Niveau(rs.getInt("id"), rs.getString("nom")));
            }
        }
        return niveaux;
    }

    public void updateNom(int id, String nouveauNom) {
        String sql = "UPDATE niveaux SET nom = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nouveauNom);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM niveaux WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public String getNomById(int id) {
        String sql = "SELECT nom FROM niveaux WHERE id = ?";
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