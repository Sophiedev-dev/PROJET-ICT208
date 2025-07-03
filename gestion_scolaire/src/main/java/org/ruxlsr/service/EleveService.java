package org.ruxlsr.service;

import org.ruxlsr.utils.DatabaseConnection;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class EleveService {

    public void afficherBulletin(int eleveId, int trimestre) {
        try (Connection conn = DatabaseConnection.getConnection()) {

            // 1. Récupérer la classe de l’élève
            int classeId = getClasseId(conn, eleveId);
            if (classeId == -1) {
                System.out.println("Classe introuvable.");
                return;
            }

            // 2. Récupérer toutes les notes de l’élève avec les coefficients
            String sql = """
                SELECT c.nom AS cours, c.coefficient, n.note_cc, n.note_examen, n.moyenne
                FROM notes n
                JOIN cours c ON c.id = n.cours_id
                WHERE n.eleve_id = ? AND n.trimestre = ?
            """;

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, eleveId);
            stmt.setInt(2, trimestre);
            ResultSet rs = stmt.executeQuery();

            float totalCoef = 0;
            float totalPonderee = 0;

            System.out.println("\n--- Bulletin Trimestre " + trimestre + " ---");

            while (rs.next()) {
                String cours = rs.getString("cours");
                int coef = rs.getInt("coefficient");
                float cc = rs.getFloat("note_cc");
                float exam = rs.getFloat("note_examen");
                float moyenne = rs.getFloat("moyenne");

                System.out.printf("%-20s | Coef: %d | CC: %.2f | Exam: %.2f | Moy: %.2f\n",
                        cours, coef, cc, exam, moyenne);

                totalCoef += coef;
                totalPonderee += moyenne * coef;
            }

            // 3. Calcul moyenne générale pondérée
            float moyenneGenerale = totalCoef > 0 ? totalPonderee / totalCoef : 0;
            String mention = getMention(moyenneGenerale);

            // 4. Calcul du rang dans la classe
            int rang = getRang(conn, classeId, eleveId, trimestre);

            System.out.printf("\nMoyenne générale : %.2f\n", moyenneGenerale);
            System.out.println("Mention : " + mention);
            System.out.println("Rang : " + rang);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getClasseId(Connection conn, int eleveId) throws SQLException {
        String sql = "SELECT classe_id FROM eleves WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, eleveId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("classe_id");
        }
        return -1;
    }

    private String getMention(float moyenne) {
        if (moyenne >= 16) return "Très Bien";
        if (moyenne >= 14) return "Bien";
        if (moyenne >= 12) return "Assez Bien";
        if (moyenne >= 10) return "Passable";
        return "Insuffisant";
    }

    private int getRang(Connection conn, int classeId, int eleveId, int trimestre) throws SQLException {
        String sql = """
            SELECT eleve_id, SUM(moyenne * c.coefficient) / SUM(c.coefficient) AS moyenne_generale
            FROM notes n
            JOIN eleves e ON n.eleve_id = e.id
            JOIN cours c ON n.cours_id = c.id
            WHERE e.classe_id = ? AND n.trimestre = ?
            GROUP BY eleve_id
            ORDER BY moyenne_generale DESC
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, classeId);
            stmt.setInt(2, trimestre);
            ResultSet rs = stmt.executeQuery();
            int rang = 1;
            while (rs.next()) {
                if (rs.getInt("eleve_id") == eleveId) return rang;
                rang++;
            }
        }
        return -1; // non trouvé
    }
}
