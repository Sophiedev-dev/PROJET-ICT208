package dao;

import model.Cours;
import model.Enseignant;
import model.Classe;
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
                Classe c = new ClasseDAO().findById(rs.getInt("classe_id"));
                return new Cours(rs.getInt("id"), rs.getString("nom"), rs.getInt("coefficient"), e, c);
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
                Classe classe = new ClasseDAO().findById(rs.getInt("classe_id"));
                coursList.add(new Cours(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getInt("coefficient"),
                        enseignant,
                        classe));
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
            Classe c = new ClasseDAO().findById(rs.getInt("classe_id"));
            list.add(new Cours(rs.getInt("id"), rs.getString("nom"), rs.getInt("coefficient"), e, c));
        }
        return list;
    }

    public void insert(Cours cours) throws SQLException {
        String sql = "INSERT INTO cours(nom, coefficient, enseignant_id, classe_id) VALUES(?, ?, ?, ?)";
        try (PreparedStatement stmt = ConnexionDB.getConnexion().prepareStatement(sql)) {
            stmt.setString(1, cours.getNom());
            stmt.setInt(2, cours.getCoefficient());
            stmt.setInt(3, cours.getEnseignant().getId());
            stmt.setInt(4, cours.getClasse().getId());
            stmt.executeUpdate();
        }
    }

    public List<Cours> findByClasse(int classeId) throws SQLException {
        List<Cours> coursList = new ArrayList<>();
        String sql = "SELECT * FROM cours WHERE classe_id = ?";
        try (PreparedStatement stmt = ConnexionDB.getConnexion().prepareStatement(sql)) {
            stmt.setInt(1, classeId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Enseignant enseignant = new EnseignantDAO().findById(rs.getInt("enseignant_id"));
                Classe classe = new ClasseDAO().findById(rs.getInt("classe_id"));
                coursList.add(new Cours(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getInt("coefficient"),
                        enseignant,
                        classe));
            }
        }
        return coursList;
    }

}
