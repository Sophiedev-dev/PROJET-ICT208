package org.ruxlsr.model;

public class CoursClasse {
    private int id;
    private int coursId;
    private int classeId;

    public CoursClasse(int id, int coursId, int classeId) {
        this.id = id;
        this.coursId = coursId;
        this.classeId = classeId;
    }

    public int getId() { return id; }
    public int getCoursId() { return coursId; }
    public int getClasseId() { return classeId; }

    @Override
    public String toString() {
        return "CoursClasse{id=" + id + ", coursId=" + coursId + ", classeId=" + classeId + "}";
    }
}
