package org.ruxlsr.dao;

import org.ruxlsr.model.CoursClasse;
import org.ruxlsr.utils.DatabaseConnection;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public int getClasseIdByCoursId(int coursId) {
        String sql = "SELECT classe_id FROM cours_classes WHERE cours_id = ? LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, coursId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("classe_id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void afficherCoursEtClassesPourEnseignant(int enseignantId) {
        String sql = """
        SELECT cours.nom AS cours_nom, classes.nom AS classe_nom
        FROM cours
        JOIN cours_classes ON cours.id = cours_classes.cours_id
        JOIN classes ON cours_classes.classe_id = classes.id
        WHERE cours.enseignant_id = ?
    """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, enseignantId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println("Cours : " + rs.getString("cours_nom") + ", Classe : " + rs.getString("classe_nom"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Set<Integer> getClasseIdsByEnseignant(int enseignantId) {
        Set<Integer> ids = new HashSet<>();
        String sql = """
        SELECT DISTINCT cc.classe_id
        FROM cours c
        JOIN cours_classes cc ON c.id = cc.cours_id
        WHERE c.enseignant_id = ?
    """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, enseignantId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ids.add(rs.getInt("classe_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ids;
    }

    public void remplirCoursEtClassesPourEnseignant(int enseignantId, DefaultTableModel model) {
        String sql = """
        SELECT cours.id, cours.nom AS cours_nom, classes.nom AS classe_nom
        FROM cours
        JOIN cours_classes ON cours.id = cours_classes.cours_id
        JOIN classes ON cours_classes.classe_id = classes.id
        WHERE cours.enseignant_id = ?
    """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, enseignantId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("cours_nom"),
                        rs.getString("classe_nom")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
