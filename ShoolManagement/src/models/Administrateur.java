package model;

public record Administrateur(
        int idAdmin,
        String nom,
        String prenom,
        String email,
        String login,
        String motDePasse) {
}
