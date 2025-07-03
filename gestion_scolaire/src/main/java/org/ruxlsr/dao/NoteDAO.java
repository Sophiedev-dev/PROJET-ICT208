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

}
