package org.ruxlsr.dao;

import org.ruxlsr.utils.DatabaseConnection;

import java.sql.*;


public class NoteDAO {

    public void afficherNotesParClasseEtCours(int classeId, int coursId, int trimestre) {
        String sql = """
            SELECT e.nom AS eleve, n.note_cc, n.note_examen, n.moyenne
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
                System.out.printf("Élève : %s | CC : %.2f | Examen : %.2f | Moy : %.2f\n",
                        rs.getString("eleve"),
                        rs.getFloat("note_cc"),
                        rs.getFloat("note_examen"),
                        rs.getFloat("moyenne"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
