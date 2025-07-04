package org.ruxlsr.service;

import org.ruxlsr.dao.CoursClasseDAO;
import org.ruxlsr.dao.CoursDAO;
import org.ruxlsr.dao.EleveDAO;
import org.ruxlsr.dao.NoteDAO;
import org.ruxlsr.model.Cours;
import org.ruxlsr.model.Eleve;

import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Set;

public class EnseignantService {

    private final NoteDAO noteDAO = new NoteDAO();
    private final CoursDAO coursDAO = new CoursDAO();
    private final CoursClasseDAO coursClasseDAO = new CoursClasseDAO();
    private final EleveDAO eleveDAO = new EleveDAO();

    public void afficherCoursEtClasses(int enseignantId) {
        coursClasseDAO.afficherCoursEtClassesPourEnseignant(enseignantId);
    }

    public void saisirNoteCC(int eleveId, int coursId, int trimestre, float noteCC) {
        noteDAO.saisirNoteCC(eleveId, coursId, trimestre, noteCC);
    }

    public void saisirNoteExamen(String anonymat, int coursId, int trimestre, float note) {
        noteDAO.saisirNoteExamenParAnonymat(anonymat, coursId, trimestre, note);
    }

    public List<Cours> getCoursByEnseignant(int enseignantId) {
        return coursDAO.getByEnseignant(enseignantId);
    }

    public int getClasseIdForCours(int coursId) {
        return coursClasseDAO.getClasseIdByCoursId(coursId);
    }

    public List<Eleve> getElevesByClasse(int classeId) {
        return eleveDAO.getByClasse(classeId);
    }

    public Set<Integer> getClasseIdsByEnseignant(int enseignantId) {
        return coursClasseDAO.getClasseIdsByEnseignant(enseignantId);
    }

    public void getNoteParclasseEtCours(DefaultTableModel model, int classeId, int coursId, int trimestre) {
        noteDAO.getNotesParClasseEtCours(model, classeId, coursId, trimestre);
    }

    public Float getNoteCC(int eleveId, int coursId, int trimestre) {
        return noteDAO.getNoteCC(eleveId, coursId, trimestre);
    }

    public Float getNoteExamen(String anonymat, int coursId, int trimestre) {
        return noteDAO.getNoteExamenParAnonymat(anonymat, coursId, trimestre);
    }

    public void getCoursClasseNomsById(int enseignantId, DefaultTableModel model) {
        coursClasseDAO.remplirCoursEtClassesPourEnseignant(enseignantId, model);
    }
}
