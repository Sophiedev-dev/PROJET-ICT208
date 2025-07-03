package org.ruxlsr.view.console.admin;

import org.ruxlsr.model.Classe;
import org.ruxlsr.model.Cours;
import org.ruxlsr.model.Eleve;
import org.ruxlsr.service.AdminService;
import org.ruxlsr.service.NoteService;

import java.sql.SQLException;
import java.util.*;

public class PanelResultatsAdminConsole {
    private final Scanner scanner = new Scanner(System.in);
    private final AdminService adminService = new AdminService();
    private final NoteService noteService = new NoteService();

    public void gerer() {
        try {
            System.out.println("\n=== Consultation des Résultats ===");

            // Sélection classe
            List<Classe> classes = adminService.listerClasses();
            if (classes.isEmpty()) {
                System.out.println("Aucune classe disponible.");
                return;
            }

            System.out.println("Choisissez une classe :");
            for (int i = 0; i < classes.size(); i++) {
                System.out.printf("%d. %s%n", i + 1, classes.get(i).getNom());
            }
            int classeIndex = Integer.parseInt(scanner.nextLine()) - 1;
            Classe classe = classes.get(classeIndex);

            // Sélection cours
            List<Cours> coursList = adminService.listerCoursParClasse(classe.getId());
            if (coursList.isEmpty()) {
                System.out.println("Aucun cours associé à cette classe.");
                return;
            }

            System.out.println("Choisissez un cours :");
            for (int i = 0; i < coursList.size(); i++) {
                System.out.printf("%d. %s%n", i + 1, coursList.get(i).getNom());
            }
            int coursIndex = Integer.parseInt(scanner.nextLine()) - 1;
            Cours cours = coursList.get(coursIndex);

            // Sélection trimestre
            System.out.print("Trimestre (1, 2 ou 3) : ");
            int trimestre = Integer.parseInt(scanner.nextLine());
            if (trimestre < 1 || trimestre > 3) {
                System.out.println("Trimestre invalide.");
                return;
            }

            afficherResultats(classe, cours, trimestre);
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    private void afficherResultats(Classe classe, Cours cours, int trimestre) throws SQLException {
        List<Eleve> eleves = adminService.getElevesByClasse(classe.getId());
        if (eleves.isEmpty()) {
            System.out.println("Aucun élève dans cette classe.");
            return;
        }

        List<ResultatEleve> resultats = new ArrayList<>();
        for (Eleve e : eleves) {
            float noteCC = noteService.getNoteCC(e.getId(), cours.getId(), trimestre);
            float noteEx = noteService.getNoteExamen(e.getId(), cours.getId(), trimestre);
            float moyenne = (noteCC + noteEx) / 2f;
            String mention = getMention(moyenne);
            resultats.add(new ResultatEleve(e.getNom(), e.getIdAnonymat(), noteCC, noteEx, moyenne, mention));
        }

        // Tri par moyenne décroissante
        resultats.sort((a, b) -> Float.compare(b.moyenne, a.moyenne));

        // Affichage
        System.out.println("\nRésultats :");
        System.out.printf("%-3s %-15s %-10s %-8s %-8s %-8s %-12s %-5s%n",
                "#", "Nom", "Anonymat", "CC", "Examen", "Moyenne", "Mention", "Rang");

        int rang = 1;
        for (ResultatEleve r : resultats) {
            System.out.printf("%-3d %-15s %-10s %-8.2f %-8.2f %-8.2f %-12s %-5d%n",
                    rang, r.nom, r.anonymat, r.cc, r.examen, r.moyenne, r.mention, rang);
            rang++;
        }
    }

    private String getMention(float moyenne) {
        if (moyenne >= 16) return "Très Bien";
        if (moyenne >= 14) return "Bien";
        if (moyenne >= 12) return "Assez Bien";
        if (moyenne >= 10) return "Passable";
        return "Insuffisant";
    }

    private static class ResultatEleve {
        String nom;
        String anonymat;
        float cc, examen, moyenne;
        String mention;

        ResultatEleve(String nom, String anonymat, float cc, float examen, float moyenne, String mention) {
            this.nom = nom;
            this.anonymat = anonymat;
            this.cc = cc;
            this.examen = examen;
            this.moyenne = moyenne;
            this.mention = mention;
        }
    }
}
