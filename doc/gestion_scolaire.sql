CREATE DATABASE IF NOT EXISTS gestion_scolaire;
USE gestion_scolaire;

-- Table niveaux
CREATE TABLE niveaux (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(20) NOT NULL UNIQUE
);

-- Table classes
CREATE TABLE classes (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(20) NOT NULL,
    niveau_id INT,
    FOREIGN KEY (niveau_id) REFERENCES niveaux(id)
);

-- Table enseignants
CREATE TABLE enseignants (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL
);

-- Table cours
CREATE TABLE cours (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
    coefficient INT NOT NULL,
    enseignant_id INT,
    FOREIGN KEY (enseignant_id) REFERENCES enseignants(id)
);

-- Table cours_classes (relation n-n entre cours et classes)
CREATE TABLE cours_classes (
    id INT PRIMARY KEY AUTO_INCREMENT,
    cours_id INT,
    classe_id INT,
    FOREIGN KEY (cours_id) REFERENCES cours(id),
    FOREIGN KEY (classe_id) REFERENCES classes(id)
);

-- Table élèves
CREATE TABLE eleves (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
    classe_id INT,
    id_anonymat VARCHAR(20),
    FOREIGN KEY (classe_id) REFERENCES classes(id)
);

-- Table utilisateurs
CREATE TABLE utilisateurs (
    id INT PRIMARY KEY AUTO_INCREMENT,
    identifiant VARCHAR(50) NOT NULL UNIQUE,
    mot_de_passe VARCHAR(100) NOT NULL,
    role ENUM('ADMINISTRATEUR', 'ENSEIGNANT', 'ELEVE') NOT NULL,
    enseignant_id INT,
    eleve_id INT,
    FOREIGN KEY (enseignant_id) REFERENCES enseignants(id),
    FOREIGN KEY (eleve_id) REFERENCES eleves(id)
);

-- Table notes
CREATE TABLE notes (
    id INT PRIMARY KEY AUTO_INCREMENT,
    eleve_id INT,
    cours_id INT,
    trimestre INT CHECK (trimestre BETWEEN 1 AND 3),
    note_cc FLOAT,
    note_examen FLOAT,
    moyenne FLOAT,
    FOREIGN KEY (eleve_id) REFERENCES eleves(id),
    FOREIGN KEY (cours_id) REFERENCES cours(id)
);


-- Supprimez d'abord les contraintes existantes (si nécessaire)
ALTER TABLE cours_classes DROP FOREIGN KEY cours_classes_ibfk_1;
ALTER TABLE notes DROP FOREIGN KEY notes_ibfk_2;

-- Modification pour la table cours_classes
ALTER TABLE cours_classes 
ADD CONSTRAINT fk_cours_classes_cours
FOREIGN KEY (cours_id) REFERENCES cours(id)
ON DELETE CASCADE;

-- Modification pour la table notes
ALTER TABLE notes
ADD CONSTRAINT fk_notes_cours
FOREIGN KEY (cours_id) REFERENCES cours(id)
ON DELETE CASCADE;

-- Niveaux
INSERT INTO niveaux (nom) VALUES 
('CP'), ('CE1'), ('CE2'), ('CM1'), ('CM2'), 
('6ème'), ('5ème'), ('4ème'), ('3ème'),
('2nde'), ('1ère'), ('Terminale');

-- Classes
INSERT INTO classes (nom, niveau_id) VALUES
('CP-A', 1), ('CP-B', 1),
('CE1-A', 2), ('CE1-B', 2),
('6ème-A', 6), ('6ème-B', 6),
('5ème-A', 7), ('5ème-B', 7),
('2nde-G', 10), ('2nde-T', 10),
('1ère-S', 11), ('Terminale-ES', 12);

-- Enseignants
INSERT INTO enseignants (nom) VALUES
('Marie Dupont'), ('Jean Martin'), ('Sophie Lambert'),
('Pierre Durand'), ('Lucie Petit'), ('Thomas Moreau'),
('Amélie Roux'), ('Nicolas Lefebvre');

