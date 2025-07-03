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
}