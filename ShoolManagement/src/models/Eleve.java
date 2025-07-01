package model;

public record Eleve(
        int id_eleve,
        String nom,
        String prenom,
        LocalDate date_naissance,
        String adresse,
        String telephone,
        int id_classe) {
}
