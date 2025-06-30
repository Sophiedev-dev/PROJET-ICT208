erDiagram
    NIVEAU {
        int id_niveau PK
        string nom_niveau
        string description
    }
    
    CLASSE {
        int id_classe PK
        string nom_classe
        int id_niveau FK
    }
    
    ELEVE {
        int id_eleve PK
        string nom
        string prenom
        date date_naissance
        string adresse
        string telephone
        int id_classe FK
    }
    
    ENSEIGNANT {
        int id_enseignant PK
        string nom
        string prenom
        string email
        string telephone
        string specialite
    }
    
    COURS {
        int id_cours PK
        string nom_cours
        int coefficient
        string description
    }
    
    ADMINISTRATEUR {
        int id_admin PK
        string nom
        string prenom
        string email
        string login
        string mot_de_passe
    }
    
    TRIMESTRE {
        int id_trimestre PK
        int numero_trimestre
        date date_debut
        date date_fin
        int annee_scolaire
    }
    
    ANONYMAT {
        int id_anonymat PK
        string code_anonymat
        int id_eleve FK
        int id_trimestre FK
        int id_cours FK
    }
    
    NOTE {
        int id_note PK
        float note_controle_continu
        float note_examen
        int id_eleve FK
        int id_cours FK
        int id_trimestre FK
    }
    
    ENSEIGNER {
        int id_enseignant FK
        int id_cours FK
        int id_classe FK
    }
    
    %% Relations
    NIVEAU ||--o{ CLASSE : "contient"
    CLASSE ||--o{ ELEVE : "regroupe"
    
    %% Relation ternaire : Un enseignant enseigne un cours dans une classe
    ENSEIGNANT }o--o{ ENSEIGNER : "participe"
    COURS }o--o{ ENSEIGNER : "participe"
    CLASSE }o--o{ ENSEIGNER : "participe"
    
    %% Relations pour anonymat
    ELEVE ||--o{ ANONYMAT : "possède"
    TRIMESTRE ||--o{ ANONYMAT : "concerne"
    COURS ||--o{ ANONYMAT : "pour"
    
    %% Relations pour notes
    ELEVE ||--o{ NOTE : "obtient"
    COURS ||--o{ NOTE : "dans"
    TRIMESTRE ||--o{ NOTE : "au"
