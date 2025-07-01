package dao;

import model.*;
import util.DatabaseConnection;

import java.sql.*;
import java.util.*;

public class CoursDAO {
    public List<Cours> findAll() throws SQLException {
        String sql = "SELECT * FROM COURS";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            List<Cours> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new Cours(
                        rs.getInt("id_cours"),
                        rs.getString("nom_cours"),
                        rs.getInt("coefficient"),
                        rs.getString("description")));
            }
            return list;
        }
    }
}