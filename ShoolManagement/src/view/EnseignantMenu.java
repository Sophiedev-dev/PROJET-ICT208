package view;

import controller.EnseignantController;
import dao.ClasseDAO;
import model.*;

import java.sql.SQLException;
import java.util.*;

public class EnseignantMenu {
    private final EnseignantController controller = new EnseignantController();
    private final Scanner scanner = new Scanner(System.in);

    public void afficher(Utilisateur enseignantUser) {
        Enseignant enseignant = enseignantUser.getEnseignant();
        int choix;
        do {
            System.out.println("\n=== Menu Enseignant ===");
            System.out.println("1. Saisir note CC");
            System.out.println("2. Saisir note examen");
            System.out.println("0. Quitter");
            System.out.print("Choix : ");
            choix = scanner.nextInt(); scanner.nextLine();

            try {
                List<Cours> coursList = controller.getCoursParEnseignant(enseignant);
                for (int i = 0; i < coursList.size(); i++)
                    System.out.println((i + 1) + ". " + coursList.get(i).getNom());

                System.out.print("Choisir cours : ");
                Cours cours = coursList.get(scanner.nextInt() - 1); scanner.nextLine();

                System.out.print("Trimestre (1-3) : ");
                int t = scanner.nextInt(); scanner.nextLine();
                Trimestre trimestre = Trimestre.values()[t - 1];

                if (choix == 1) {
                    List<Classe> classes = new ClasseDAO().findAll();
                    for (int i = 0; i < classes.size(); i++)
                        System.out.println((i + 1) + ". " + classes.get(i).getNom());
                    System.out.print("Choisir classe : ");
                    Classe classe = classes.get(scanner.nextInt() - 1); scanner.nextLine();
                    List<Eleve> eleves = controller.getElevesDeClasse(classe.getId());
                    for (Eleve e : eleves) {
                        System.out.print("Note CC de " + e.getNom() + " : ");
                        double note = scanner.nextDouble();
                        controller.saisirNoteCC(e, cours, trimestre, note);
                    }
                } else if (choix == 2) {
                    System.out.println("Saisir les notes par anonymat (entrez 'fin' pour arrêter)");
                    while (true) {
                        System.out.print("Anonymat : ");
                        String idA = scanner.nextLine();
                        if (idA.equalsIgnoreCase("fin")) break;
                        System.out.print("Note examen : ");
                        double note = scanner.nextDouble(); scanner.nextLine();
                        controller.saisirNoteExamen(idA, cours, trimestre, note);
                    }
                }

            } catch (Exception e) {
                System.out.println("Erreur : " + e.getMessage());
            }
        } while (choix != 0);
    }
}
