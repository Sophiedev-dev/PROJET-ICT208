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
    // Étape 1 : Créer et insérer l'enseignant
    Enseignant enseignant = new Enseignant(0, nom);
    enseignantDAO.insert(enseignant);

    // Étape 2 : Récupérer l'enseignant avec son ID (option dépendant de la méthode insert)
    // Si insert() ne met pas à jour l'objet avec son ID, il faut une méthode pour le retrouver
    // Ex : on suppose que l'enseignant inséré est en dernier, on peut faire :
    Enseignant enseignantCree = enseignantDAO.findDernierCree();

    // Étape 3 : Créer un identifiant et un mot de passe par défaut
    String identifiant = nom.toLowerCase().replaceAll("\\s+", "") + enseignantCree.getId();  // ex: "jeandupont2"
    String motDePasse = "default123"; // à modifier ou crypter plus tard

    // Étape 4 : Créer et insérer l'utilisateur
    Utilisateur utilisateur = new Utilisateur(0, identifiant, motDePasse, Role.ENSEIGNANT);
    utilisateur.setEnseignant(enseignantCree);

    new UtilisateurDAO().insert(utilisateur);

    System.out.println("Utilisateur enseignant créé : " + identifiant + " / " + motDePasse);
}


    public void creerCours(String nom, int coefficient, Enseignant enseignant, Classe classe) throws SQLException {
        coursDAO.insert(new Cours(0, nom, coefficient, enseignant, classe));
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
