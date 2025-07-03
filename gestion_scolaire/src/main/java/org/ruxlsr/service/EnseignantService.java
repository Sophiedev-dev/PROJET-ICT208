package org.ruxlsr.service;

import org.ruxlsr.dao.CoursClasseDAO;
import org.ruxlsr.dao.NoteDAO;
import org.ruxlsr.model.Cours;
import org.ruxlsr.model.Eleve;
import org.ruxlsr.utils.DatabaseConnection;

import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
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
            JOptionPane.showMessageDialog(null, "Erreur SQL : " + e.getMessage());
        }
    }

    public void saisirNoteCC(int eleveId, int coursId, int trimestre, float noteCC) {
        String select = "SELECT id FROM notes WHERE eleve_id = ? AND cours_id = ? AND trimestre = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(select)) {
            stmt.setInt(1, eleveId);
            stmt.setInt(2, coursId);
            stmt.setInt(3, trimestre);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // La note existe, on fait un UPDATE
                String update = "UPDATE notes SET note_cc = ? WHERE eleve_id = ? AND cours_id = ? AND trimestre = ?";
                try (PreparedStatement updateStmt = conn.prepareStatement(update)) {
                    updateStmt.setFloat(1, noteCC);
                    updateStmt.setInt(2, eleveId);
                    updateStmt.setInt(3, coursId);
                    updateStmt.setInt(4, trimestre);
                    updateStmt.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Erreur lors de la mise à jour de la note : " + e.getMessage());
                }
            } else {
                // La note n'existe pas, on fait un INSERT
                String insert = "INSERT INTO notes (eleve_id, cours_id, trimestre, note_cc, moyenne) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insert)) {
                    insertStmt.setInt(1, eleveId);
                    insertStmt.setInt(2, coursId);
                    insertStmt.setInt(3, trimestre);
                    insertStmt.setFloat(4, noteCC);
                    insertStmt.setFloat(5, noteCC); // moyenne provisoire
                    insertStmt.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Erreur lors de l'insertion de la note : " + e.getMessage());
                }

            
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur SQL : " + e.getMessage());
        }
    }

    public void saisirNoteExamen(String anonymat, int coursId, int trimestre, float note) {
        // Si la note existe déjà, fait un UPDATE, sinon fait un INSERT
        String sql = """
            SELECT notes.id, notes.note_cc FROM notes 
            JOIN eleves ON notes.eleve_id = eleves.id 
            WHERE eleves.id_anonymat = ? AND notes.cours_id = ? AND notes.trimestre = ?
        """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, anonymat);
            stmt.setInt(2, coursId);
            stmt.setInt(3, trimestre);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                float noteCC = rs.getFloat("note_cc");
                int noteId = rs.getInt("id");
                float moyenne = (noteCC + note) / 2;

                String update = "UPDATE notes SET note_examen = ?, moyenne = ? WHERE id = ?";
                PreparedStatement updateStmt = conn.prepareStatement(update);
                updateStmt.setFloat(1, note);
                updateStmt.setFloat(2, moyenne);
                updateStmt.setInt(3, noteId);
                updateStmt.executeUpdate();
            } else {
                // Insertion si la note n'existe pas
                // Récupérer l'ID de l'élève à partir de l'anonymat
                String selectEleve = "SELECT id FROM eleves WHERE id_anonymat = ?";
                PreparedStatement eleveStmt = conn.prepareStatement(selectEleve);
                eleveStmt.setString(1, anonymat);
                ResultSet eleveRs = eleveStmt.executeQuery();
                if (eleveRs.next()) {
                    int eleveId = eleveRs.getInt("id");
                    String insert = "INSERT INTO notes (eleve_id, cours_id, trimestre, note_examen, moyenne) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement insertStmt = conn.prepareStatement(insert);
                    insertStmt.setInt(1, eleveId);
                    insertStmt.setInt(2, coursId);
                    insertStmt.setInt(3, trimestre);
                    insertStmt.setFloat(4, note);
                    insertStmt.setFloat(5, note); // provisoire
                    insertStmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur SQL : " + e.getMessage());
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
            JOptionPane.showMessageDialog(null, "Erreur SQL : " + e.getMessage());
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
            JOptionPane.showMessageDialog(null, "Erreur SQL : " + e.getMessage());
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
            JOptionPane.showMessageDialog(null, "Erreur SQL : " + e.getMessage());
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
            JOptionPane.showMessageDialog(null, "Erreur SQL : " + e.getMessage());
        }
        return ids;
    }

    public void getNoteParclasseEtCours(DefaultTableModel model, int classeId, int coursId, int trimestre){
        NoteDAO noteDAO = new NoteDAO();
        noteDAO.getNotesParClasseEtCours(model, classeId, coursId, trimestre);
    }

    public Float getNoteCC(int eleveId, int coursId, int trimestre) {
        // Retourne la note CC si elle existe, sinon null
        String sql = "SELECT note_cc FROM notes WHERE eleve_id = ? AND cours_id = ? AND trimestre = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, eleveId);
            stmt.setInt(2, coursId);
            stmt.setInt(3, trimestre);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getFloat("note_cc");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur SQL : " + e.getMessage());
        }
        return null;
    }

    public Float getNoteExamen(String anonymat, int coursId, int trimestre) {
        // Retourne la note d'examen si elle existe, sinon null
        String sql = "SELECT note_examen FROM notes JOIN eleves ON notes.eleve_id = eleves.id WHERE eleves.id_anonymat = ? AND notes.cours_id = ? AND notes.trimestre = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, anonymat);
            stmt.setInt(2, coursId);
            stmt.setInt(3, trimestre);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getFloat("note_examen");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur SQL : " + e.getMessage());
        }
        return null;
    }
}
