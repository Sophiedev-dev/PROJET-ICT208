package org.ruxlsr.view.console.admin;

import org.ruxlsr.model.Niveau;
import org.ruxlsr.service.AdminService;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class PanelNiveauxConsole {
    private final Scanner scanner = new Scanner(System.in);
    private final AdminService service = new AdminService();

    public void gerer() throws SQLException {
        while (true) {
            System.out.println("\n=== Gestion des Niveaux ===");
            System.out.println("1. Lister les niveaux");
            System.out.println("2. Ajouter un niveau");
            System.out.println("3. Supprimer un niveau");
            System.out.println("4. Modifier un niveau");
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
        List<Niveau> niveaux = service.listerNiveaux();
        System.out.println("ID\tNom");
        for (Niveau n : niveaux) {
            System.out.printf("%d\t%s%n", n.getId(), n.getNom());
        }
    }

    private void ajouter() throws SQLException {
        System.out.print("Nom du niveau : ");
        String nom = scanner.nextLine();
        if (nom.isBlank()) {
            System.out.println("Entrée invalide.");
            return;
        }
        service.creerNiveau(nom);
        System.out.println("Niveau ajouté avec succès.");
    }

    private void supprimer() {
        System.out.print("ID du niveau à supprimer : ");
        int id = Integer.parseInt(scanner.nextLine());
        service.supprimerNiveauParIdCascade(id);
        System.out.println("Niveau supprimé.");
    }

    private void modifier() {
        System.out.print("ID du niveau à modifier : ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("Nouveau nom : ");
        String nom = scanner.nextLine();
        service.modifierNomNiveau(id, nom);
        System.out.println("Nom modifié.");
    }
}
