package dao;

import utils.Database;
import models.VueEnseignantsClasses;
import java.sql.*;
import java.util.*;

public class VueEnseignantsClassesDAO {
    private final Connection c;

    public VueEnseignantsClassesDAO() throws SQLException {
        this.c = Database.getConnection();
    }

    public List<VueEnseignantsClasses> findAll() throws SQLException {
        String sql = "SELECT * FROM vue_enseignants_classes";
        try (PreparedStatement stmt = c.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            var list = new ArrayList<VueEnseignantsClasses>();
            while (rs.next()) {
                list.add(new VueEnseignantsClasses(
                        rs.getInt("id_enseignant"),
                        rs.getString("nom_enseignant"),
                        rs.getString("prenom_enseignant"),
                        rs.getString("nom_cours"),
                        rs.getString("nom_classe"),
                        rs.getString("nom_niveau")));
            }
            return list;
        }
    }
}
