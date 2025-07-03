package org.ruxlsr.view.console.enseignant;

import org.ruxlsr.service.EnseignantService;

import org.ruxlsr.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConsoleCoursClasses {
    private final int enseignantId;

    public ConsoleCoursClasses(int enseignantId) {
        this.enseignantId = enseignantId;
    }

    public void afficher() {
        System.out.println("\n=== Liste des cours et classes de l'enseignant ===");

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = """
                SELECT cours.id, cours.nom AS cours_nom, classes.nom AS classe_nom
                FROM cours
                JOIN cours_classes ON cours.id = cours_classes.cours_id
                JOIN classes ON cours_classes.classe_id = classes.id
                WHERE cours.enseignant_id = ?
            """;
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, enseignantId);
                ResultSet rs = stmt.executeQuery();

                int count = 0;
                while (rs.next()) {
                    count++;
                    int idCours = rs.getInt("id");
                    String nomCours = rs.getString("cours_nom");
                    String nomClasse = rs.getString("classe_nom");

                    System.out.printf("%d. Cours ID: %d | Nom: %s | Classe: %s\n", count, idCours, nomCours, nomClasse);
                }

                if (count == 0) {
                    System.out.println("Aucun cours ou classe assigné.");
                }

            }
        } catch (SQLException e) {
            System.out.println("Erreur lors du chargement des cours : " + e.getMessage());
        }
    }
}