-- Cours
INSERT INTO cours (nom, coefficient, enseignant_id) VALUES
('Mathématiques', 4, 1),
('Français', 3, 2),
('Histoire-Géographie', 2, 3),
('Sciences', 3, 4),
('Anglais', 2, 5),
('EPS', 1, 6),
('Physique-Chimie', 3, 7),
('SVT', 2, 8);

-- Cours_Classes (assignation des cours aux classes)
INSERT INTO cours_classes (cours_id, classe_id) VALUES
(1, 5), (1, 6), (1, 7), (1, 8), (1, 9), (1, 10), (1, 11), (1, 12),
(2, 5), (2, 6), (2, 7), (2, 8), (2, 9), (2, 10), (2, 11), (2, 12),
(3, 5), (3, 6), (3, 7), (3, 8), (3, 9), (3, 10), (3, 11), (3, 12),
(4, 5), (4, 6), (4, 7), (4, 8),
(5, 5), (5, 6), (5, 7), (5, 8), (5, 9), (5, 10), (5, 11), (5, 12),
(6, 5), (6, 6), (6, 7), (6, 8), (6, 9), (6, 10), (6, 11), (6, 12),
(7, 9), (7, 10), (7, 11), (7, 12),
(8, 9), (8, 10), (8, 11), (8, 12);

-- Elèves (exemples pour quelques classes)
INSERT INTO eleves (nom, classe_id, id_anonymat) VALUES
-- 6ème-A
('Lucas Bernard', 5, '6A001'),
('Emma Petit', 5, '6A002'),
('Hugo Leroy', 5, '6A003'),

-- 5ème-B
('Chloé Moreau', 8, '5B001'),
('Nathan Simon', 8, '5B002'),
('Léa Laurent', 8, '5B003'),

-- 2nde-G
('Tom Richard', 9, '2G001'),
('Manon Michel', 9, '2G002'),
('Enzo Garcia', 9, '2G003'),

-- Terminale-ES
('Camille David', 12, 'TES001'),
('Maxime Bertrand', 12, 'TES002'),
('Sarah Nguyen', 12, 'TES003');

-- Utilisateurs
INSERT INTO utilisateurs (identifiant, mot_de_passe, role, enseignant_id, eleve_id) VALUES
-- Administrateur
('admin', SHA2('admin123', 256), 'ADMINISTRATEUR', NULL, NULL),

-- Enseignants
('m.dupont', SHA2('math123', 256), 'ENSEIGNANT', 1, NULL),
('j.martin', SHA2('french123', 256), 'ENSEIGNANT', 2, NULL),
('s.lambert', SHA2('history123', 256), 'ENSEIGNANT', 3, NULL),

-- Elèves
('6a001', SHA2('eleve123', 256), 'ELEVE', NULL, 1),
('6a002', SHA2('eleve123', 256), 'ELEVE', NULL, 2),
('tes001', SHA2('eleve123', 256), 'ELEVE', NULL, 10);

-- Notes (exemples pour quelques élèves)
INSERT INTO notes (eleve_id, cours_id, trimestre, note_cc, note_examen, moyenne) VALUES
-- Notes pour Lucas Bernard (6ème-A)
(1, 1, 1, 14.5, 12.0, 13.25),
(1, 2, 1, 12.0, 15.0, 13.5),
(1, 5, 1, 16.0, 14.5, 15.25),

-- Notes pour Chloé Moreau (5ème-B)
(4, 1, 1, 18.0, 17.5, 17.75),
(4, 3, 1, 15.0, 16.0, 15.5),
(4, 6, 1, 20.0, 18.0, 19.0),

-- Notes pour Camille David (Terminale-ES)
(10, 1, 1, 11.0, 13.5, 12.25),
(10, 7, 1, 14.0, 12.0, 13.0),
(10, 8, 1, 16.0, 15.0, 15.5);