-- ========================================
-- SCRIPT SQL - SYSTÈME DE GESTION SCOLAIRE
-- ========================================

-- Création de la base de données
CREATE DATABASE IF NOT EXISTS gestion_scolaire;
USE gestion_scolaire;

-- Suppression des tables existantes (ordre inverse des dépendances)
DROP TABLE IF EXISTS NOTE;
DROP TABLE IF EXISTS ANONYMAT;
DROP TABLE IF EXISTS ENSEIGNER;
DROP TABLE IF EXISTS ELEVE;
DROP TABLE IF EXISTS CLASSE;
DROP TABLE IF EXISTS NIVEAU;
DROP TABLE IF EXISTS COURS;
DROP TABLE IF EXISTS ENSEIGNANT;
DROP TABLE IF EXISTS ADMINISTRATEUR;
DROP TABLE IF EXISTS TRIMESTRE;

-- ========================================
-- CRÉATION DES TABLES
-- ========================================

-- Table NIVEAU
CREATE TABLE NIVEAU (
    id_niveau INT PRIMARY KEY AUTO_INCREMENT,
    nom_niveau VARCHAR(50) NOT NULL UNIQUE,
    description TEXT
);

-- Table CLASSE
CREATE TABLE CLASSE (
    id_classe INT PRIMARY KEY AUTO_INCREMENT,
    nom_classe VARCHAR(50) NOT NULL,
    id_niveau INT NOT NULL,
    FOREIGN KEY (id_niveau) REFERENCES NIVEAU(id_niveau) ON DELETE CASCADE,
    UNIQUE KEY unique_classe_niveau (nom_classe, id_niveau)
);

-- Table ELEVE
CREATE TABLE ELEVE (
    id_eleve INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    date_naissance DATE NOT NULL,
    adresse TEXT,
    telephone VARCHAR(20),
    id_classe INT NOT NULL,
    FOREIGN KEY (id_classe) REFERENCES CLASSE(id_classe) ON DELETE RESTRICT,
    INDEX idx_nom_prenom (nom, prenom),
    INDEX idx_classe (id_classe)
);

-- Table ENSEIGNANT
CREATE TABLE ENSEIGNANT (
    id_enseignant INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    telephone VARCHAR(20),
    specialite VARCHAR(100),
    INDEX idx_email (email),
    INDEX idx_nom_prenom (nom, prenom)
);

-- Table COURS
CREATE TABLE COURS (
    id_cours INT PRIMARY KEY AUTO_INCREMENT,
    nom_cours VARCHAR(100) NOT NULL UNIQUE,
    coefficient INT NOT NULL CHECK (coefficient > 0),
    description TEXT,
    INDEX idx_nom_cours (nom_cours)
);

-- Table ADMINISTRATEUR
CREATE TABLE ADMINISTRATEUR (
    id_admin INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    login VARCHAR(50) UNIQUE NOT NULL,
    mot_de_passe VARCHAR(255) NOT NULL,
    INDEX idx_login (login),
    INDEX idx_email (email)
);

-- Table TRIMESTRE
CREATE TABLE TRIMESTRE (
    id_trimestre INT PRIMARY KEY AUTO_INCREMENT,
    numero_trimestre INT NOT NULL CHECK (numero_trimestre BETWEEN 1 AND 3),
    date_debut DATE NOT NULL,
    date_fin DATE NOT NULL,
    annee_scolaire INT NOT NULL,
    CHECK (date_fin > date_debut),
    UNIQUE KEY unique_trimestre_annee (numero_trimestre, annee_scolaire),
    INDEX idx_annee_scolaire (annee_scolaire)
);

-- Table ENSEIGNER (relation ternaire)
CREATE TABLE ENSEIGNER (
    id_enseignant INT NOT NULL,
    id_cours INT NOT NULL,
    id_classe INT NOT NULL,
    PRIMARY KEY (id_enseignant, id_cours, id_classe),
    FOREIGN KEY (id_enseignant) REFERENCES ENSEIGNANT(id_enseignant) ON DELETE CASCADE,
    FOREIGN KEY (id_cours) REFERENCES COURS(id_cours) ON DELETE CASCADE,
    FOREIGN KEY (id_classe) REFERENCES CLASSE(id_classe) ON DELETE CASCADE,
    INDEX idx_enseignant (id_enseignant),
    INDEX idx_cours (id_cours),
    INDEX idx_classe (id_classe)
);

