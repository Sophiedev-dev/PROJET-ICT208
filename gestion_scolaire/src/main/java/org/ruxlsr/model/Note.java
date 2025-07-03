package org.ruxlsr.model;

public class Note {
    private int id;
    private int eleveId;
    private int coursId;
    private int trimestre;
    private float noteCC;
    private float noteExamen;
    private float moyenne;

    public Note(int id, int eleveId, int coursId, int trimestre, float noteCC, float noteExamen, float moyenne) {
        this.id = id;
        this.eleveId = eleveId;
        this.coursId = coursId;
        this.trimestre = trimestre;
        this.noteCC = noteCC;
        this.noteExamen = noteExamen;
        this.moyenne = moyenne;
    }

    public int getId() { return id; }
    public int getEleveId() { return eleveId; }
    public int getCoursId() { return coursId; }
    public int getTrimestre() { return trimestre; }
    public float getNoteCC() { return noteCC; }
    public float getNoteExamen() { return noteExamen; }
    public float getMoyenne() { return moyenne; }

    @Override
    public String toString() {
        return "Note{id=" + id + ", eleveId=" + eleveId + ", coursId=" + coursId +
                ", trim=" + trimestre + ", cc=" + noteCC + ", exam=" + noteExamen + ", moy=" + moyenne + "}";
    }
}
