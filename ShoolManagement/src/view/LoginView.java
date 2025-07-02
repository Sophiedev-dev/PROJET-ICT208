package view;

import controller.AuthController;
import model.Role;
import model.Utilisateur;

import java.util.Scanner;

public class LoginView {
    private final AuthController authController = new AuthController();
    private final Scanner scanner = new Scanner(System.in);

    public void afficher() {
        System.out.println("=== Connexion ===");
        System.out.print("Identifiant : ");
        String identifiant = scanner.nextLine();
        System.out.print("Mot de passe : ");
        String motDePasse = scanner.nextLine();

        Utilisateur utilisateur = authController.login(identifiant, motDePasse);

        if (utilisateur == null) {
            System.out.println("Échec de la connexion. Identifiants invalides.");
        } else {
            switch (utilisateur.getRole()) {
                case ADMINISTRATEUR -> new AdminMenu().afficher(utilisateur);
                case ENSEIGNANT -> new EnseignantMenu().afficher(utilisateur);
                case ELEVE -> new EleveMenu().afficher(utilisateur);
            }
        }
    }
}
