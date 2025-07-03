package model;

public class Bulletin {
    private int id;
    private Eleve eleve;
    private Classe classe;
    private Trimestre trimestre;
    private double moyenne;
    private int rang;
    private String mention;

    public Bulletin(int id, Eleve eleve, Classe classe, Trimestre trimestre, double moyenne, int rang, String mention) {
        this.id = id;
        this.eleve = eleve;
        this.classe = classe;
        this.trimestre = trimestre;
        this.moyenne = moyenne;
        this.rang = rang;
        this.mention = mention;
    }

    // Getters et setters
    public int getId() { return id; }
    public Eleve getEleve() { return eleve; }
    public Classe getClasse() { return classe; }
    public Trimestre getTrimestre() { return trimestre; }
    public double getMoyenne() { return moyenne; }
    public int getRang() { return rang; }
    public String getMention() { return mention; }

    public void setId(int id) { this.id = id; }
    public void setEleve(Eleve eleve) { this.eleve = eleve; }
    public void setClasse(Classe classe) { this.classe = classe; }
    public void setTrimestre(Trimestre trimestre) { this.trimestre = trimestre; }
    public void setMoyenne(double moyenne) { this.moyenne = moyenne; }
    public void setRang(int rang) { this.rang = rang; }
    public void setMention(String mention) { this.mention = mention; }
}
