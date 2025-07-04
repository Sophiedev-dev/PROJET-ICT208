package org.ruxlsr.dao;

import org.ruxlsr.model.Eleve;
import org.ruxlsr.utils.DatabaseConnection;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EleveDAO {

    public void insert(Eleve eleve) throws SQLException {
        String sql = "INSERT INTO eleves(nom, classe_id, id_anonymat) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, eleve.getNom());
            stmt.setInt(2, eleve.getClasseId());
            stmt.setString(3, eleve.getIdAnonymat());
            stmt.executeUpdate();
        }
    }

    public List<Eleve> getByClasse(int classeId) throws SQLException {
        List<Eleve> eleves = new ArrayList<>();
        String sql = "SELECT * FROM eleves WHERE classe_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, classeId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                eleves.add(new Eleve(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getInt("classe_id"),
                        rs.getString("id_anonymat")
                ));
            }
        }
        return eleves;
    }

    public List<Eleve> getAll() throws SQLException {
        List<Eleve> eleves = new ArrayList<>();
        String sql = "SELECT * FROM eleves";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                eleves.add(new Eleve(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getInt("classe_id"),
                        rs.getString("id_anonymat")
                ));
            }
        }
        return eleves;
    }

    public void updateAnonymat(int eleveId, String idAnonymat) throws SQLException {
        String sql = "UPDATE eleves SET id_anonymat = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idAnonymat);
            stmt.setInt(2, eleveId);
            stmt.executeUpdate();
        }
    }

    public void supprimerParClasse(int classeId) {
        String sql = "DELETE FROM eleves WHERE classe_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, classeId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int insertReturnId(Eleve e) {
        String sql = "INSERT INTO eleves (nom, classe_id, id_anonymat) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, e.getNom());
            stmt.setInt(2, e.getClasseId());
            stmt.setString(3, e.getIdAnonymat());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }
    public void update(int id, String nom, int classeId) {
        String sql = "UPDATE eleves SET nom = ?, classe_id = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nom);
            stmt.setInt(2, classeId);
            stmt.setInt(3, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void delete(int id) {
        String sql = "DELETE FROM eleves WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getNomById(int eleveId) {
        String sql = "SELECT nom FROM eleves WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, eleveId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getString("nom");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Élève inconnu";
    }

    public float remplirBulletin(int eleveId, int trimestre, DefaultTableModel model) {
        float totalCoef = 0;
        float totalPonderee = 0;
        String sql = """
            SELECT c.nom AS cours, c.coefficient, n.note_cc, n.note_examen, n.moyenne
            FROM notes n
            JOIN cours c ON c.id = n.cours_id
            WHERE n.eleve_id = ? AND n.trimestre = ?
        """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, eleveId);
            stmt.setInt(2, trimestre);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String cours = rs.getString("cours");
                int coef = rs.getInt("coefficient");
                float cc = rs.getFloat("note_cc");
                float exam = rs.getFloat("note_examen");
                float moy = rs.getFloat("moyenne");

                model.addRow(new Object[]{cours, coef, cc, exam, moy});
                totalCoef += coef;
                totalPonderee += moy * coef;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalCoef > 0 ? totalPonderee / totalCoef : 0;
    }

    public String getRang(int eleveId, int trimestre) {
        String sql = """
            SELECT e.id, SUM(n.moyenne * c.coefficient) / SUM(c.coefficient) AS moy
            FROM notes n
            JOIN eleves e ON e.id = n.eleve_id
            JOIN cours c ON c.id = n.cours_id
            WHERE n.trimestre = ? AND e.classe_id = (
                SELECT classe_id FROM eleves WHERE id = ?
            )
            GROUP BY e.id
            ORDER BY moy DESC
        """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, trimestre);
            stmt.setInt(2, eleveId);
            ResultSet rs = stmt.executeQuery();
            int rang = 1;
            while (rs.next()) {
                if (rs.getInt("id") == eleveId) return String.valueOf(rang);
                rang++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "-";
    }
}
