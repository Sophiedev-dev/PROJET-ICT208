package org.ruxlsr.view.console;

import org.ruxlsr.service.AdminService;
import org.ruxlsr.view.console.admin.AdminConsole;
// import org.ruxlsr.view.console.eleve.EleveConsole;
// import org.ruxlsr.view.console.enseignant.EnseignantConsole;
import org.ruxlsr.view.console.eleve.EleveConsole;
import org.ruxlsr.view.console.enseignant.EnseignantConsole;

import java.sql.SQLException;
import java.util.Scanner;

public class MainConsole {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws SQLException {
        while (true) {
            System.out.println("Choisir un rôle à simuler :");
            System.out.println("1. ADMINISTRATEUR");
            System.out.println("2. ENSEIGNANT");
            System.out.println("3. ELEVE");
            System.out.println("0. Quitter");
            System.out.print("Votre choix : ");
            String choix = scanner.nextLine();

            switch (choix) {
                case "1" -> new AdminConsole().afficherMenu();
                case "2" -> {
                    System.out.print("ID de l'enseignant : ");
                    int id = Integer.parseInt(scanner.nextLine());
                    new EnseignantConsole(id).afficherMenu();
                }
                case "3" -> {
                    System.out.print("ID de l'élève : ");
                    int id = Integer.parseInt(scanner.nextLine());
                    new EleveConsole(id).afficherBulletin();
                }
                case "0" -> {
                    System.out.println("Au revoir !");
                    return;
                }
                default -> System.out.println("Choix invalide. Réessayez.");
            }
        }
    }
}
