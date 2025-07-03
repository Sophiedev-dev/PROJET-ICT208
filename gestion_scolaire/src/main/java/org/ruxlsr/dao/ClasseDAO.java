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
    public List<Classe> getByNiveau(int niveauId) {
        List<Classe> list = new ArrayList<>();
        String sql = "SELECT * FROM classes WHERE niveau_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, niveauId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new Classe(rs.getInt("id"), rs.getString("nom"), rs.getInt("niveau_id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void delete(int id) {
        String sql = "DELETE FROM classes WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(int id, String nom) {
        String sql = "UPDATE classes set nom=?  WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nom);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