-- Table ANONYMAT
CREATE TABLE ANONYMAT (
    id_anonymat INT PRIMARY KEY AUTO_INCREMENT,
    code_anonymat VARCHAR(20) NOT NULL UNIQUE,
    id_eleve INT NOT NULL,
    id_trimestre INT NOT NULL,
    id_cours INT NOT NULL,
    FOREIGN KEY (id_eleve) REFERENCES ELEVE(id_eleve) ON DELETE CASCADE,
    FOREIGN KEY (id_trimestre) REFERENCES TRIMESTRE(id_trimestre) ON DELETE CASCADE,
    FOREIGN KEY (id_cours) REFERENCES COURS(id_cours) ON DELETE CASCADE,
    UNIQUE KEY unique_anonymat_eleve_trimestre_cours (id_eleve, id_trimestre, id_cours),
    INDEX idx_code_anonymat (code_anonymat),
    INDEX idx_eleve_trimestre (id_eleve, id_trimestre)
);

-- Table NOTE
CREATE TABLE NOTE (
    id_note INT PRIMARY KEY AUTO_INCREMENT,
    note_controle_continu DECIMAL(4,2) CHECK (note_controle_continu BETWEEN 0 AND 20),
    note_examen DECIMAL(4,2) CHECK (note_examen BETWEEN 0 AND 20),
    id_eleve INT NOT NULL,
    id_cours INT NOT NULL,
    id_trimestre INT NOT NULL,
    FOREIGN KEY (id_eleve) REFERENCES ELEVE(id_eleve) ON DELETE CASCADE,
    FOREIGN KEY (id_cours) REFERENCES COURS(id_cours) ON DELETE CASCADE,
    FOREIGN KEY (id_trimestre) REFERENCES TRIMESTRE(id_trimestre) ON DELETE CASCADE,
    UNIQUE KEY unique_note_eleve_cours_trimestre (id_eleve, id_cours, id_trimestre),
    INDEX idx_eleve_trimestre (id_eleve, id_trimestre),
    INDEX idx_cours_trimestre (id_cours, id_trimestre)
);

-- ========================================
-- VUES UTILES
-- ========================================

-- Vue pour calculer les moyennes des élèves par trimestre
CREATE VIEW vue_moyennes_eleves AS
SELECT 
    n.id_eleve,
    e.nom,
    e.prenom,
    n.id_trimestre,
    n.id_cours,
    c.nom_cours,
    c.coefficient,
    n.note_controle_continu,
    n.note_examen,
    ROUND((COALESCE(n.note_controle_continu, 0) + COALESCE(n.note_examen, 0)) / 2, 2) as moyenne_trimestre
FROM NOTE n
JOIN ELEVE e ON n.id_eleve = e.id_eleve
JOIN COURS c ON n.id_cours = c.id_cours;

-- Vue pour calculer les effectifs par classe
CREATE VIEW vue_effectifs_classes AS
SELECT 
    cl.id_classe,
    cl.nom_classe,
    n.nom_niveau,
    COUNT(e.id_eleve) as effectif
FROM CLASSE cl
LEFT JOIN ELEVE e ON cl.id_classe = e.id_classe
JOIN NIVEAU n ON cl.id_niveau = n.id_niveau
GROUP BY cl.id_classe, cl.nom_classe, n.nom_niveau;

-- Vue pour les enseignants et leurs classes
CREATE VIEW vue_enseignants_classes AS
SELECT 
    ens.id_enseignant,
    ens.nom as nom_enseignant,
    ens.prenom as prenom_enseignant,
    c.nom_cours,
    cl.nom_classe,
    n.nom_niveau
