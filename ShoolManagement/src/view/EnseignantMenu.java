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
            System.out.println("1. Voir mes cours par classe");
            System.out.println("2. Voir les élèves d’un cours");
            System.out.println("3. Saisir note CC");
            System.out.println("4. Saisir note examen");
            System.out.println("5. Voir les notes des élèves par cours");
            System.out.println("6. Générer bulletins");
            System.out.println("0. Quitter");
            System.out.print("Choix : ");
            choix = scanner.nextInt(); scanner.nextLine();

            try {
                List<Cours> coursList = controller.getCoursParEnseignant(enseignant);
                if (coursList.isEmpty()) {
                    System.out.println("Aucun cours trouvé.");
                    continue;
                }

                if (choix >= 1 && choix <= 5) {
                    for (int i = 0; i < coursList.size(); i++)
                        System.out.println((i + 1) + ". " + coursList.get(i).getNom() + " - Classe : " + coursList.get(i).getClasse().getNom());
                    System.out.print("Choisir cours : ");
                    Cours cours = coursList.get(scanner.nextInt() - 1); scanner.nextLine();

                    if (choix == 1) {
                        System.out.println("Cours : " + cours.getNom() + " | Classe : " + cours.getClasse().getNom());
                    } else if (choix == 2) {
                        List<Eleve> eleves = controller.getElevesDeClasse(cours.getClasse().getId());
                        eleves.forEach(e -> System.out.println("- " + e.getNom()));
                    } else if (choix == 3) {
                        System.out.print("Trimestre (1-3) : ");
                        Trimestre trimestre = Trimestre.values()[scanner.nextInt() - 1];
                        scanner.nextLine();
                        List<Eleve> eleves = controller.getElevesDeClasse(cours.getClasse().getId());
                        for (Eleve e : eleves) {
                            System.out.print("Note CC de " + e.getNom() + " : ");
                            double note = scanner.nextDouble(); scanner.nextLine();
                            controller.saisirNoteCC(e, cours, trimestre, note);
                        }
                    } else if (choix == 4) {
                        System.out.print("Trimestre (1-3) : ");
                        Trimestre trimestre = Trimestre.values()[scanner.nextInt() - 1];
                        scanner.nextLine();
                        System.out.println("Saisir les notes examen par anonymat (tapez 'fin' pour arrêter)");
                        while (true) {
                            System.out.print("Anonymat : ");
                            String id = scanner.nextLine();
                            if (id.equalsIgnoreCase("fin")) break;
                            System.out.print("Note examen : ");
                            double note = scanner.nextDouble(); scanner.nextLine();
                            controller.saisirNoteExamen(id, cours, trimestre, note);
                        }
                    } else if (choix == 5) {
                        System.out.print("Trimestre (1-3) : ");
                        Trimestre trimestre = Trimestre.values()[scanner.nextInt() - 1];
                        scanner.nextLine();
                        List<Eleve> eleves = controller.getElevesDeClasse(cours.getClasse().getId());
                        for (Eleve e : eleves) {
                            Note n = controller.getNote(e, cours, trimestre);
                            if (n != null) {
                                double moyenne = (n.getNoteCC() + n.getNoteExamen()) / 2;
                                System.out.println(e.getNom() + " → CC: " + n.getNoteCC() + ", Examen: " + n.getNoteExamen() + ", Moyenne: " + moyenne);
                            }
                        }
                    }
                } else if (choix == 6) {
                    List<Classe> classes = new ClasseDAO().findAll();
                    for (int i = 0; i < classes.size(); i++)
                        System.out.println((i + 1) + ". " + classes.get(i).getNom());
                    System.out.print("Choisir classe pour le bulletin : ");
                    Classe classe = classes.get(scanner.nextInt() - 1); scanner.nextLine();

                    System.out.print("Trimestre (1-3) : ");
                    Trimestre trimestre = Trimestre.values()[scanner.nextInt() - 1]; scanner.nextLine();

                    controller.genererBulletin(classe.getId(), trimestre);
                }

            } catch (Exception e) {
                System.out.println("Erreur : " + e.getMessage());
            }

        } while (choix != 0);
    }
}
