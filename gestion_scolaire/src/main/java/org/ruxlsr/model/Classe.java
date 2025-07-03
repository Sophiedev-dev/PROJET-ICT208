package org.ruxlsr.model;

public class Classe {
    private int id;
    private String nom;
    private int niveauId;

    public Classe(int id, String nom, int niveauId) {
        this.id = id;
        this.nom = nom;
        this.niveauId = niveauId;
    }

    public int getId() { return id; }
    public String getNom() { return nom; }
    public int getNiveauId() { return niveauId; }

    public void setId(int id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }

    @Override
    public String toString() {
        return nom; // Affiche seulement le nom dans la combo
    }
}
