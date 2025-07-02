package models;

public abstract class Utilisateur {
    protected String nom;
    protected String prenom;
    protected String email;

    public Utilisateur(String nom, String prenom, String email) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getEmail() {
        return email;
    }

    public abstract boolean authentifier(String login, String motDePasse);
}