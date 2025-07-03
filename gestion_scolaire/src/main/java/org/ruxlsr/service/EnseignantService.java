package org.ruxlsr.service;

import org.ruxlsr.dao.CoursClasseDAO;
import org.ruxlsr.dao.NoteDAO;
import org.ruxlsr.model.Cours;
import org.ruxlsr.model.Eleve;
import org.ruxlsr.utils.DatabaseConnection;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EnseignantService {

    public void afficherCoursEtClasses(int enseignantId) {
        String sql = """
            SELECT cours.id, cours.nom AS cours_nom, classes.nom AS classe_nom
            FROM cours
            JOIN cours_classes ON cours.id = cours_classes.cours_id
            JOIN classes ON cours_classes.classe_id = classes.id
            WHERE cours.enseignant_id = ?
            """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, enseignantId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println("Cours : " + rs.getString("cours_nom") + ", Classe : " + rs.getString("classe_nom"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saisirNoteCC(int eleveId, int coursId, int trimestre, float noteCC) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String check = "SELECT id FROM notes WHERE eleve_id = ? AND cours_id = ? AND trimestre = ?";
            PreparedStatement checkStmt = conn.prepareStatement(check);
            checkStmt.setInt(1, eleveId);
            checkStmt.setInt(2, coursId);
            checkStmt.setInt(3, trimestre);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                // Mise à jour
                String update = "UPDATE notes SET note_cc = ?, moyenne = (note_examen + ?) / 2 WHERE id = ?";
                PreparedStatement updateStmt = conn.prepareStatement(update);
                updateStmt.setFloat(1, noteCC);
                updateStmt.setFloat(2, noteCC);
                updateStmt.setInt(3, rs.getInt("id"));
                updateStmt.executeUpdate();
            } else {
                // Insertion
                String insert = "INSERT INTO notes (eleve_id, cours_id, trimestre, note_cc, moyenne) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement insertStmt = conn.prepareStatement(insert);
                insertStmt.setInt(1, eleveId);
                insertStmt.setInt(2, coursId);
                insertStmt.setInt(3, trimestre);
                insertStmt.setFloat(4, noteCC);
                insertStmt.setFloat(5, noteCC); // provisoire
                insertStmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saisirNoteExamen(String idAnonymat, int coursId, int trimestre, float noteExamen) {
        String sql = """
            SELECT id, note_cc FROM notes 
            JOIN eleves ON notes.eleve_id = eleves.id 
            WHERE eleves.id_anonymat = ? AND notes.cours_id = ? AND notes.trimestre = ?
        """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idAnonymat);
            stmt.setInt(2, coursId);
            stmt.setInt(3, trimestre);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                float noteCC = rs.getFloat("note_cc");
                int noteId = rs.getInt("id");
                float moyenne = (noteCC + noteExamen) / 2;

                String update = "UPDATE notes SET note_examen = ?, moyenne = ? WHERE id = ?";
                PreparedStatement updateStmt = conn.prepareStatement(update);
                updateStmt.setFloat(1, noteExamen);
                updateStmt.setFloat(2, moyenne);
                updateStmt.setInt(3, noteId);
                updateStmt.executeUpdate();
            } else {
                System.out.println("Note non trouvée (CC absente ?)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Cours> getCoursByEnseignant(int enseignantId) {
        List<Cours> coursList = new ArrayList<>();
        String sql = "SELECT * FROM cours WHERE enseignant_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, enseignantId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                coursList.add(new Cours(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getInt("coefficient"),
                        rs.getInt("enseignant_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return coursList;
    }

    public int getClasseIdForCours(int coursId) {
        String sql = "SELECT classe_id FROM cours_classes WHERE cours_id = ? LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, coursId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("classe_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<Eleve> getElevesByClasse(int classeId) {
        List<Eleve> list = new ArrayList<>();
        String sql = "SELECT * FROM eleves WHERE classe_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, classeId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new Eleve(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getInt("classe_id"),
                        rs.getString("id_anonymat")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Set<Integer> getClasseIdsByEnseignant(int enseignantId) {
        Set<Integer> ids = new HashSet<>();
        String sql = """
            SELECT DISTINCT cc.classe_id
            FROM cours c
            JOIN cours_classes cc ON c.id = cc.cours_id
            WHERE c.enseignant_id = ?
        """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, enseignantId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ids.add(rs.getInt("classe_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ids;
    }

    public void getNoteParclasseEtCours(DefaultTableModel model, int classeId, int coursId, int trimestre){
        NoteDAO noteDAO = new NoteDAO();
        noteDAO.getNotesParClasseEtCours(model, classeId, coursId, trimestre);
    }


}
