package model;

public record Enseignant(
        int idEnseignant,
        String nom,
        String prenom,
        String email,
        String telephone,
        String specialite) {
}
