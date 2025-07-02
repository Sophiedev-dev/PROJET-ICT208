package dao;

import model.*;

import java.sql.*;
import java.util.*;

public class NoteDAO {

    public void insert(Note note) throws SQLException {
        String sql = "INSERT INTO notes (eleve_id, cours_id, trimestre, note_cc, note_examen) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = ConnexionDB.getConnexion().prepareStatement(sql)) {
            stmt.setInt(1, note.getEleve().getId());
            stmt.setInt(2, note.getCours().getId());
            stmt.setString(3, note.getTrimestre().name());
            stmt.setDouble(4, note.getNoteCC());
            stmt.setDouble(5, note.getNoteExamen());
            stmt.executeUpdate();
        }
    }

    public void update(Note note) throws SQLException {
        String sql = "UPDATE notes SET note_cc = ?, note_examen = ? WHERE eleve_id = ? AND cours_id = ? AND trimestre = ?";
        try (PreparedStatement stmt = ConnexionDB.getConnexion().prepareStatement(sql)) {
            stmt.setDouble(1, note.getNoteCC());
            stmt.setDouble(2, note.getNoteExamen());
            stmt.setInt(3, note.getEleve().getId());
            stmt.setInt(4, note.getCours().getId());
            stmt.setString(5, note.getTrimestre().name());
            stmt.executeUpdate();
        }
    }

    public Note findNote(int eleveId, int coursId, Trimestre trimestre) throws SQLException {
        String sql = "SELECT * FROM notes WHERE eleve_id = ? AND cours_id = ? AND trimestre = ?";
        try (PreparedStatement stmt = ConnexionDB.getConnexion().prepareStatement(sql)) {
            stmt.setInt(1, eleveId);
            stmt.setInt(2, coursId);
            stmt.setString(3, trimestre.name());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Eleve eleve = new EleveDAO().findById(eleveId);
                Cours cours = new CoursDAO().findById(coursId);
                return new Note(
                    rs.getInt("id"),
                    eleve,
                    cours,
                    trimestre,
                    rs.getDouble("note_cc"),
                    rs.getDouble("note_examen")
                );
            }
        }
        return null;
    }

    public List<Note> findByEleveAndTrimestre(int eleveId, Trimestre trimestre) throws SQLException {
        List<Note> notes = new ArrayList<>();
        String sql = "SELECT * FROM notes WHERE eleve_id = ? AND trimestre = ?";
        try (PreparedStatement stmt = ConnexionDB.getConnexion().prepareStatement(sql)) {
            stmt.setInt(1, eleveId);
            stmt.setString(2, trimestre.name());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Eleve eleve = new EleveDAO().findById(eleveId);
                Cours cours = new CoursDAO().findById(rs.getInt("cours_id"));
                Note note = new Note(
                    rs.getInt("id"),
                    eleve,
                    cours,
                    trimestre,
                    rs.getDouble("note_cc"),
                    rs.getDouble("note_examen")
                );
                notes.add(note);
            }
        }
        return notes;
    }
}
