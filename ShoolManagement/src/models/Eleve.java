package models;

import java.util.Date;

public class Eleve extends Utilisateur {
    private Date dateNaissance;
    private String adresse;
    private String telephone;
    private String classe;

    public Eleve(String nom, String prenom, String email, Date dateNaissance, String adresse, String telephone, String classe) {
        super(nom, prenom, email);
        this.dateNaissance = dateNaissance;
        this.adresse = adresse;
        this.telephone = telephone;
        this.classe = classe;
    }

    @Override
    public boolean authentifier(String login, String motDePasse) {
        // Pour l'instant, on utilise une authentification simple avec le nom et prénom
        return this.nom.equalsIgnoreCase(login);
    }

    public void voirBulletin() {
        // TODO: Implémenter l'affichage du bulletin
        System.out.println("Bulletin de " + nom + " " + prenom);
        System.out.println("Classe: " + classe);
    }

    public void voirCours() {
        // TODO: Implémenter l'affichage des cours
        System.out.println("Liste des cours pour " + classe);
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getClasse() {
        return classe;
    }
}