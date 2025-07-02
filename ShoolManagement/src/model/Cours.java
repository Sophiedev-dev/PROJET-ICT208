package model;

public class Cours {
    private int id;
    private String nom;
    private int coefficient;
    private Enseignant enseignant;

    public Cours() {}

    public Cours(int id, String nom, int coefficient, Enseignant enseignant) {
        this.id = id;
        this.nom = nom;
        this.coefficient = coefficient;
        this.enseignant = enseignant;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public int getCoefficient() { return coefficient; }
    public void setCoefficient(int coefficient) { this.coefficient = coefficient; }

    public Enseignant getEnseignant() { return enseignant; }
    public void setEnseignant(Enseignant enseignant) { this.enseignant = enseignant; }

    @Override
    public String toString() {
        return nom + " (Coef: " + coefficient + ")";
    }
}
