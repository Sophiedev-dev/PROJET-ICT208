package org.ruxlsr.dao;

import org.ruxlsr.model.Cours;
import org.ruxlsr.model.Niveau;
import org.ruxlsr.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CoursDAO {
    public List<Cours> getAll() throws SQLException {
        List<Cours> cours = new ArrayList<>();
        String sql = "SELECT * FROM cours";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                cours.add(new Cours(rs.getInt("id"), rs.getString("nom"), rs.getInt("coefficient"),rs.getInt("enseignant_id")));
            }
        }
        return cours;
    }
}
