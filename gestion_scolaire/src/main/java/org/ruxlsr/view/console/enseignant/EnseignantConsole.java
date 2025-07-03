package org.ruxlsr.view.console.enseignant;

import java.util.Scanner;

public class EnseignantConsole {
    private final int enseignantId;
    private final Scanner scanner = new Scanner(System.in);

    public EnseignantConsole(int enseignantId) {
        this.enseignantId = enseignantId;
    }

    public void afficherMenu() {
        while (true) {
            System.out.println("\n=== Menu Enseignant ===");
            System.out.println("1. Voir mes cours & classes");
            System.out.println("2. Saisir notes CC");
            System.out.println("3. Saisir notes examen");
            System.out.println("4. Voir notes des élèves");
            System.out.println("0. Quitter");
            System.out.print("Votre choix : ");
            String choix = scanner.nextLine();

            switch (choix) {
                case "1" -> new ConsoleCoursClasses(enseignantId).afficher();
                case "2" -> new ConsoleSaisieCC(enseignantId).executer();
                case "3" -> new ConsoleSaisieExamen(enseignantId).executer();
                // case "4" -> new ConsoleVoirNotes(enseignantId).afficher();
                case "0" -> { return; }
                default -> System.out.println("Choix invalide.");
            }
        }
    }
}
