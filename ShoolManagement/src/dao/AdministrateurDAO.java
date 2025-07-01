package dao;

import model.*;
import util.DatabaseConnection;

import java.sql.*;
import java.util.*;

public class AdministrateurDAO {
    public List<Administrateur> findAll() throws SQLException {
        String sql = "SELECT * FROM ADMINISTRATEUR";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            List<Administrateur> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new Administrateur(
                        rs.getInt("id_admin"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("login"),
                        rs.getString("mot_de_passe")));
            }
            return list;
        }
    }
}