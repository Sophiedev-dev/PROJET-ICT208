package utils;

import model.*;

import java.util.List;

public class BulletinPrinter {

    public static void printBulletin(Eleve eleve, Trimestre trimestre, List<Note> notes) {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║          BULLETIN DE NOTES            ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("Nom     : " + eleve.getNom());
        System.out.println("Classe  : " + eleve.getClasse().getNom());
        System.out.println("Trimestre : " + trimestre.name());

        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║ Matière       CC   Exam   Moy   Coef   ║");
        System.out.println("╠════════════════════════════════════════╣");

        double total = 0;
        int totalCoef = 0;

        for (Note note : notes) {
            double moyenne = note.getMoyenne();
            int coef = note.getCours().getCoefficient();
            total += moyenne * coef;
            totalCoef += coef;
            System.out.printf("║ %-12s  %.2f  %.2f  %.2f    %d     ║\n",
                    note.getCours().getNom(), note.getNoteCC(), note.getNoteExamen(), moyenne, coef);
        }

        double moyenneGenerale = totalCoef == 0 ? 0 : total / totalCoef;
        String mention = MoyenneUtils.getMention(moyenneGenerale);

        System.out.println("╚════════════════════════════════════════╝");
        System.out.printf("Moyenne Générale : %.2f\n", moyenneGenerale);
        System.out.println("Mention : " + mention);
        System.out.println("══════════════════════════════════════════");
    }
}
