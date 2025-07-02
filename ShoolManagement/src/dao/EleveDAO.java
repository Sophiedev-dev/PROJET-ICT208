package dao;

import model.Classe;
import model.Eleve;
import java.sql.*;
import java.util.*;

public class EleveDAO {

    public Eleve findById(int id) throws SQLException {
        String sql = "SELECT * FROM eleves WHERE id = ?";
        try (PreparedStatement stmt = ConnexionDB.getConnexion().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Classe c = new ClasseDAO().findById(rs.getInt("classe_id"));
                return new Eleve(id, rs.getString("nom"), c, rs.getString("id_anonymat"));
            }
        }
        return null;
    }

    public List<Eleve> findByClasse(int classeId) throws SQLException {
        List<Eleve> list = new ArrayList<>();
        String sql = "SELECT * FROM eleves WHERE classe_id = ?";
        try (PreparedStatement stmt = ConnexionDB.getConnexion().prepareStatement(sql)) {
            stmt.setInt(1, classeId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Classe c = new ClasseDAO().findById(classeId);
                list.add(new Eleve(rs.getInt("id"), rs.getString("nom"), c, rs.getString("id_anonymat")));
            }
        }
        return list;
    }

    public void insert(Eleve eleve) throws SQLException {
        String sql = "INSERT INTO eleves(nom, classe_id, id_anonymat) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = ConnexionDB.getConnexion().prepareStatement(sql)) {
            stmt.setString(1, eleve.getNom());
            stmt.setInt(2, eleve.getClasse().getId());
            stmt.setString(3, eleve.getIdAnonymat());
            stmt.executeUpdate();
        }
    }

    public Eleve findByAnonymat(String idAnonymat) throws SQLException {
    String sql = "SELECT * FROM eleves WHERE id_anonymat = ?";
    try (PreparedStatement stmt = ConnexionDB.getConnexion().prepareStatement(sql)) {
        stmt.setString(1, idAnonymat);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            Classe c = new ClasseDAO().findById(rs.getInt("classe_id"));
            return new Eleve(rs.getInt("id"), rs.getString("nom"), c, idAnonymat);
        }
    }
    return null;
}

}
