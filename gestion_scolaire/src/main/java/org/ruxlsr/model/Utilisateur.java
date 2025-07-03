package org.ruxlsr.model;

public class Utilisateur {
    private int id;
    private String identifiant;
    private String motDePasse;
    private String role;
    private Integer enseignantId;
    private Integer eleveId;

    public Utilisateur(int id, String identifiant, String motDePasse, String role, Integer enseignantId, Integer eleveId) {
        this.id = id;
        this.identifiant = identifiant;
        this.motDePasse = motDePasse;
        this.role = role;
        this.enseignantId = enseignantId;
        this.eleveId = eleveId;
    }

    public int getId() {
        return id;
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public String getRole() {
        return role;
    }

    public Integer getEnseignantId() {
        return enseignantId;
    }

    public Integer getEleveId() {
        return eleveId;
    }
}

