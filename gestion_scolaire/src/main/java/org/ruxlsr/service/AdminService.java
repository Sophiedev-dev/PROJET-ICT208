package org.ruxlsr.service;

import org.ruxlsr.dao.*;
import org.ruxlsr.model.*;
import org.ruxlsr.utils.DatabaseConnection;
import org.ruxlsr.utils.PasswordUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class AdminService {

    private final NiveauDAO niveauDAO = new NiveauDAO();
    private final ClasseDAO classeDAO = new ClasseDAO();
    private final CoursDAO coursDAO = new CoursDAO();
    private final EleveDAO eleveDAO = new EleveDAO();
    private final EnseignantDAO enseignantDAO = new EnseignantDAO();
    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO();


    public void creerClasse(String nom, int niveauId) throws SQLException {
        final ClasseDAO classeDAO = new ClasseDAO();
        classeDAO.insert(new Classe(0, nom, niveauId));
    }

    /*public void creerEnseignant(String nom) throws SQLException {
        String sql = "INSERT INTO enseignants(nom) VALUES (?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nom);
            stmt.executeUpdate();
        }
    }*/

    public void creerCours(String nom, int coefficient, int enseignantId) throws SQLException {
        String sql = "INSERT INTO cours(nom, coefficient, enseignant_id) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nom);
            stmt.setInt(2, coefficient);
            stmt.setInt(3, enseignantId);
            stmt.executeUpdate();
            System.out.println("insertion reussie");
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
    public List<Cours> listerCours() throws SQLException {
        return new CoursDAO().getAll();
    }
    public List<Eleve> listerEleves() throws SQLException {
        return new EleveDAO().getAll();
    }

    public void creerNiveau(String nom) throws SQLException {
        niveauDAO.insert(new Niveau(0, nom));
    }
    public List<Enseignant> listerEnseignants() throws SQLException {
        return new EnseignantDAO().getAll();
    }

    public void modifierNomNiveau(int id, String nom) {
        niveauDAO.updateNom(id, nom);
    }

    public void supprimerNiveauParIdCascade(int id) {
        List<Classe> classes = classeDAO.getByNiveau(id);
        for (Classe c : classes) {
            List<Cours> cours = coursDAO.getByClasse(c.getId());
            for (Cours co : cours) {
                coursDAO.supprimerCoursEtNotes(co.getId());
            }
            eleveDAO.supprimerParClasse(c.getId());
            classeDAO.delete(c.getId());
        }
        niveauDAO.delete(id);
    }

    public void creerEnseignant(String nom) {
        Enseignant e = new Enseignant(0, nom);
        int id = enseignantDAO.insertReturnId(e);
        String mdp = PasswordUtils.generateRandomPassword();
        utilisateurDAO.insert(new Utilisateur(0, nom.toLowerCase(), mdp, "ENSEIGNANT", id, 0));
    }

    public void modifierNomEnseignant(int id, String nom) {
        enseignantDAO.updateNom(id, nom);
    }

    public void supprimerEnseignantParId(int id) {
        utilisateurDAO.deleteByEnseignantId(id);
        coursDAO.supprimerCoursParEnseignant(id);
        enseignantDAO.delete(id);
    }


    public void supprimerEleveParId(int id) {
        eleveDAO.delete(id);
    }


    public void creerEleveAvecAnonymat(String nom, int classeId) {
        String anonymat = "A" + System.currentTimeMillis();
        Eleve e = new Eleve(0, nom, classeId, anonymat);
        int id = eleveDAO.insertReturnId(e);
        if (id > 0) {
          System.out.println("insertion reussie");
        } else {
            throw new RuntimeException("Erreur lors de l'ajout de l'élève. Insertion échouée.");
        }
    }


    public void modifierEleve(int id, String nom, int classeId) {
        eleveDAO.update(id, nom, classeId);
    }


    public void supprimerCoursParId(int id) {
        coursDAO.supprimerCoursEtNotes(id);
    }


    public int getIdEnseignantByNom(String nom) {
        return enseignantDAO.getIdByNom(nom);
    }


    public void modifierCours(int id, String nomCours, int coefficient, int enseignantId) {
        coursDAO.update(id, nomCours, coefficient, enseignantId);
    }


    public String getNomEnseignantById(int enseignantId) {
        return enseignantDAO.getNomById(enseignantId);
    }

    public String getNomNiveauById(int niveauId) {
       return niveauDAO.getNomById(niveauId);
    }

    public void modifierNomClasse(int id, String nom) {
        classeDAO.update(id, nom);
    }

    public void supprimerClasseParId(int i) {
        classeDAO.delete(i);
    }

    public List<Cours> listerCoursParClasse(int classeId) {
        return coursDAO.getByClasse(classeId);
    }
    public List<Eleve> getElevesByClasse(int classeId) throws SQLException {
        return eleveDAO.getByClasse(classeId);
    }

}