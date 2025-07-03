package org.ruxlsr.model;

public class Eleve {
    private int id;
    private String nom;
    private int classeId;
    private String idAnonymat;

    public Eleve(int id, String nom, int classeId, String idAnonymat) {
        this.id = id;
        this.nom = nom;
        this.classeId = classeId;
        this.idAnonymat = idAnonymat;
    }

    public int getId() { return id; }
    public String getNom() { return nom; }
    public int getClasseId() { return classeId; }
    public String getIdAnonymat() { return idAnonymat; }

    @Override
    public String toString() {
        return "Eleve{id=" + id + ", nom='" + nom + "', classeId=" + classeId + ", anonymat='" + idAnonymat + "'}";
    }
}
