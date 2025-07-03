package model;

public class Cours {
    private int id;
    private String nom;
    private int coefficient;
    private Enseignant enseignant;
    private Classe classe;  // nouvelle propriété

    public Cours() {}

    public Cours(int id, String nom, int coefficient, Enseignant enseignant, Classe classe) {
        this.id = id;
        this.nom = nom;
        this.coefficient = coefficient;
        this.enseignant = enseignant;
        this.classe = classe;
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

    public Classe getClasse() { return classe; }
    public void setClasse(Classe classe) { this.classe = classe; }

    @Override
    public String toString() {
        return nom + " (Coef: " + coefficient + ", Classe: " + (classe != null ? classe.getNom() : "N/A") + ")";
    }
}
