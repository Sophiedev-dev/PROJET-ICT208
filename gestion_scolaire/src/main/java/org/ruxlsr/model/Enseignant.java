package org.ruxlsr.model;


public class Enseignant {
    private int id;
    private String nom;

    public Enseignant(int id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    public int getId() { return id; }
    public String getNom() { return nom; }

    @Override
    public String toString() {
        return "Enseignant{id=" + id + ", nom='" + nom + "'}";
    }
}

