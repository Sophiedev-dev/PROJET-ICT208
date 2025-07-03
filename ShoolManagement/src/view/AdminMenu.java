package view;

import controller.AdminController;
import model.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class AdminMenu {
    private final AdminController controller = new AdminController();
    private final Scanner scanner = new Scanner(System.in);

    public void afficher(Utilisateur admin) {
        int choix;
        do {
            System.out.println("\n=== Menu Administrateur ===");
            System.out.println("1. Créer un niveau");
            System.out.println("2. Créer une classe");
            System.out.println("3. Créer un enseignant");
            System.out.println("4. Créer un cours");
            System.out.println("5. Inscrire un élève");
            System.out.println("0. Quitter");
            System.out.print("Choix : ");
            choix = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (choix) {
                    case 1 -> {
                        System.out.print("Nom du niveau : ");
                        controller.creerNiveau(scanner.nextLine());
                    }
                    case 2 -> {
                        List<Niveau> niveaux = controller.getTousLesNiveaux();
                        for (int i = 0; i < niveaux.size(); i++)
                            System.out.println((i + 1) + ". " + niveaux.get(i).getNom());
                        System.out.print("Choisir niveau : ");
                        Niveau niveau = niveaux.get(scanner.nextInt() - 1);
                        scanner.nextLine();
                        System.out.print("Nom de la classe : ");
                        controller.creerClasse(scanner.nextLine(), niveau);
                    }
                    case 3 -> {
                        System.out.print("Nom de l’enseignant : ");
                        controller.creerEnseignant(scanner.nextLine());
                    }
                    case 4 -> {
                        // Afficher les enseignants
                        List<Enseignant> enseignants = controller.getTousLesEnseignants();
                        for (int i = 0; i < enseignants.size(); i++)
                            System.out.println((i + 1) + ". " + enseignants.get(i).getNom());
                        System.out.print("Choisir enseignant : ");
                        Enseignant e = enseignants.get(scanner.nextInt() - 1);
                        scanner.nextLine();

                        // Afficher les classes disponibles
                        List<Classe> classes = controller.getToutesLesClasses();
                        for (int i = 0; i < classes.size(); i++)
                            System.out.println((i + 1) + ". " + classes.get(i).getNom());
                        System.out.print("Choisir classe : ");
                        Classe c = classes.get(scanner.nextInt() - 1);
                        scanner.nextLine();

                        // Demander les autres infos du cours
                        System.out.print("Nom du cours : ");
                        String nom = scanner.nextLine();
                        System.out.print("Coefficient : ");
                        int coef = scanner.nextInt();
                        scanner.nextLine();

                        // Créer le cours avec enseignant et classe
                        controller.creerCours(nom, coef, e, c);
                    }

                    case 5 -> {
                        List<Classe> classes = controller.getToutesLesClasses();
                        for (int i = 0; i < classes.size(); i++)
                            System.out.println((i + 1) + ". " + classes.get(i));
                        System.out.print("Choisir classe : ");
                        Classe c = classes.get(scanner.nextInt() - 1);
                        scanner.nextLine();
                        System.out.print("Nom élève : ");
                        String nom = scanner.nextLine();
                        System.out.print("Anonymat : ");
                        String idAnonymat = scanner.nextLine();
                        controller.inscrireEleve(nom, c, idAnonymat);
                    }
                }
            } catch (Exception e) {
                System.out.println("Erreur : " + e.getMessage());
            }

        } while (choix != 0);
    }
}
