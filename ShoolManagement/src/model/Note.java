package model;

public class Note {
    private int id;
    private Eleve eleve;
    private Cours cours;
    private Trimestre trimestre;
    private double noteCC;
    private double noteExamen;

    public Note() {}

    public Note(int id, Eleve eleve, Cours cours, Trimestre trimestre, double noteCC, double noteExamen) {
        this.id = id;
        this.eleve = eleve;
        this.cours = cours;
        this.trimestre = trimestre;
        this.noteCC = noteCC;
        this.noteExamen = noteExamen;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Eleve getEleve() { return eleve; }
    public void setEleve(Eleve eleve) { this.eleve = eleve; }

    public Cours getCours() { return cours; }
    public void setCours(Cours cours) { this.cours = cours; }

    public Trimestre getTrimestre() { return trimestre; }
    public void setTrimestre(Trimestre trimestre) { this.trimestre = trimestre; }

    public double getNoteCC() { return noteCC; }
    public void setNoteCC(double noteCC) { this.noteCC = noteCC; }

    public double getNoteExamen() { return noteExamen; }
    public void setNoteExamen(double noteExamen) { this.noteExamen = noteExamen; }

    public double getMoyenne() {
        return (noteCC + noteExamen) / 2;
    }
}
