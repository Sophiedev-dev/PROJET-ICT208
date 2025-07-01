package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EnseignantDAO {
    public List<Enseignant> findAll() throws SQLException {
        String sql = "SELECT * FROM ENSEIGNANT";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            List<Enseignant> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new Enseignant(
                        rs.getInt("id_enseignant"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("telephone"),
                        rs.getString("specialite")));
            }
            return list;
        }
    }
}