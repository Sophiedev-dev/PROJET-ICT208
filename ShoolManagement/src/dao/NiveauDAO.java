package dao;

import model.Niveau;
import java.sql.*;
import java.util.*;

public class NiveauDAO {

    public Niveau findById(int id) throws SQLException {
        String sql = "SELECT * FROM niveaux WHERE id = ?";
        try (PreparedStatement stmt = ConnexionDB.getConnexion().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Niveau(rs.getInt("id"), rs.getString("nom"));
            }
        }
        return null;
    }

    public List<Niveau> findAll() throws SQLException {
        List<Niveau> list = new ArrayList<>();
        String sql = "SELECT * FROM niveaux";
        Statement stmt = ConnexionDB.getConnexion().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            list.add(new Niveau(rs.getInt("id"), rs.getString("nom")));
        }
        return list;
    }

    public void insert(Niveau niveau) throws SQLException {
        String sql = "INSERT INTO niveaux(nom) VALUES(?)";
        try (PreparedStatement stmt = ConnexionDB.getConnexion().prepareStatement(sql)) {
            stmt.setString(1, niveau.getNom());
            stmt.executeUpdate();
        }
    }
}
