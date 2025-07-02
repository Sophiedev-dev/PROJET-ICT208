package models;

import java.util.ArrayList;
import java.util.List;

public class Enseignant extends Utilisateur {
    private String telephone;
    private String specialite;
    private List<String> cours;
    private List<String> classes;

    public Enseignant(String nom, String prenom, String email, String telephone, String specialite) {
        super(nom, prenom, email);
        this.telephone = telephone;
        this.specialite = specialite;
        this.cours = new ArrayList<>();
        this.classes = new ArrayList<>();
    }

    @Override
    public boolean authentifier(String login, String motDePasse) {
        // Pour l'instant, on utilise l'email comme login
        return this.email.equals(login);
    }

    public void ajouterCours(String cours) {
        this.cours.add(cours);
    }

    public void ajouterClasse(String classe) {
        this.classes.add(classe);
    }

    public List<String> getCours() {
        return cours;
    }

    public List<String> getClasses() {
        return classes;
    }

    public void saisirNotes() {
        // TODO: Implémenter la saisie des notes
    }

    public String getTelephone() {
        return telephone;
    }

    public String getSpecialite() {
        return specialite;
    }
}