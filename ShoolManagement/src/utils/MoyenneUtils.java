package utils;

import model.Note;

import java.util.List;

public class MoyenneUtils {

    public static double calculerMoyenneGenerale(List<Note> notes) {
        double total = 0;
        int totalCoef = 0;
        for (Note note : notes) {
            double moyenne = note.getMoyenne();
            int coef = note.getCours().getCoefficient();
            total += moyenne * coef;
            totalCoef += coef;
        }
        return totalCoef == 0 ? 0 : total / totalCoef;
    }

    public static String getMention(double moyenne) {
        if (moyenne >= 16) return "Très Bien";
        if (moyenne >= 14) return "Bien";
        if (moyenne >= 12) return "Assez Bien";
        if (moyenne >= 10) return "Passable";
        return "Insuffisant";
    }
}
