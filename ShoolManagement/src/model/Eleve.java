package model;

public class Eleve {
    private int id;
    private String nom;
    private Classe classe;
    private String idAnonymat;

    public Eleve() {}

    public Eleve(int id, String nom, Classe classe, String idAnonymat) {
        this.id = id;
        this.nom = nom;
        this.classe = classe;
        this.idAnonymat = idAnonymat;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public Classe getClasse() { return classe; }
    public void setClasse(Classe classe) { this.classe = classe; }

    public String getIdAnonymat() { return idAnonymat; }
    public void setIdAnonymat(String idAnonymat) { this.idAnonymat = idAnonymat; }

    @Override
    public String toString() {
        return nom + " (" + classe.getNom() + ")";
    }
}
