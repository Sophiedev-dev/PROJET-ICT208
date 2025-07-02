package controller;

import dao.UtilisateurDAO;
import model.Role;
import model.Utilisateur;

public class AuthController {

    private UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    public Utilisateur login(String identifiant, String motDePasse) {
        try {
            Utilisateur utilisateur = utilisateurDAO.findByIdentifiant(identifiant);
            if (utilisateur != null && utilisateur.getMotDePasse().equals(motDePasse)) {
                return utilisateur;
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la connexion : " + e.getMessage());
        }
        return null;
    }
}
