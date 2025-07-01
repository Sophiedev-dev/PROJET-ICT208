package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EnseignerDAO {
    public List<Enseigner> findAll() throws SQLException {
        String sql = "SELECT * FROM ENSEIGNER";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            List<Enseigner> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new Enseigner(
                        rs.getInt("id_enseignant"),
                        rs.getInt("id_cours"),
                        rs.getInt("id_classe")));
            }
            return list;
        }
    }
}