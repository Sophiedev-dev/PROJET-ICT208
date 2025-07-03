package org.ruxlsr.view.console.admin;

import org.ruxlsr.model.Classe;
import org.ruxlsr.model.Eleve;
import org.ruxlsr.service.AdminService;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class PanelElevesConsole {
    private final Scanner scanner = new Scanner(System.in);
    private final AdminService service = new AdminService();

    public void gerer() throws SQLException {
        while (true) {
            System.out.println("\n=== Gestion des Élèves ===");
            System.out.println("1. Lister les élèves");
            System.out.println("2. Ajouter un élève");
            System.out.println("3. Supprimer un élève");
            System.out.println("4. Modifier un élève");
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
        List<Eleve> eleves = service.listerEleves();
        System.out.println("ID\tNom\tClasseID\tAnonymat");
        for (Eleve e : eleves) {
            System.out.printf("%d\t%s\t%d\t%s%n", e.getId(), e.getNom(), e.getClasseId(), e.getIdAnonymat());
        }
    }

    private void ajouter() throws SQLException {
        System.out.print("Nom de l'élève : ");
        String nom = scanner.nextLine();
        List<Classe> classes = service.listerClasses();
        for (int i = 0; i < classes.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, classes.get(i).getNom());
        }
        System.out.print("Choisir une classe : ");
        int index = Integer.parseInt(scanner.nextLine()) - 1;
        service.creerEleveAvecAnonymat(nom, classes.get(index).getId());
        System.out.println("Élève ajouté.");
    }

    private void supprimer() {
        System.out.print("ID de l’élève à supprimer : ");
        int id = Integer.parseInt(scanner.nextLine());
        service.supprimerEleveParId(id);
        System.out.println("Élève supprimé.");
    }

    private void modifier() {
        System.out.print("ID de l’élève à modifier : ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("Nouveau nom : ");
        String nom = scanner.nextLine();
        System.out.print("Nouvelle classe ID : ");
        int classeId = Integer.parseInt(scanner.nextLine());
        service.modifierEleve(id, nom, classeId);
        System.out.println("Élève modifié.");
    }
}
