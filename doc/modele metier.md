# Modèle métier – Gestion Scolaire

## 1. Niveaux
- Représente les niveaux scolaires (ex : CP, CE1, 6ème, Terminale).
- Attributs : `id`, `nom`.

## 2. Classes
- Chaque classe appartient à un niveau (`niveau_id`).
- Exemple : "6ème-A" est une classe du niveau "6ème".
- Attributs : `id`, `nom`, `niveau_id`.

## 3. Enseignants
- Liste des enseignants de l’établissement.
- Attributs : `id`, `nom`.

## 4. Cours
- Un cours a un nom, un coefficient (pondération), et est attribué à un enseignant.
- Attributs : `id`, `nom`, `coefficient`, `enseignant_id`.

## 5. Cours_Classes
- Table de liaison (relation n-n) entre les cours et les classes.
- Permet d’assigner plusieurs cours à plusieurs classes.
- Attributs : `id`, `cours_id`, `classe_id`.

## 6. Élèves
- Chaque élève appartient à une classe et possède un identifiant d’anonymat (pour les examens).
- Attributs : `id`, `nom`, `classe_id`, `id_anonymat`.

## 7. Utilisateurs
- Permet la gestion des connexions (authentification).
- Un utilisateur peut être un administrateur, un enseignant ou un élève.
- Attributs : `id`, `identifiant`, `mot_de_passe`, `role`, `enseignant_id`, `eleve_id`.

## 8. Notes
- Stocke les notes des élèves pour chaque cours et chaque trimestre.
- Attributs : `id`, `eleve_id`, `cours_id`, `trimestre`, `note_cc`, `note_examen`, `moyenne`.

---

## Relations principales

- **Niveau** 1---n **Classe**
- **Classe** 1---n **Élève**
- **Enseignant** 1---n **Cours**
- **Cours** n---n **Classe** (via **Cours_Classes**)
- **Élève** 1---n **Note** (chaque note est pour un élève, un cours, un trimestre)
- **Utilisateur** peut référencer un **Enseignant** ou un **Élève** (pour la connexion)

---


ce modèle permet de gérer :
- L’organisation pédagogique (niveaux, classes, cours, enseignants)
- L’affectation des cours aux classes
- L’inscription des élèves et leur anonymat
- La saisie et la consultation des notes (par trimestre)
- L’authentification et la gestion des rôles (admin, enseignant, élève)

