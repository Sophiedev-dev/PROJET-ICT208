package dao;

import model.*;
import util.DatabaseConnection;

import java.sql.*;
import java.util.*;

public class AnonymatDAO {
    public List<Anonymat> findAll() throws SQLException {
        String sql = "SELECT * FROM ANONYMAT";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            List<Anonymat> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new Anonymat(
                        rs.getInt("id_anonymat"),
                        rs.getString("code_anonymat"),
                        rs.getInt("id_eleve"),
                        rs.getInt("id_trimestre"),
                        rs.getInt("id_cours")));
            }
            return list;
        }
    }
}