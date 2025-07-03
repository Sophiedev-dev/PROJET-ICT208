package org.ruxlsr.view.console.enseignant;

import org.ruxlsr.model.Cours;
import org.ruxlsr.model.Eleve;
import org.ruxlsr.service.EnseignantService;

import java.util.List;
import java.util.Scanner;

public class ConsoleSaisieExamen {
    private final int enseignantId;
    private final EnseignantService service = new EnseignantService();
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleSaisieExamen(int enseignantId) {
        this.enseignantId = enseignantId;
    }

    public void executer() {
        List<Cours> coursList = service.getCoursByEnseignant(enseignantId);
        if (coursList.isEmpty()) {
            System.out.println("Aucun cours assigné.");
            return;
        }

        System.out.println("\n--- Saisie des notes d'examen ---");
        for (int i = 0; i < coursList.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, coursList.get(i).getNom());
        }

        System.out.print("Choisir un cours : ");
        int indexCours = Integer.parseInt(scanner.nextLine()) - 1;
        Cours cours = coursList.get(indexCours);
        int classeId = service.getClasseIdForCours(cours.getId());

        System.out.print("Trimestre (1-3) : ");
        int trimestre = Integer.parseInt(scanner.nextLine());

        List<Eleve> eleves = service.getElevesByClasse(classeId);
        for (Eleve el : eleves) {
            System.out.printf("Note examen (anonymat %s) : ", el.getIdAnonymat());
            String input = scanner.nextLine();
            if (!input.isBlank()) {
                float note = Float.parseFloat(input);
                service.saisirNoteExamen(el.getIdAnonymat(), cours.getId(), trimestre, note);
            }
        }

        System.out.println("Notes d'examen enregistrées.");
    }
}
