package org.ruxlsr.view.console.admin;

import org.ruxlsr.model.Classe;
import org.ruxlsr.model.Cours;
import org.ruxlsr.model.Enseignant;
import org.ruxlsr.service.AdminService;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class PanelCoursConsole {
    private final Scanner scanner = new Scanner(System.in);
    private final AdminService service = new AdminService();

    public void gerer() throws SQLException {
        while (true) {
            System.out.println("\n=== Gestion des Cours ===");
            System.out.println("1. Lister les cours");
            System.out.println("2. Ajouter un cours");
            System.out.println("3. Supprimer un cours");
            System.out.println("4. Modifier un cours");
            System.out.println("0. Retour");
            System.out.print("Votre choix : ");
            String choix = scanner.nextLine();

            switch (choix) {
                case "1" -> lister();
                case "2" -> ajouter();
                case "3" -> supprimer();
                case "4" -> modifier();
                case "0" -> { return; }
                default -> System.out.println("Choix invalide.");
            }
        }
    }

    private void lister() throws SQLException {
        List<Cours> coursList = service.listerCours();
        System.out.println("ID\tNom\tCoef\tEnseignant\tClasse");
        for (Cours c : coursList) {
            String enseignant = service.getNomEnseignantById(c.getEnseignantId());
            String classe = service.getNomClasseByCoursId(c.getId());
            System.out.printf("%d\t%s\t%d\t%s\t%s%n", c.getId(), c.getNom(), c.getCoefficient(), enseignant, classe);
        }
    }

    private void ajouter() throws SQLException {
        System.out.print("Nom du cours : ");
        String nom = scanner.nextLine();
        System.out.print("Coefficient : ");
        int coef = Integer.parseInt(scanner.nextLine());

        List<Enseignant> enseignants = service.listerEnseignants();
        for (int i = 0; i < enseignants.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, enseignants.get(i).getNom());
        }
        System.out.print("Choisir un enseignant : ");
        int ensIndex = Integer.parseInt(scanner.nextLine()) - 1;

        List<Classe> classes = service.listerClasses();
        for (int i = 0; i < classes.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, classes.get(i).getNom());
        }
        System.out.print("Choisir une classe : ");
        int clsIndex = Integer.parseInt(scanner.nextLine()) - 1;

        int coursId = service.creerCoursEtRetournerId(nom, coef, enseignants.get(ensIndex).getId());
        service.associerCoursAClasse(coursId, classes.get(clsIndex).getId());

        System.out.println("Cours ajouté.");
    }

    private void supprimer() {
        System.out.print("ID du cours à supprimer : ");
        int id = Integer.parseInt(scanner.nextLine());
        service.supprimerCoursParId(id);
        System.out.println("Cours supprimé.");
    }

    private void modifier() {
        System.out.print("ID du cours à modifier : ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("Nouveau nom : ");
        String nom = scanner.nextLine();
        System.out.print("Nouveau coef : ");
        int coef = Integer.parseInt(scanner.nextLine());

        System.out.print("Nom de l’enseignant (exactement) : ");
        String nomEns = scanner.nextLine();
        int enseignantId = service.getIdEnseignantByNom(nomEns);

        service.modifierCours(id, nom, coef, enseignantId);
        System.out.println("Cours modifié.");
    }
}
