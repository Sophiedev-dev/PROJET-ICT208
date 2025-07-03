package org.ruxlsr.view.console.admin;

import java.sql.SQLException;
import java.util.Scanner;

public class AdminConsole {
    private final Scanner scanner = new Scanner(System.in);

    public void afficherMenu() throws SQLException {
        while (true) {
            System.out.println("\n===== Menu Administrateur =====");
            System.out.println("1. Gérer les Niveaux");
            System.out.println("2. Gérer les Classes");
            System.out.println("3. Gérer les Enseignants");
            System.out.println("4. Gérer les Cours");
            System.out.println("5. Gérer les Élèves");
            System.out.println("6. Voir les Résultats");
            System.out.println("0. Retour au menu principal");
            System.out.print("Votre choix : ");
            String choix = scanner.nextLine();

            switch (choix) {
                case "1" -> new PanelNiveauxConsole().gerer();
                case "2" -> new PanelClassesConsole().gerer();
                case "3" -> new PanelEnseignantsConsole().gerer();
                case "4" -> new PanelCoursConsole().gerer();
                case "5" -> new PanelElevesConsole().gerer();
                case "6" -> new PanelResultatsAdminConsole().gerer();
                case "0" -> {
                    return;
                }
                default -> System.out.println("Choix invalide.");
            }
        }
    }
}
