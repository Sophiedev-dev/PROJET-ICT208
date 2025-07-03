package org.ruxlsr.dao;

import org.ruxlsr.utils.DatabaseConnection;

import javax.swing.table.DefaultTableModel;
import java.sql.*;


public class NoteDAO {



    public void getNotesParClasseEtCours(DefaultTableModel model, int classeId, int coursId, int trimestre) {
        String sql = """
        SELECT e.nom, n.note_cc, n.note_examen, n.moyenne
        FROM notes n
        JOIN eleves e ON n.eleve_id = e.id
        WHERE e.classe_id = ? AND n.cours_id = ? AND n.trimestre = ?
    """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, classeId);
            stmt.setInt(2, coursId);
            stmt.setInt(3, trimestre);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("nom"),
                        rs.getFloat("note_cc"),
                        rs.getFloat("note_examen"),
                        rs.getFloat("moyenne")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
