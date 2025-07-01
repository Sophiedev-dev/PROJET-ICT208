package models;

public record VueMoyennesEleves(
        int id_eleve,
        String nom,
        String prenom,
        int id_trimestre,
        int id_cours,
        String nom_cours,
        int coefficient,
        double note_controle_continu,
        double note_examen,
        double moyenne_trimestre) {
}
