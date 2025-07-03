package org.ruxlsr.service;

import org.ruxlsr.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class NoteService {

    public float getNoteCC(int eleveId, int coursId, int trimestre) {
        String sql = "SELECT note_cc FROM notes WHERE eleve_id = ? AND cours_id = ? AND trimestre = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, eleveId);
            stmt.setInt(2, coursId);
            stmt.setInt(3, trimestre);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getFloat("note_cc");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0f;
    }

    public float getNoteExamen(int eleveId, int coursId, int trimestre) {
        String sql = "SELECT note_examen FROM notes WHERE eleve_id = ? AND cours_id = ? AND trimestre = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, eleveId);
            stmt.setInt(2, coursId);
            stmt.setInt(3, trimestre);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getFloat("note_examen");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0f;
    }
}

