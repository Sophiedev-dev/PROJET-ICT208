package org.ruxlsr.view.console.admin;

import org.ruxlsr.model.Classe;
import org.ruxlsr.model.Niveau;
import org.ruxlsr.service.AdminService;

import java.util.List;
import java.util.Scanner;

public class PanelClassesConsole {
    private final Scanner scanner = new Scanner(System.in);
    private final AdminService service = new AdminService();

    public void gerer() {
        while (true) {
            System.out.println("\n=== Gestion des Classes ===");
            System.out.println("1. Lister les classes");
            System.out.println("2. Ajouter une classe");
            System.out.println("3. Supprimer une classe");
            System.out.println("4. Modifier le nom d’une classe");
            System.out.println("0. Retour");
            System.out.print("Votre choix : ");
            String choix = scanner.nextLine();

            switch (choix) {
                case "1" -> listerClasses();
                case "2" -> ajouterClasse();
                case "3" -> supprimerClasse();
                case "4" -> modifierClasse();
                case "0" -> { return; }
                default -> System.out.println("Choix invalide.");
            }
        }
    }

    private void listerClasses() {
        try {
            List<Classe> classes = service.listerClasses();
            System.out.println("ID\tNom\tNiveau");
            for (Classe c : classes) {
                String niveauNom = service.getNomNiveauById(c.getNiveauId());
                System.out.printf("%d\t%s\t%s%n", c.getId(), c.getNom(), niveauNom);
            }
        } catch (Exception e) {
            System.out.println("Erreur lors du chargement des classes : " + e.getMessage());
        }
    }

    private void ajouterClasse() {
        try {
            System.out.print("Nom de la classe : ");
            String nom = scanner.nextLine();

            List<Niveau> niveaux = service.listerNiveaux();
            for (int i = 0; i < niveaux.size(); i++) {
                System.out.printf("%d. %s%n", i + 1, niveaux.get(i).getNom());
            }
            System.out.print("Choisir un niveau : ");
            int index = Integer.parseInt(scanner.nextLine()) - 1;

            if (index >= 0 && index < niveaux.size()) {
                Niveau niveau = niveaux.get(index);
                service.creerClasse(nom, niveau.getId());
                System.out.println("Classe ajoutée avec succès.");
            } else {
                System.out.println("Choix de niveau invalide.");
            }
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    private void supprimerClasse() {
        try {
            System.out.print("ID de la classe à supprimer : ");
            int id = Integer.parseInt(scanner.nextLine());
            service.supprimerClasseParId(id);
            System.out.println("Classe supprimée avec succès.");
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    private void modifierClasse() {
        try {
            System.out.print("ID de la classe à modifier : ");
            int id = Integer.parseInt(scanner.nextLine());
            System.out.print("Nouveau nom : ");
            String nouveauNom = scanner.nextLine();
            service.modifierNomClasse(id, nouveauNom);
            System.out.println("Nom modifié avec succès.");
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }
}
