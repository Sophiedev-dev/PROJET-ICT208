package dao;

import model.Cours;
import model.Enseignant;
import java.sql.*;
import java.util.*;

public class CoursDAO {

    public Cours findById(int id) throws SQLException {
        String sql = "SELECT * FROM cours WHERE id = ?";
        try (PreparedStatement stmt = ConnexionDB.getConnexion().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Enseignant e = new EnseignantDAO().findById(rs.getInt("enseignant_id"));
                return new Cours(rs.getInt("id"), rs.getString("nom"), rs.getInt("coefficient"), e);
            }
        }
        return null;
    }

    public List<Cours> findByEnseignant(int enseignantId) throws SQLException {
    List<Cours> coursList = new ArrayList<>();
    String sql = "SELECT * FROM cours WHERE enseignant_id = ?";
    try (PreparedStatement stmt = ConnexionDB.getConnexion().prepareStatement(sql)) {
        stmt.setInt(1, enseignantId);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Enseignant enseignant = new EnseignantDAO().findById(enseignantId);
            coursList.add(new Cours(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getInt("coefficient"),
                enseignant
            ));
        }
    }
    return coursList;
}


    public List<Cours> findAll() throws SQLException {
        List<Cours> list = new ArrayList<>();
        String sql = "SELECT * FROM cours";
        Statement stmt = ConnexionDB.getConnexion().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            Enseignant e = new EnseignantDAO().findById(rs.getInt("enseignant_id"));
            list.add(new Cours(rs.getInt("id"), rs.getString("nom"), rs.getInt("coefficient"), e));
        }
        return list;
    }

    public void insert(Cours cours) throws SQLException {
        String sql = "INSERT INTO cours(nom, coefficient, enseignant_id) VALUES(?, ?, ?)";
        try (PreparedStatement stmt = ConnexionDB.getConnexion().prepareStatement(sql)) {
            stmt.setString(1, cours.getNom());
            stmt.setInt(2, cours.getCoefficient());
            stmt.setInt(3, cours.getEnseignant().getId());
            stmt.executeUpdate();
        }
    }
}
