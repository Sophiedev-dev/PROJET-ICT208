package org.ruxlsr.model;

public class Cours {
    private int id;
    private String nom;
    private int coefficient;
    private int enseignantId;

    public Cours(int id, String nom, int coefficient, int enseignantId) {
        this.id = id;
        this.nom = nom;
        this.coefficient = coefficient;
        this.enseignantId = enseignantId;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public int getCoefficient() {
        return coefficient;
    }

    public int getEnseignantId() {
        return enseignantId;
    }

    @Override
    public String toString() {
        return nom;
    }
}
