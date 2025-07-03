package org.ruxlsr.dao;

import org.ruxlsr.model.Cours;
import org.ruxlsr.model.Niveau;
import org.ruxlsr.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CoursDAO {
    public List<Cours> getAll() throws SQLException {
        List<Cours> cours = new ArrayList<>();
        String sql = "SELECT * FROM cours";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                cours.add(new Cours(rs.getInt("id"), rs.getString("nom"), rs.getInt("coefficient"),rs.getInt("enseignant_id")));
            }
        }
        return cours;
    }
    public List<Cours> getByClasse(int classeId) {
        List<Cours> list = new ArrayList<>();
        String sql = """
        SELECT c.* FROM cours c
        JOIN cours_classes cc ON c.id = cc.cours_id
        WHERE cc.classe_id = ?
        """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, classeId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new Cours(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getInt("coefficient"),
                        rs.getInt("enseignant_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void supprimerCoursEtNotes(int coursId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement deleteNotes = conn.prepareStatement("DELETE FROM notes WHERE cours_id = ?");
            deleteNotes.setInt(1, coursId);
            deleteNotes.executeUpdate();

            PreparedStatement deleteCours = conn.prepareStatement("DELETE FROM cours WHERE id = ?");
            deleteCours.setInt(1, coursId);
            deleteCours.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void supprimerCoursParEnseignant(int enseignantId) {
        String sql = "DELETE FROM cours WHERE enseignant_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, enseignantId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void update(int id, String nom, int coef, int enseignantId) {
        String sql = "UPDATE cours SET nom = ?, coefficient = ?, enseignant_id = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nom);
            stmt.setInt(2, coef);
            stmt.setInt(3, enseignantId);
            stmt.setInt(4, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int insertReturnId(Cours cours) {
        String sql = "INSERT INTO cours (nom, coefficient, enseignant_id) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, cours.getNom());
            stmt.setInt(2, cours.getCoefficient());
            stmt.setInt(3, cours.getEnseignantId());

            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // en cas d’échec
    }


}
