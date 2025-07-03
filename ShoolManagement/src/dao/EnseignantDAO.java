package dao;

import model.Enseignant;
import java.sql.*;
import java.util.*;

public class EnseignantDAO {

    public Enseignant findById(int id) throws SQLException {
        String sql = "SELECT * FROM enseignants WHERE id = ?";
        try (PreparedStatement stmt = ConnexionDB.getConnexion().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Enseignant(rs.getInt("id"), rs.getString("nom"));
            }
        }
        return null;
    }

    public List<Enseignant> findAll() throws SQLException {
        List<Enseignant> list = new ArrayList<>();
        String sql = "SELECT * FROM enseignants";
        Statement stmt = ConnexionDB.getConnexion().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            list.add(new Enseignant(rs.getInt("id"), rs.getString("nom")));
        }
        return list;
    }

    public void insert(Enseignant enseignant) throws SQLException {
        String sql = "INSERT INTO enseignants(nom) VALUES(?)";
        try (PreparedStatement stmt = ConnexionDB.getConnexion().prepareStatement(sql)) {
            stmt.setString(1, enseignant.getNom());
            stmt.executeUpdate();
        }
    }

    public Enseignant findDernierCree() throws SQLException {
    String sql = "SELECT * FROM enseignants ORDER BY id DESC LIMIT 1";
    Statement stmt = ConnexionDB.getConnexion().createStatement();
    ResultSet rs = stmt.executeQuery(sql);
    if (rs.next()) {
        return new Enseignant(rs.getInt("id"), rs.getString("nom"));
    }
    return null;
}

}
