package org.ruxlsr.view.console.eleve;

import org.ruxlsr.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class EleveConsole {
    private final int eleveId;
    private final Scanner scanner = new Scanner(System.in);

    public EleveConsole(int eleveId) {
        this.eleveId = eleveId;
    }

    public void afficherBulletin() {
        String nom = getNomEleve();
        System.out.println("\n=== Bulletin de " + nom + " ===");

        while (true) {
            System.out.print("Choisir le trimestre (1, 2, 3 ou 0 pour quitter) : ");
            String input = scanner.nextLine();

            if (input.equals("0")) break;

            int trimestre;
            try {
                trimestre = Integer.parseInt(input);
                if (trimestre < 1 || trimestre > 3) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                System.out.println("Trimestre invalide.");
                continue;
            }

            afficherNotesPourTrimestre(trimestre);
        }
    }

    private void afficherNotesPourTrimestre(int trimestre) {
        try (Connection conn = DatabaseConnection.getConnection()) {
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

            float totalCoef = 0, totalPonderee = 0;
            System.out.printf("%-20s %-10s %-10s %-10s %-10s%n", "Matière", "Coef", "Note CC", "Examen", "Moyenne");

            boolean hasData = false;

            while (rs.next()) {
                hasData = true;
                String cours = rs.getString("cours");
                int coef = rs.getInt("coefficient");
                float cc = rs.getFloat("note_cc");
                float ex = rs.getFloat("note_examen");
                float moy = rs.getFloat("moyenne");

                System.out.printf("%-20s %-10d %-10.2f %-10.2f %-10.2f%n", cours, coef, cc, ex, moy);

                totalCoef += coef;
                totalPonderee += moy * coef;
            }

            if (!hasData) {
                System.out.println("Aucune note enregistrée pour ce trimestre.");
                return;
            }

            float moyenne = totalCoef > 0 ? totalPonderee / totalCoef : 0;
            String mention = getMention(moyenne);
            String rang = getRang(trimestre);

            System.out.printf("\nMoyenne Générale : %.2f%n", moyenne);
            System.out.println("Mention : " + mention);
            System.out.println("Rang : " + rang);

        } catch (Exception e) {
            System.out.println("Erreur lors du chargement du bulletin : " + e.getMessage());
        }
    }

    private String getNomEleve() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT nom FROM eleves WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, eleveId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getString("nom");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Élève inconnu";
    }

    private String getMention(float moyenne) {
        if (moyenne >= 16) return "Très Bien";
        if (moyenne >= 14) return "Bien";
        if (moyenne >= 12) return "Assez Bien";
        if (moyenne >= 10) return "Passable";
        return "Insuffisant";
    }

    private String getRang(int trimestre) {
        try (Connection conn = DatabaseConnection.getConnection()) {
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

            PreparedStatement stmt = conn.prepareStatement(sql);
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
