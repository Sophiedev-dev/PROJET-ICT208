package dao;

import model.Classe;
import model.Niveau;
import java.sql.*;
import java.util.*;

public class ClasseDAO {

    public Classe findById(int id) throws SQLException {
        String sql = "SELECT * FROM classes WHERE id = ?";
        try (PreparedStatement stmt = ConnexionDB.getConnexion().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Niveau niveau = new NiveauDAO().findById(rs.getInt("niveau_id"));
                return new Classe(rs.getInt("id"), rs.getString("nom"), niveau);
            }
        }
        return null;
    }

    public List<Classe> findAll() throws SQLException {
        List<Classe> list = new ArrayList<>();
        String sql = "SELECT * FROM classes";
        Statement stmt = ConnexionDB.getConnexion().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            Niveau niveau = new NiveauDAO().findById(rs.getInt("niveau_id"));
            list.add(new Classe(rs.getInt("id"), rs.getString("nom"), niveau));
        }
        return list;
    }

    public void insert(Classe classe) throws SQLException {
        String sql = "INSERT INTO classes(nom, niveau_id) VALUES(?, ?)";
        try (PreparedStatement stmt = ConnexionDB.getConnexion().prepareStatement(sql)) {
            stmt.setString(1, classe.getNom());
            stmt.setInt(2, classe.getNiveau().getId());
            stmt.executeUpdate();
        }
    }
}
