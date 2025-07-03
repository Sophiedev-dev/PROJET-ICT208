package org.ruxlsr.dao;

import org.ruxlsr.model.Utilisateur;
import org.ruxlsr.utils.DatabaseConnection;

import java.sql.*;

public class UtilisateurDAO {

    public Utilisateur login(String identifiant, String motDePasse) {
        String sql = "SELECT * FROM utilisateurs WHERE identifiant = ? AND mot_de_passe = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, identifiant);
            stmt.setString(2, motDePasse);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Utilisateur(
                        rs.getInt("id"),
                        rs.getString("identifiant"),
                        rs.getString("mot_de_passe"),
                        rs.getString("role"),
                        rs.getObject("enseignant_id") != null ? rs.getInt("enseignant_id") : null,
                        rs.getObject("eleve_id") != null ? rs.getInt("eleve_id") : null
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteByEnseignantId(int enseignantId) {
        String sql = "DELETE FROM utilisateurs WHERE enseignant_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, enseignantId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insert(Utilisateur u) {
        String sql = "INSERT INTO utilisateurs (identifiant, mot_de_passe, role, enseignant_id, eleve_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, u.getIdentifiant());
            stmt.setString(2, u.getMotDePasse());
            stmt.setString(3, u.getRole());
            if (u.getEnseignantId() > 0) {
                stmt.setInt(4, u.getEnseignantId());
            } else {
                stmt.setNull(4, java.sql.Types.INTEGER);
            }
            if (u.getEleveId() > 0) {
                stmt.setInt(5, u.getEleveId());
            } else {
                stmt.setNull(5, java.sql.Types.INTEGER);
            }
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteByEleveId(int eleveId) {
        String sql = "DELETE FROM utilisateurs WHERE eleve_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, eleveId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}