FROM ENSEIGNANT ens
JOIN ENSEIGNER e ON ens.id_enseignant = e.id_enseignant
JOIN COURS c ON e.id_cours = c.id_cours
JOIN CLASSE cl ON e.id_classe = cl.id_classe
JOIN NIVEAU n ON cl.id_niveau = n.id_niveau;

-- ========================================
-- DONNÉES DE TEST
-- ========================================

-- Insertion des niveaux
INSERT INTO NIVEAU (nom_niveau, description) VALUES 
('6ème', 'Sixième année du collège'),
('5ème', 'Cinquième année du collège'),
('4ème', 'Quatrième année du collège'),
('3ème', 'Troisième année du collège');

-- Insertion des classes
INSERT INTO CLASSE (nom_classe, id_niveau) VALUES 
('6èmeA', 1), ('6èmeB', 1),
('5èmeA', 2), ('5èmeB', 2),
('4èmeA', 3), ('4èmeB', 3),
('3èmeA', 4), ('3èmeB', 4);

-- Insertion des cours
INSERT INTO COURS (nom_cours, coefficient, description) VALUES 
('Mathématiques', 4, 'Cours de mathématiques'),
('Français', 4, 'Cours de français'),
('Histoire-Géographie', 3, 'Cours d\'histoire et géographie'),
('Sciences Physiques', 3, 'Cours de physique-chimie'),
('SVT', 2, 'Sciences de la Vie et de la Terre'),
('Anglais', 3, 'Cours d\'anglais'),
('EPS', 2, 'Éducation Physique et Sportive');

-- Insertion des enseignants
INSERT INTO ENSEIGNANT (nom, prenom, email, telephone, specialite) VALUES 
('MARTIN', 'Jean', 'j.martin@college.fr', '0123456789', 'Mathématiques'),
('DURAND', 'Marie', 'm.durand@college.fr', '0123456790', 'Français'),
('MOREAU', 'Pierre', 'p.moreau@college.fr', '0123456791', 'Histoire-Géographie'),
('BERNARD', 'Sophie', 's.bernard@college.fr', '0123456792', 'Sciences Physiques'),
('ROBERT', 'Paul', 'p.robert@college.fr', '0123456793', 'SVT'),
('PETIT', 'Anne', 'a.petit@college.fr', '0123456794', 'Anglais'),
('RICHARD', 'Marc', 'm.richard@college.fr', '0123456795', 'EPS');

-- Insertion des administrateurs
INSERT INTO ADMINISTRATEUR (nom, prenom, email, login, mot_de_passe) VALUES 
('ADMIN', 'Système', 'admin@college.fr', 'admin', '$2a$10$N9qo8uLOickgx2ZMRZoMye'), -- mot de passe: "password"
('DIRECTEUR', 'Principal', 'directeur@college.fr', 'directeur', '$2a$10$N9qo8uLOickgx2ZMRZoMye');

-- Insertion des trimestres pour l'année scolaire 2024-2025
INSERT INTO TRIMESTRE (numero_trimestre, date_debut, date_fin, annee_scolaire) VALUES 
(1, '2024-09-01', '2024-12-20', 2024),
(2, '2025-01-06', '2025-04-04', 2024),
(3, '2025-04-21', '2025-07-05', 2024);

-- Quelques élèves de test
INSERT INTO ELEVE (nom, prenom, date_naissance, adresse, telephone, id_classe) VALUES 
('DUPONT', 'Alice', '2011-03-15', '123 Rue de la Paix, Paris', '0123456700', 1),
('MARTIN', 'Bob', '2011-07-22', '456 Avenue des Champs, Lyon', '0123456701', 1),
('GARCIA', 'Clara', '2011-11-10', '789 Boulevard Saint-Michel, Marseille', '0123456702', 1),
('LOPEZ', 'David', '2010-01-08', '321 Rue Victor Hugo, Toulouse', '0123456703', 3),
('SILVA', 'Emma', '2010-05-30', '654 Place de la République, Nice', '0123456704', 3);

