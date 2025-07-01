package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class NoteDAO {
    public List<Note> findAll() throws SQLException {
        String sql = "SELECT * FROM NOTE";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            List<Note> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new Note(
                        rs.getInt("id_note"),
                        rs.getBigDecimal("note_controle_continu"),
                        rs.getBigDecimal("note_examen"),
                        rs.getInt("id_eleve"),
                        rs.getInt("id_cours"),
                        rs.getInt("id_trimestre")));
            }
            return list;
        }
    }
}