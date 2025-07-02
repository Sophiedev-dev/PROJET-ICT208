src/
├── model/             # 🧩 Modèles métiers (Objets)
│   ├── Eleve.java
│   ├── Enseignant.java
│   ├── Classe.java
│   ├── Niveau.java
│   ├── Cours.java
│   ├── Note.java
│   ├── Trimestre.java
│   ├── Utilisateur.java
│   ├── Role.java
│   └── Bulletin.java
│
├── dao/               # 🗃️ Data Access Objects (JDBC)
│   ├── EleveDAO.java
│   ├── EnseignantDAO.java
│   ├── ClasseDAO.java
│   ├── NiveauDAO.java
│   ├── CoursDAO.java
│   ├── NoteDAO.java
│   ├── UtilisateurDAO.java
│   └── ConnexionDB.java
│
├── controller/        # 🧠 Logique métier et coordination
│   ├── AdminController.java
│   ├── EnseignantController.java
│   ├── EleveController.java
│   └── AuthController.java
│
├── view/              # 🎨 Interface utilisateur (console ou Swing)
│   ├── MainConsole.java
│   ├── AdminMenu.java
│   ├── EnseignantMenu.java
│   ├── EleveMenu.java
│   └── LoginView.java
│
└── utils/             # 🔧 Outils divers
    ├── AnonymatGenerator.java
    ├── BulletinPrinter.java
    └── MoyenneUtils.java
