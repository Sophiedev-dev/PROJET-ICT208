package model;

public class Utilisateur {
    private int id;
    private String identifiant;
    private String motDePasse;
    private Role role;
    private Enseignant enseignant;
    private Eleve eleve;

    public Utilisateur() {}

    public Utilisateur(int id, String identifiant, String motDePasse, Role role) {
        this.id = id;
        this.identifiant = identifiant;
        this.motDePasse = motDePasse;
        this.role = role;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getIdentifiant() { return identifiant; }
    public void setIdentifiant(String identifiant) { this.identifiant = identifiant; }

    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public Enseignant getEnseignant() { return enseignant; }
    public void setEnseignant(Enseignant enseignant) { this.enseignant = enseignant; }

    public Eleve getEleve() { return eleve; }
    public void setEleve(Eleve eleve) { this.eleve = eleve; }
}
