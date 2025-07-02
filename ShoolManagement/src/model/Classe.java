package model;

public class Classe {
    private int id;
    private String nom;
    private Niveau niveau;

    public Classe() {}

    public Classe(int id, String nom, Niveau niveau) {
        this.id = id;
        this.nom = nom;
        this.niveau = niveau;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public Niveau getNiveau() { return niveau; }
    public void setNiveau(Niveau niveau) { this.niveau = niveau; }

    @Override
    public String toString() {
        return nom + " (" + niveau.getNom() + ")";
    }
}
