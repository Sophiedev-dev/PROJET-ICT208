package dao;

import utils.Database;
import models.VueEffectifsClasses;
import java.sql.*;
import java.util.*;

public class VueEffectifsClassesDAO {
    private final Connection c;

    public VueEffectifsClassesDAO() throws SQLException {
        this.c = Database.getConnection();
    }

    public List<VueEffectifsClasses> findAll() throws SQLException {
        String sql = "SELECT * FROM vue_effectifs_classes";
        try (PreparedStatement stmt = c.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            var list = new ArrayList<VueEffectifsClasses>();
            while (rs.next()) {
                list.add(new VueEffectifsClasses(
                        rs.getInt("id_classe"),
                        rs.getString("nom_classe"),
                        rs.getString("nom_niveau"),
                        rs.getInt("effectif")));
            }
            return list;
        }
    }
}
