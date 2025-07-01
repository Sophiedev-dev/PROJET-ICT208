package model;

public record Trimestre(
        int idTrimestre,
        int numeroTrimestre,
        LocalDate dateDebut,
        LocalDate dateFin,
        int anneeScolaire) {
}
