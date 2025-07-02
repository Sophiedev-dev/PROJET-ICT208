package view;

import controller.EleveController;
import model.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class EleveMenu {
    private final EleveController controller = new EleveController();
    private final Scanner scanner = new Scanner(System.in);

    public void afficher(Utilisateur utilisateur) {
        Eleve eleve = utilisateur.getEleve();

        System.out.println("\n=== Menu Élève ===");
        System.out.println("Bulletin pour quel trimestre ? (1-3)");
        int t = scanner.nextInt(); scanner.nextLine();
        Trimestre trimestre = Trimestre.values()[t - 1];

        try {
            List<Note> notes = controller.getNotesParTrimestre(eleve, trimestre);
            double moyenne = controller.calculerMoyenneGenerale(eleve, trimestre);
            String mention = controller.getMention(moyenne);

            System.out.println("Nom : " + eleve.getNom());
            System.out.println("Classe : " + eleve.getClasse().getNom());
            System.out.println("Trimestre : " + trimestre.name());
            System.out.println("Notes :");
            for (Note note : notes) {
                System.out.println(note.getCours().getNom() + ": CC=" + note.getNoteCC() +
                    ", Examen=" + note.getNoteExamen() +
                    ", Moyenne=" + note.getMoyenne());
            }
            System.out.println("Moyenne générale : " + moyenne);
            System.out.println("Mention : " + mention);

        } catch (SQLException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }
}
