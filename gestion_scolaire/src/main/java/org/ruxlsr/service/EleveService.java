package org.ruxlsr.service;

import org.ruxlsr.dao.EleveDAO;
import org.ruxlsr.utils.DatabaseConnection;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class EleveService {
    private final EleveDAO eleveDAO = new EleveDAO();

    private int getClasseId(Connection conn, int eleveId) throws SQLException {
        String sql = "SELECT classe_id FROM eleves WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, eleveId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("classe_id");
        }
        return -1;
    }

    public String getMention(float moyenne) {
        if (moyenne >= 16) return "Très Bien";
        if (moyenne >= 14) return "Bien";
        if (moyenne >= 12) return "Assez Bien";
        if (moyenne >= 10) return "Passable";
        return "Insuffisant";
    }



    public String getNomEleve(int eleveId) {
        return eleveDAO.getNomById(eleveId);
    }

    public float remplirBulletin(int eleveId, int trimestre, DefaultTableModel model) {
        return eleveDAO.remplirBulletin(eleveId, trimestre, model);
    }


    public String getRang(int eleveId, int trimestre) {
        return eleveDAO.getRang(eleveId, trimestre);
    }
}
