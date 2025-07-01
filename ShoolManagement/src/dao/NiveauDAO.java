package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class NiveauDAO {
    public List<Niveau> findAll() throws SQLException {
        String sql = "SELECT * FROM NIVEAU";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            List<Niveau> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new Niveau(
                        rs.getInt("id_niveau"),
                        rs.getString("nom_niveau"),
                        rs.getString("description")));
            }
            return list;
        }
    }
}