package dao;

import model.*;

import java.sql.*;

public class BulletinDAO {

    public void insertOrUpdate(Bulletin bulletin) throws SQLException {
    String selectSql = "SELECT id FROM bulletins WHERE eleve_id = ? AND trimestre = ?";
    Connection conn = ConnexionDB.getConnexion();
    int existingId = -1;

    try (PreparedStatement stmt = conn.prepareStatement(selectSql)) {
        stmt.setInt(1, bulletin.getEleve().getId());
        stmt.setString(2, bulletin.getTrimestre().name());
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            existingId = rs.getInt("id");
        }
    }

    if (existingId != -1) {
        // Mise à jour
        String updateSql = "UPDATE bulletins SET moyenne = ?, rang = ?, mention = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(updateSql)) {
            stmt.setDouble(1, bulletin.getMoyenne());
            stmt.setInt(2, bulletin.getRang());
            stmt.setString(3, bulletin.getMention());
            stmt.setInt(4, existingId);
            stmt.executeUpdate();
        }
    } else {
        // Insertion
        String insertSql = "INSERT INTO bulletins (eleve_id, classe_id, trimestre, moyenne, rang, mention) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
            stmt.setInt(1, bulletin.getEleve().getId());
            stmt.setInt(2, bulletin.getClasse().getId());
            stmt.setString(3, bulletin.getTrimestre().name());
            stmt.setDouble(4, bulletin.getMoyenne());
            stmt.setInt(5, bulletin.getRang());
            stmt.setString(6, bulletin.getMention());
            stmt.executeUpdate();
        }
    }
}


    public Bulletin findByEleveAndTrimestre(int eleveId, Trimestre trimestre) throws SQLException {
        String sql = "SELECT * FROM bulletins WHERE eleve_id = ? AND trimestre = ?";
        try (PreparedStatement stmt = ConnexionDB.getConnexion().prepareStatement(sql)) {
            stmt.setInt(1, eleveId);
            stmt.setString(2, trimestre.name());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Eleve eleve = new EleveDAO().findById(eleveId);
                Classe classe = new ClasseDAO().findById(rs.getInt("classe_id"));
                return new Bulletin(
                    rs.getInt("id"),
                    eleve,
                    classe,
                    Trimestre.valueOf(rs.getString("trimestre")),
                    rs.getDouble("moyenne"),
                    rs.getInt("rang"),
                    rs.getString("mention")
                );
            }
        }
        return null;
    }
}
