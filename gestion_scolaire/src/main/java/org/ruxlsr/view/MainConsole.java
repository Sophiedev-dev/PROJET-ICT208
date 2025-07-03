package org.ruxlsr.view;

import org.ruxlsr.dao.UtilisateurDAO;
import org.ruxlsr.model.Utilisateur;
import org.ruxlsr.service.AdminService;
import org.ruxlsr.service.EleveService;
import org.ruxlsr.service.EnseignantService;

import java.util.Scanner;

public class MainConsole {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

        System.out.println("=== Connexion ===");
        System.out.print("Identifiant : ");
        String identifiant = scanner.nextLine();

        System.out.print("Mot de passe : ");
        String motDePasse = scanner.nextLine();

        Utilisateur user = utilisateurDAO.login(identifiant, motDePasse);

        if (user == null) {
            System.out.println("Erreur : Identifiants invalides.");
            return;
        }

        System.out.println("Connecté en tant que : " + user.getRole());

        switch (user.getRole()) {
            case "ADMINISTRATEUR" -> menuAdmin();
            case "ENSEIGNANT" -> menuEnseignant(user.getEnseignantId());
            case "ELEVE" -> menuEleve(user.getEleveId());
            default -> System.out.println("Rôle inconnu.");
        }
    }

    private static void menuAdmin() {
        AdminService adminService = new AdminService();

        while (true) {
            System.out.println("\n--- Menu Administrateur ---");
            System.out.println("1. Créer un niveau");
            System.out.println("2. Créer une classe");
            System.out.println("3. Créer un enseignant");
            System.out.println("4. Créer un cours");
            System.out.println("5. Créer un élève");
            System.out.println("6. Générer un anonymat");
            System.out.println("0. Quitter");

            System.out.print("Choix : ");
            int choix = scanner.nextInt();
            scanner.nextLine(); // flush

            try {
                switch (choix) {
                    case 1 -> {
                        System.out.print("Nom du niveau : ");
                        adminService.creerNiveau(scanner.nextLine());
                    }
                    case 2 -> {
                        System.out.print("Nom de la classe : ");
                        String nomClasse = scanner.nextLine();
                        System.out.print("ID du niveau : ");
                        int niveauId = scanner.nextInt(); scanner.nextLine();
                        adminService.creerClasse(nomClasse, niveauId);
                    }
                    case 3 -> {
                        System.out.print("Nom de l'enseignant : ");
                        adminService.creerEnseignant(scanner.nextLine());
                    }
                    case 4 -> {
                        System.out.print("Nom du cours : ");
                        String nom = scanner.nextLine();
                        System.out.print("Coefficient : ");
                        int coef = scanner.nextInt();
                        System.out.print("ID enseignant : ");
                        int idEns = scanner.nextInt(); scanner.nextLine();
                        adminService.creerCours(nom, coef, idEns);
                    }
                    case 5 -> {
                        System.out.print("Nom de l’élève : ");
                        String nom = scanner.nextLine();
                        System.out.print("ID de la classe : ");
                        int classeId = scanner.nextInt(); scanner.nextLine();
                        adminService.creerEleve(nom, classeId);
                    }
                    case 6 -> {
                        System.out.print("ID de l’élève : ");
                        int id = scanner.nextInt(); scanner.nextLine();
                        System.out.print("Nouvel ID d’anonymat : ");
                        adminService.genererAnonymat(id, scanner.nextLine());
                    }
                    case 0 -> { System.out.println("Au revoir."); return; }
                    default -> System.out.println("Choix invalide.");
                }
            } catch (Exception e) {
                System.out.println("Erreur : " + e.getMessage());
            }
        }
    }

    private static void menuEnseignant(int enseignantId) {
        EnseignantService service = new EnseignantService();

        while (true) {
            System.out.println("\n--- Menu Enseignant ---");
            System.out.println("1. Voir mes cours et classes");
            System.out.println("2. Saisir note de contrôle continu");
            System.out.println("3. Saisir note d’examen (via anonymat)");
            System.out.println("0. Quitter");

            System.out.print("Choix : ");
            int choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1 -> service.afficherCoursEtClasses(enseignantId);
                case 2 -> {
                    System.out.print("ID élève : ");
                    int eleveId = scanner.nextInt();
                    System.out.print("ID cours : ");
                    int coursId = scanner.nextInt();
                    System.out.print("Trimestre (1-3) : ");
                    int trimestre = scanner.nextInt();
                    System.out.print("Note CC : ");
                    float note = scanner.nextFloat(); scanner.nextLine();
                    service.saisirNoteCC(eleveId, coursId, trimestre, note);
                }
                case 3 -> {
                    System.out.print("ID anonymat élève : ");
                    String anonymat = scanner.nextLine();
                    System.out.print("ID cours : ");
                    int coursId = scanner.nextInt();
                    System.out.print("Trimestre (1-3) : ");
                    int trimestre = scanner.nextInt();
                    System.out.print("Note Examen : ");
                    float note = scanner.nextFloat(); scanner.nextLine();
                    service.saisirNoteExamen(anonymat, coursId, trimestre, note);
                }
                case 0 -> { System.out.println("Déconnexion..."); return; }
                default -> System.out.println("Choix invalide.");
            }
        }
    }

    private static void menuEleve(int eleveId) {
        EleveService eleveService = new EleveService();

        while (true) {
            System.out.println("\n--- Menu Élève ---");
            System.out.println("1. Afficher bulletin d’un trimestre");
            System.out.println("0. Quitter");

            System.out.print("Choix : ");
            int choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1 -> {
                    System.out.print("Trimestre (1-3) : ");
                    int trim = scanner.nextInt(); scanner.nextLine();
                    eleveService.afficherBulletin(eleveId, trim);
                }
                case 0 -> { System.out.println("Déconnexion..."); return; }
                default -> System.out.println("Choix invalide.");
            }
        }
    }
}
