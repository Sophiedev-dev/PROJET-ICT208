package models;

public class Administrateur extends Utilisateur {
    private String login;
    private String motDePasse;

    public Administrateur(String nom, String prenom, String email, String login, String motDePasse) {
        super(nom, prenom, email);
        this.login = login;
        this.motDePasse = motDePasse;
    }

    @Override
    public boolean authentifier(String login, String motDePasse) {
        return this.login.equals(login) && this.motDePasse.equals(motDePasse);
    }

    // Méthodes spécifiques à l'administrateur
    public void gererEleves() {
        // TODO: Implémenter la gestion des élèves
    }

    public void gererEnseignants() {
        // TODO: Implémenter la gestion des enseignants
    }

    public void gererClasses() {
        // TODO: Implémenter la gestion des classes
    }

    public void gererCours() {
        // TODO: Implémenter la gestion des cours
    }
}