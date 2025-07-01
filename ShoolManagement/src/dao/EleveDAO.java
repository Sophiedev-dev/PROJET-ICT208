package dao;

import java.sql.*;
import java.util.*;

import model.Eleve;
import utils.Database;

public class EleveDAO {
    private final Connection connection;

    public EleveDAO() {
        this.connection = Database.getConnection();
    }

    public void create(Eleve eleve) throws SQLException {
        String sql = """
                    INSERT INTO ELEVE (nom, prenom, date_naissance, adresse, telephone, id_classe)
                    VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, eleve.nom());
            stmt.setString(2, eleve.prenom());
            stmt.setDate(3, Date.valueOf(eleve.date_naissance()));
            stmt.setString(4, eleve.adresse());
            stmt.setString(5, eleve.telephone());
            stmt.setInt(6, eleve.id_classe());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    System.out.println("Élève inséré avec ID: " + id);
                }
            }
        }
    }

    public Eleve findById(int id) throws SQLException {
        String sql = "SELECT * FROM ELEVE WHERE id_eleve = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        }
        return null;
    }

    public List<Eleve> findAll() throws SQLException {
        List<Eleve> eleves = new ArrayList<>();
        String sql = "SELECT * FROM ELEVE";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                eleves.add(mapResultSet(rs));
            }
        }
        return eleves;
    }

    public void update(Eleve eleve) throws SQLException {
        String sql = """
                    UPDATE ELEVE SET nom = ?, prenom = ?, date_naissance = ?, adresse = ?, telephone = ?, id_classe = ?
                    WHERE id_eleve = ?
                """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, eleve.nom());
            stmt.setString(2, eleve.prenom());
            stmt.setDate(3, Date.valueOf(eleve.date_naissance()));
            stmt.setString(4, eleve.adresse());
            stmt.setString(5, eleve.telephone());
            stmt.setInt(6, eleve.id_classe());
            stmt.setInt(7, eleve.id_eleve());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM ELEVE WHERE id_eleve = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private Eleve mapResultSet(ResultSet rs) throws SQLException {
        return new Eleve(
                rs.getInt("id_eleve"),
                rs.getString("nom"),
                rs.getString("prenom"),
                rs.getDate("date_naissance").toLocalDate(),
                rs.getString("adresse"),
                rs.getString("telephone"),
                rs.getInt("id_classe"));
    }
}
