package org.ruxlsr.service;

import org.ruxlsr.dao.*;
import org.ruxlsr.model.*;
import org.ruxlsr.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class AdminService {

    public void creerNiveau(String nom) throws SQLException {
        final NiveauDAO niveauDAO = new NiveauDAO();
        niveauDAO.insert(new Niveau(0, nom));
    }

    public void creerClasse(String nom, int niveauId) throws SQLException {
        final ClasseDAO classeDAO = new ClasseDAO();
        classeDAO.insert(new Classe(0, nom, niveauId));
    }

    public void creerEnseignant(String nom) throws SQLException {
        String sql = "INSERT INTO enseignants(nom) VALUES (?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nom);
            stmt.executeUpdate();
        }
    }

    public void creerCours(String nom, int coefficient, int enseignantId) throws SQLException {
        String sql = "INSERT INTO cours(nom, coefficient, enseignant_id) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nom);
            stmt.setInt(2, coefficient);
            stmt.setInt(3, enseignantId);
            stmt.executeUpdate();
        }
    }

    public void creerEleve(String nom, int classeId) throws SQLException {
        String sql = "INSERT INTO eleves(nom, classe_id) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nom);
            stmt.setInt(2, classeId);
            stmt.executeUpdate();
        }
    }

    public void genererAnonymat(int eleveId, String idAnonymat) throws SQLException {
        final EleveDAO eleveDao = new EleveDAO();
        eleveDao.updateAnonymat(eleveId, idAnonymat);
    }

    public List<Niveau> listerNiveaux() throws SQLException {
        return new NiveauDAO().getAll();
    }

    public List<Classe> listerClasses() throws SQLException {
        return new ClasseDAO().getAll();
    }
    public List<Enseignant> listerEnseignants() throws SQLException {
        return new EnseignantDAO().getAll();
    }
    public List<Cours> listerCours() throws SQLException {
        return new CoursDAO().getAll();
    }
    public List<Eleve> listerEleves() throws SQLException {
        return new EleveDAO().getAll();
    }
}