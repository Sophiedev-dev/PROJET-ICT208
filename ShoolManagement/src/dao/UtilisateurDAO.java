package dao;

import model.*;

import java.sql.*;

public class UtilisateurDAO {

    public Utilisateur findByIdentifiant(String identifiant) throws SQLException {
        String sql = "SELECT * FROM utilisateurs WHERE identifiant = ?";
        try (PreparedStatement stmt = ConnexionDB.getConnexion().prepareStatement(sql)) {
            stmt.setString(1, identifiant);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Role role = Role.valueOf(rs.getString("role"));
                Utilisateur u = new Utilisateur(
                    rs.getInt("id"),
                    rs.getString("identifiant"),
                    rs.getString("mot_de_passe"),
                    role
                );

                if (role == Role.ENSEIGNANT && rs.getInt("enseignant_id") != 0) {
                    Enseignant e = new EnseignantDAO().findById(rs.getInt("enseignant_id"));
                    u.setEnseignant(e);
                }

                if (role == Role.ELEVE && rs.getInt("eleve_id") != 0) {
                    Eleve e = new EleveDAO().findById(rs.getInt("eleve_id"));
                    u.setEleve(e);
                }

                return u;
            }
        }
        return null;
    }

    public void insert(Utilisateur u) throws SQLException {
        String sql = "INSERT INTO utilisateurs(identifiant, mot_de_passe, role, enseignant_id, eleve_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = ConnexionDB.getConnexion().prepareStatement(sql)) {
            stmt.setString(1, u.getIdentifiant());
            stmt.setString(2, u.getMotDePasse());
            stmt.setString(3, u.getRole().name());

            if (u.getEnseignant() != null) {
                stmt.setInt(4, u.getEnseignant().getId());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }

            if (u.getEleve() != null) {
                stmt.setInt(5, u.getEleve().getId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }

            stmt.executeUpdate();
        }
    }
}
