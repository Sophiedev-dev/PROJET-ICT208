package model;

public record Note(
        int idNote,
        float noteControleContinu,
        float noteExamen,
        int idEleve,
        int idCours,
        int idTrimestre) {
}
