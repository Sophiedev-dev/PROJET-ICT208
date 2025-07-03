erDiagram

niveaux {
int id PK
varchar nom UNIQUE
}

classes {
int id PK
varchar nom
int niveau_id FK
}

cours {
int id PK
varchar nom
int coefficient
int enseignant_id FK
}

enseignants {
int id PK
varchar nom
}

cours_classes {
int id PK
int cours_id FK
int classe_id FK
}

eleves {
int id PK
varchar nom
int classe_id FK
varchar id_anonymat
}

utilisateurs {
int id PK
varchar identifiant UNIQUE
varchar mot_de_passe
enum role
int enseignant_id FK
int eleve_id FK
}

niveaux ||--o{ classes : "contient"
classes ||--o{ eleves : "a"
enseignants ||--o{ cours : "enseigne"
cours ||--o{ cours_classes : "assigné à"
classes ||--o{ cours_classes : "reçoit"
enseignants ||--|| utilisateurs : "peut être"
eleves ||--|| utilisateurs : "peut être"
