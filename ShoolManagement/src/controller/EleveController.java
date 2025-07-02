package controller;

import dao.*;
import model.*;

import java.sql.SQLException;
import java.util.*;

public class EleveController {

    private NoteDAO noteDAO = new NoteDAO();
    private CoursDAO coursDAO = new CoursDAO();

    public List<Note> getNotesParTrimestre(Eleve eleve, Trimestre trimestre) throws SQLException {
        return noteDAO.findByEleveAndTrimestre(eleve.getId(), trimestre);
    }

    public double calculerMoyenneGenerale(Eleve eleve, Trimestre trimestre) throws SQLException {
        List<Note> notes = getNotesParTrimestre(eleve, trimestre);
        double total = 0;
        int totalCoef = 0;

        for (Note note : notes) {
            double moy = note.getMoyenne();
            int coef = note.getCours().getCoefficient();
            total += moy * coef;
            totalCoef += coef;
        }
        return totalCoef == 0 ? 0 : total / totalCoef;
    }

    public String getMention(double moyenne) {
        if (moyenne >= 16) return "Très Bien";
        if (moyenne >= 14) return "Bien";
        if (moyenne >= 12) return "Assez Bien";
        if (moyenne >= 10) return "Passable";
        return "Insuffisant";
    }
}
