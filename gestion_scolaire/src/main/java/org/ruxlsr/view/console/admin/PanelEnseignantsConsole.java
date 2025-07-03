package org.ruxlsr.view.console.admin;

import org.ruxlsr.model.Enseignant;
import org.ruxlsr.service.AdminService;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class PanelEnseignantsConsole {
    private final Scanner scanner = new Scanner(System.in);
    private final AdminService service = new AdminService();

    public void gerer() throws SQLException {
        while (true) {
            System.out.println("\n=== Gestion des Enseignants ===");
            System.out.println("1. Lister les enseignants");
            System.out.println("2. Ajouter un enseignant");
            System.out.println("3. Supprimer un enseignant");
            System.out.println("4. Modifier un enseignant");
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
        List<Enseignant> list = service.listerEnseignants();
        System.out.println("ID\tNom");
        for (Enseignant e : list) {
            System.out.printf("%d\t%s%n", e.getId(), e.getNom());
        }
    }

    private void ajouter() {
        System.out.print("Nom de l'enseignant : ");
        String nom = scanner.nextLine();
        if (nom.isBlank()) {
            System.out.println("Nom invalide.");
            return;
        }
        service.creerEnseignant(nom);
        System.out.println("Enseignant ajouté.");
    }

    private void supprimer() {
        System.out.print("ID de l’enseignant à supprimer : ");
        int id = Integer.parseInt(scanner.nextLine());
        service.supprimerEnseignantParId(id);
        System.out.println("Enseignant supprimé.");
    }

    private void modifier() {
        System.out.print("ID de l’enseignant à modifier : ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("Nouveau nom : ");
        String nom = scanner.nextLine();
        service.modifierNomEnseignant(id, nom);
        System.out.println("Nom modifié.");
    }
}
