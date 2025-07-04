package org.ruxlsr.dao;

import org.ruxlsr.utils.DatabaseConnection;

import javax.swing.table.DefaultTableModel;
import java.sql.*;


public class NoteDAO {

    public void getNotesParClasseEtCours(DefaultTableModel model, int classeId, int coursId, int trimestre) {
        String sql = """
        SELECT e.nom, n.note_cc, n.note_examen, n.moyenne
        FROM eleves e
        LEFT JOIN notes n ON e.id = n.eleve_id AND n.cours_id = ? AND n.trimestre = ?
        WHERE e.classe_id = ?
    """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, coursId);
            stmt.setInt(2, trimestre);
            stmt.setInt(3, classeId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("nom"),
                    rs.getObject("note_cc") != null ? rs.getFloat("note_cc") : "",
                    rs.getObject("note_examen") != null ? rs.getFloat("note_examen") : "",
                    rs.getObject("moyenne") != null ? rs.getFloat("moyenne") : ""
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saisirNoteCC(int eleveId, int coursId, int trimestre, float noteCC) {
        String select = "SELECT id FROM notes WHERE eleve_id = ? AND cours_id = ? AND trimestre = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(select)) {
            stmt.setInt(1, eleveId);
            stmt.setInt(2, coursId);
            stmt.setInt(3, trimestre);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String update = "UPDATE notes SET note_cc = ?, moyenne = (note_examen + ?) / 2 WHERE id = ?";
                try (PreparedStatement updateStmt = conn.prepareStatement(update)) {
                    updateStmt.setFloat(1, noteCC);
                    updateStmt.setFloat(2, noteCC);
                    updateStmt.setInt(3, rs.getInt("id"));
                    updateStmt.executeUpdate();
                }
            } else {
                String insert = "INSERT INTO notes (eleve_id, cours_id, trimestre, note_cc, moyenne) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insert)) {
                    insertStmt.setInt(1, eleveId);
                    insertStmt.setInt(2, coursId);
                    insertStmt.setInt(3, trimestre);
                    insertStmt.setFloat(4, noteCC);
                    insertStmt.setFloat(5, noteCC);
                    insertStmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saisirNoteExamenParAnonymat(String anonymat, int coursId, int trimestre, float note) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = """
            SELECT notes.id, notes.note_cc FROM notes 
            JOIN eleves ON notes.eleve_id = eleves.id 
            WHERE eleves.id_anonymat = ? AND notes.cours_id = ? AND notes.trimestre = ?
        """;
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, anonymat);
            stmt.setInt(2, coursId);
            stmt.setInt(3, trimestre);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                float noteCC = rs.getFloat("note_cc");
                int noteId = rs.getInt("id");
                float moyenne = (noteCC + note) / 2;

                String update = "UPDATE notes SET note_examen = ?, moyenne = ? WHERE id = ?";
                PreparedStatement updateStmt = conn.prepareStatement(update);
                updateStmt.setFloat(1, note);
                updateStmt.setFloat(2, moyenne);
                updateStmt.setInt(3, noteId);
                updateStmt.executeUpdate();
            } else {
                String getEleveId = "SELECT id FROM eleves WHERE id_anonymat = ?";
                PreparedStatement eleveStmt = conn.prepareStatement(getEleveId);
                eleveStmt.setString(1, anonymat);
                ResultSet eleveRs = eleveStmt.executeQuery();
                if (eleveRs.next()) {
                    int eleveId = eleveRs.getInt("id");
                    String insert = "INSERT INTO notes (eleve_id, cours_id, trimestre, note_examen, moyenne) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement insertStmt = conn.prepareStatement(insert);
                    insertStmt.setInt(1, eleveId);
                    insertStmt.setInt(2, coursId);
                    insertStmt.setInt(3, trimestre);
                    insertStmt.setFloat(4, note);
                    insertStmt.setFloat(5, note);
                    insertStmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Float getNoteCC(int eleveId, int coursId, int trimestre) {
        String sql = "SELECT note_cc FROM notes WHERE eleve_id = ? AND cours_id = ? AND trimestre = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, eleveId);
            stmt.setInt(2, coursId);
            stmt.setInt(3, trimestre);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getFloat("note_cc");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Float getNoteExamenParAnonymat(String anonymat, int coursId, int trimestre) {
        String sql = """
        SELECT note_examen FROM notes 
        JOIN eleves ON notes.eleve_id = eleves.id 
        WHERE eleves.id_anonymat = ? AND cours_id = ? AND trimestre = ?
    """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, anonymat);
            stmt.setInt(2, coursId);
            stmt.setInt(3, trimestre);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getFloat("note_examen");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
