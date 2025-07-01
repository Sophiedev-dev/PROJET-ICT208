package dao;

import utils.Database;
import models.VueMoyennesEleves;

import java.sql.*;
import java.util.*;

public class VueMoyennesElevesDAO {
    private final Connection c;

    public VueMoyennesElevesDAO() throws SQLException {
        this.c = Database.getConnection();
    }

    public List<VueMoyennesEleves> findByEleveTrimestre(int idEleve, int idTrimestre) throws SQLException {
        String sql = """
                    SELECT * FROM vue_moyennes_eleves
                    WHERE id_eleve = ? AND id_trimestre = ?
                """;
        try (PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setInt(1, idEleve);
            stmt.setInt(2, idTrimestre);
            ResultSet rs = stmt.executeQuery();
            var list = new ArrayList<VueMoyennesEleves>();
            while (rs.next()) {
                list.add(new VueMoyennesEleves(
                        rs.getInt("id_eleve"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getInt("id_trimestre"),
                        rs.getInt("id_cours"),
                        rs.getString("nom_cours"),
                        rs.getInt("coefficient"),
                        rs.getDouble("note_controle_continu"),
                        rs.getDouble("note_examen"),
                        rs.getDouble("moyenne_trimestre")));
            }
            return list;
        }
    }
}
