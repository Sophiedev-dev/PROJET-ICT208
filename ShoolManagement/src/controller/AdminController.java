package controller;

import dao.*;
import model.*;

import java.sql.SQLException;
import java.util.List;

public class AdminController {

    private NiveauDAO niveauDAO = new NiveauDAO();
    private ClasseDAO classeDAO = new ClasseDAO();
    private EnseignantDAO enseignantDAO = new EnseignantDAO();
    private CoursDAO coursDAO = new CoursDAO();
    private EleveDAO eleveDAO = new EleveDAO();

    public void creerNiveau(String nom) throws SQLException {
        niveauDAO.insert(new Niveau(0, nom));
    }

    public void creerClasse(String nom, Niveau niveau) throws SQLException {
        classeDAO.insert(new Classe(0, nom, niveau));
    }

    public void creerEnseignant(String nom) throws SQLException {
        enseignantDAO.insert(new Enseignant(0, nom));
    }

    public void creerCours(String nom, int coefficient, Enseignant enseignant) throws SQLException {
        coursDAO.insert(new Cours(0, nom, coefficient, enseignant));
    }

    public void inscrireEleve(String nom, Classe classe, String idAnonymat) throws SQLException {
        eleveDAO.insert(new Eleve(0, nom, classe, idAnonymat));
    }

    public List<Classe> getToutesLesClasses() throws SQLException {
        return classeDAO.findAll();
    }

    public List<Enseignant> getTousLesEnseignants() throws SQLException {
        return enseignantDAO.findAll();
    }

    public List<Niveau> getTousLesNiveaux() throws SQLException {
        return niveauDAO.findAll();
    }

    public List<Cours> getTousLesCours() throws SQLException {
        return coursDAO.findAll();
    }
}
