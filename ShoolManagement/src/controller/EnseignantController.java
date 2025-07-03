package controller;

import dao.*;
import model.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnseignantController {

    private NoteDAO noteDAO = new NoteDAO();
    private CoursDAO coursDAO = new CoursDAO();
    private EleveDAO eleveDAO = new EleveDAO();

    public List<Cours> getCoursParEnseignant(Enseignant enseignant) throws SQLException {
        return coursDAO.findByEnseignant(enseignant.getId());
    }

    public List<Eleve> getElevesDeClasse(int classeId) throws SQLException {
        return eleveDAO.findByClasse(classeId);
    }

    public void saisirNoteCC(Eleve eleve, Cours cours, Trimestre trimestre, double noteCC) throws SQLException {
        Note note = noteDAO.findNote(eleve.getId(), cours.getId(), trimestre);
        if (note == null) {
            note = new Note(0, eleve, cours, trimestre, noteCC, 0.0);
            noteDAO.insert(note);
        } else {
            note.setNoteCC(noteCC);
            noteDAO.update(note);
        }
    }

    public void saisirNoteExamen(String idAnonymat, Cours cours, Trimestre trimestre, double noteExam) throws SQLException {
        Eleve eleve = eleveDAO.findByAnonymat(idAnonymat);
        if (eleve == null) throw new SQLException("Aucun élève avec cet anonymat.");
        Note note = noteDAO.findNote(eleve.getId(), cours.getId(), trimestre);
        if (note == null) {
            note = new Note(0, eleve, cours, trimestre, 0.0, noteExam);
            noteDAO.insert(note);
        } else {
            note.setNoteExamen(noteExam);
            noteDAO.update(note);
        }
    }

    public void genererBulletin(int classeId, Trimestre trimestre) throws SQLException {
    List<Eleve> eleves = eleveDAO.findByClasse(classeId);
    List<Cours> coursList = coursDAO.findByClasse(classeId);

    Map<Eleve, Double> moyennesMap = new HashMap<>();

    for (Eleve eleve : eleves) {
        double totalNotes = 0;
        int totalCoef = 0;

        for (Cours cours : coursList) {
            Note note = noteDAO.findNote(eleve.getId(), cours.getId(), trimestre);
            if (note != null) {
                double moyenneCours = (note.getNoteCC() + note.getNoteExamen()) / 2;
                totalNotes += moyenneCours * cours.getCoefficient();
                totalCoef += cours.getCoefficient();
            }
        }

        double moyenne = totalCoef > 0 ? totalNotes / totalCoef : 0.0;
        moyennesMap.put(eleve, moyenne);
    }

    // Tri par moyenne pour déterminer le rang
    List<Map.Entry<Eleve, Double>> entries = new ArrayList<>(moyennesMap.entrySet());
    entries.sort((a, b) -> Double.compare(b.getValue(), a.getValue())); // décroissant

    // Insertions dans la table bulletin
    for (int i = 0; i < entries.size(); i++) {
        Eleve eleve = entries.get(i).getKey();
        double moyenne = entries.get(i).getValue();
        int rang = i + 1;
        String mention = calculerMention(moyenne);

        new BulletinDAO().insertOrUpdate(new Bulletin(0, eleve, eleve.getClasse(), trimestre, moyenne, rang, mention));
    }

    System.out.println("✅ Bulletins générés pour la classe ID " + classeId + ", trimestre " + trimestre.name());
}

private String calculerMention(double moyenne) {
    if (moyenne >= 16) return "Très Bien";
    if (moyenne >= 14) return "Bien";
    if (moyenne >= 12) return "Assez Bien";
    if (moyenne >= 10) return "Passable";
    return "Insuffisant";
}



    public Note getNote(Eleve eleve, Cours cours, Trimestre trimestre) throws SQLException {
    return noteDAO.findNote(eleve.getId(), cours.getId(), trimestre);
}

}
