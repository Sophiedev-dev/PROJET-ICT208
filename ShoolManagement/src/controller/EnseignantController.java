package controller;

import dao.*;
import model.*;

import java.sql.SQLException;
import java.util.List;

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
}
