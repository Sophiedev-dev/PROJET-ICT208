package dao;

import model.*;
import util.DatabaseConnection;

import java.sql.*;
import java.util.*;

public class TrimestreDAO {
    public List<Trimestre> findAll() throws SQLException {
        String sql = "SELECT * FROM TRIMESTRE";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            List<Trimestre> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new Trimestre(
                        rs.getInt("id_trimestre"),
                        rs.getInt("numero_trimestre"),
                        rs.getDate("date_debut"),
                        rs.getDate("date_fin"),
                        rs.getInt("annee_scolaire")));
            }
            return list;
        }
    }
}
