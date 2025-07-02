package services;

import models.Administrateur;
import models.Enseignant;
import models.Eleve;
import models.Utilisateur;

public class AuthenticationService {
    private static AuthenticationService instance;
    private Utilisateur utilisateurConnecte;

    private AuthenticationService() {}

    public static AuthenticationService getInstance() {
        if (instance == null) {
            instance = new AuthenticationService();
        }
        return instance;
    }

    public boolean connecterAdministrateur(String login, String motDePasse) {
        // TODO: Vérifier les credentials dans la base de données
        Administrateur admin = new Administrateur("Admin", "System", "admin@school.com", "admin", "admin123");
        if (admin.authentifier(login, motDePasse)) {
            utilisateurConnecte = admin;
            return true;
        }
        return false;
    }

    public boolean connecterEnseignant(String email, String motDePasse) {
        // TODO: Vérifier les credentials dans la base de données
        Enseignant enseignant = new Enseignant("Dupont", "Jean", email, "0123456789", "Mathématiques");
        if (enseignant.authentifier(email, motDePasse)) {
            utilisateurConnecte = enseignant;
            return true;
        }
        return false;
    }

    public boolean connecterEleve(String nom, String motDePasse) {
        // TODO: Vérifier les credentials dans la base de données
        Eleve eleve = new Eleve(nom, "Prénom", "eleve@school.com", new java.util.Date(), "Adresse", "0123456789", "Terminale S");
        if (eleve.authentifier(nom, motDePasse)) {
            utilisateurConnecte = eleve;
            return true;
        }
        return false;
    }

    public Utilisateur getUtilisateurConnecte() {
        return utilisateurConnecte;
    }

    public void deconnecter() {
        utilisateurConnecte = null;
    }
}