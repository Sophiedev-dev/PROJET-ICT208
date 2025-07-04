package org.ruxlsr.service;

import org.ruxlsr.model.Utilisateur;
import org.ruxlsr.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AuthService {

    public Utilisateur authentifier(String identifiant, String motDePasse) {
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
                        rs.getInt("enseignant_id"),
                        rs.getInt("eleve_id")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Utilisateur authentifierParIdEnseignant(int enseignantId, String mdp) {
        String sql = "SELECT * FROM utilisateurs WHERE enseignant_id = ? AND mot_de_passe = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, enseignantId);
            stmt.setString(2, mdp);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Utilisateur(
                        rs.getInt("id"),
                        rs.getString("identifiant"),
                        rs.getString("mot_de_passe"),
                        rs.getString("role"),
                        rs.getInt("enseignant_id"),
                        rs.getInt("eleve_id")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
