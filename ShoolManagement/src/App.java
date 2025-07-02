import java.util.Scanner;
import services.AuthenticationService;

public class App {
    private static Scanner scanner = new Scanner(System.in);
    private static AuthenticationService authService = AuthenticationService.getInstance();

    public static void main(String[] args) throws Exception {
        System.out.println("=== SYSTÈME DE GESTION SCOLAIRE ===");
        authentification();
    }

    private static void authentification() {
        System.out.println("\nConnexion au système");
        System.out.println("1. Administrateur");
        System.out.println("2. Enseignant");
        System.out.println("3. Élève");
        System.out.print("Choisissez votre rôle (1-3): ");

        int choix = scanner.nextInt();
        scanner.nextLine(); // Vider le buffer

        System.out.print("Login/Email: ");
        String login = scanner.nextLine();
        System.out.print("Mot de passe: ");
        String motDePasse = scanner.nextLine();

        boolean connecte = false;

        switch (choix) {
            case 1:
                connecte = authService.connecterAdministrateur(login, motDePasse);
                if (connecte) menuAdministrateur();
                break;
            case 2:
                connecte = authService.connecterEnseignant(login, motDePasse);
                if (connecte) menuEnseignant();
                break;
            case 3:
                connecte = authService.connecterEleve(login, motDePasse);
                if (connecte) menuEleve();
                break;
            default:
                System.out.println("Choix invalide!");
                authentification();
                return;
        }

        if (!connecte) {
            System.out.println("Échec de l'authentification. Veuillez réessayer.");
            authentification();
        }
    }

    private static void menuAdministrateur() {
        System.out.println("\n=== MENU ADMINISTRATEUR ===");
        System.out.println("1. Gestion des élèves");
        System.out.println("2. Gestion des enseignants");
        System.out.println("3. Gestion des classes");
        System.out.println("4. Gestion des cours");
        System.out.println("5. Déconnexion");
        System.out.print("Votre choix (1-5): ");

        int choix = scanner.nextInt();
        if (choix == 5) {
            authService.deconnecter();
            authentification();
        } else {
            // TODO: Implémenter les autres fonctionnalités
            System.out.println("Fonctionnalité à venir");
            menuAdministrateur();
        }
    }

    private static void menuEnseignant() {
        System.out.println("\n=== MENU ENSEIGNANT ===");
        System.out.println("1. Voir mes cours");
        System.out.println("2. Gérer mes classes");
        System.out.println("3. Saisir les notes");
        System.out.println("4. Déconnexion");
        System.out.print("Votre choix (1-4): ");

        int choix = scanner.nextInt();
        if (choix == 4) {
            authService.deconnecter();
            authentification();
        } else {
            // TODO: Implémenter les autres fonctionnalités
            System.out.println("Fonctionnalité à venir");
            menuEnseignant();
        }
    }

    private static void menuEleve() {
        System.out.println("\n=== MENU ÉLÈVE ===");
        System.out.println("1. Voir mon bulletin");
        System.out.println("2. Voir mes cours");
        System.out.println("3. Déconnexion");
        System.out.print("Votre choix (1-3): ");

        int choix = scanner.nextInt();
        if (choix == 3) {
            authService.deconnecter();
            authentification();
        } else {
            // TODO: Implémenter les autres fonctionnalités
            System.out.println("Fonctionnalité à venir");
            menuEleve();
        }
    }
}