-- Attribution des enseignants aux cours et classes
INSERT INTO ENSEIGNER (id_enseignant, id_cours, id_classe) VALUES 
-- Mathématiques
(1, 1, 1), (1, 1, 2), (1, 1, 3), (1, 1, 4),
-- Français  
(2, 2, 1), (2, 2, 2), (2, 2, 3), (2, 2, 4),
-- Histoire-Géographie
(3, 3, 1), (3, 3, 2), (3, 3, 3), (3, 3, 4),
-- Sciences Physiques
(4, 4, 3), (4, 4, 4), (4, 4, 5), (4, 4, 6),
-- SVT
(5, 5, 1), (5, 5, 2), (5, 5, 3), (5, 5, 4),
-- Anglais
(6, 6, 1), (6, 6, 2), (6, 6, 3), (6, 6, 4),
-- EPS
(7, 7, 1), (7, 7, 2), (7, 7, 3), (7, 7, 4);

-- ========================================
-- PROCÉDURES STOCKÉES UTILES
-- ========================================

DELIMITER //

-- Procédure pour calculer la moyenne générale d'un élève pour un trimestre
CREATE PROCEDURE CalculerMoyenneGenerale(
    IN p_id_eleve INT,
    IN p_id_trimestre INT,
    OUT p_moyenne_generale DECIMAL(4,2)
)
BEGIN
    SELECT 
        ROUND(
            SUM((COALESCE(note_controle_continu, 0) + COALESCE(note_examen, 0)) / 2 * c.coefficient) / 
            SUM(c.coefficient), 2
        ) INTO p_moyenne_generale
    FROM NOTE n
    JOIN COURS c ON n.id_cours = c.id_cours
    WHERE n.id_eleve = p_id_eleve AND n.id_trimestre = p_id_trimestre;
END //

-- Procédure pour générer un code anonymat unique
CREATE PROCEDURE GenererCodeAnonymat(
    IN p_id_eleve INT,
    IN p_id_trimestre INT,
    IN p_id_cours INT,
    OUT p_code_anonymat VARCHAR(20)
)
BEGIN
    DECLARE v_code VARCHAR(20);
    DECLARE v_existe INT DEFAULT 1;
    
    WHILE v_existe > 0 DO
        SET v_code = CONCAT('ANO', LPAD(FLOOR(RAND() * 999999), 6, '0'));
        SELECT COUNT(*) INTO v_existe FROM ANONYMAT WHERE code_anonymat = v_code;
    END WHILE;
    
    INSERT INTO ANONYMAT (code_anonymat, id_eleve, id_trimestre, id_cours)
    VALUES (v_code, p_id_eleve, p_id_trimestre, p_id_cours);
    
    SET p_code_anonymat = v_code;
END //

DELIMITER ;

-- ========================================
-- REQUÊTES UTILES POUR LES BULLETINS
-- ========================================

-- Requête pour obtenir toutes les notes d'un élève pour un trimestre
/*
SELECT 
    e.nom, e.prenom,
    c.nom_cours,
    c.coefficient,
    n.note_controle_continu,
    n.note_examen,
    ROUND((COALESCE(n.note_controle_continu, 0) + COALESCE(n.note_examen, 0)) / 2, 2) as moyenne_matiere
FROM NOTE n
JOIN ELEVE e ON n.id_eleve = e.id_eleve
JOIN COURS c ON n.id_cours = c.id_cours
WHERE n.id_eleve = ? AND n.id_trimestre = ?
ORDER BY c.nom_cours;
*/

-- Requête pour calculer le rang d'un élève dans sa classe pour un trimestre
/*
SELECT 
    rang
FROM (
    SELECT 
        e.id_eleve,
        ROW_NUMBER() OVER (ORDER BY 
            SUM((COALESCE(n.note_controle_continu, 0) + COALESCE(n.note_examen, 0)) / 2 * c.coefficient) / 
            SUM(c.coefficient) DESC
        ) as rang
    FROM ELEVE e
    JOIN NOTE n ON e.id_eleve = n.id_eleve
    JOIN COURS c ON n.id_cours = c.id_cours
    WHERE e.id_classe = ? AND n.id_trimestre = ?
    GROUP BY e.id_eleve
) ranking
WHERE id_eleve = ?;
*/

COMMIT;
