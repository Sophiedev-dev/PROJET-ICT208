-- MySQL dump 10.13  Distrib 8.0.42, for Linux (x86_64)
--
-- Host: localhost    Database: gestion_scolaire
-- ------------------------------------------------------
-- Server version	8.0.42-0ubuntu0.24.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

CREATE DATABASE IF NOT EXISTS TP;
USE TP;


--
-- Table structure for table `classes`
--

DROP TABLE IF EXISTS `classes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `classes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(20) NOT NULL,
  `niveau_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `niveau_id` (`niveau_id`),
  CONSTRAINT `classes_ibfk_1` FOREIGN KEY (`niveau_id`) REFERENCES `niveaux` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `classes`
--

LOCK TABLES `classes` WRITE;
/*!40000 ALTER TABLE `classes` DISABLE KEYS */;
INSERT INTO `classes` VALUES (1,'CP-A',1),(2,'CP-B',1),(3,'CE1-A',2),(4,'CE1-B',2),(5,'6ème-A',6),(6,'6ème-B',6),(7,'5ème-A',7),(8,'5ème-B',7),(9,'2nde-G',10),(10,'2nde-T',10),(11,'1ère-S',11),(12,'Terminale-ES',12),(13,'4eme-A',8);
/*!40000 ALTER TABLE `classes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cours`
--

DROP TABLE IF EXISTS `cours`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cours` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(100) NOT NULL,
  `coefficient` int NOT NULL,
  `enseignant_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_cours_enseignants` (`enseignant_id`),
  CONSTRAINT `cours_ibfk_1` FOREIGN KEY (`enseignant_id`) REFERENCES `enseignants` (`id`),
  CONSTRAINT `fk_cours_enseignants` FOREIGN KEY (`enseignant_id`) REFERENCES `enseignants` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cours`
--

LOCK TABLES `cours` WRITE;
/*!40000 ALTER TABLE `cours` DISABLE KEYS */;
INSERT INTO `cours` VALUES (1,'Mathématiques',4,1),(2,'Français',3,2),(3,'Histoire-Géographie',2,3),(4,'Sciences',3,4),(5,'Anglais',2,5),(6,'EPS',1,6),(7,'Physique-Chimie',3,7),(8,'SVT',2,8),(13,'kkk',2,1),(15,'sss',1,1);
/*!40000 ALTER TABLE `cours` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cours_classes`
--

DROP TABLE IF EXISTS `cours_classes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cours_classes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `cours_id` int DEFAULT NULL,
  `classe_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `classe_id` (`classe_id`),
  KEY `fk_cours_classes_cours` (`cours_id`),
  CONSTRAINT `cours_classes_ibfk_2` FOREIGN KEY (`classe_id`) REFERENCES `classes` (`id`),
  CONSTRAINT `fk_cours_classes_cours` FOREIGN KEY (`cours_id`) REFERENCES `cours` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cours_classes`
--

LOCK TABLES `cours_classes` WRITE;
/*!40000 ALTER TABLE `cours_classes` DISABLE KEYS */;
INSERT INTO `cours_classes` VALUES (1,1,5),(2,1,6),(3,1,7),(4,1,8),(5,1,9),(6,1,10),(7,1,11),(8,1,12),(9,2,5),(10,2,6),(11,2,7),(12,2,8),(13,2,9),(14,2,10),(15,2,11),(16,2,12),(17,3,5),(18,3,6),(19,3,7),(20,3,8),(21,3,9),(22,3,10),(23,3,11),(24,3,12),(25,4,5),(26,4,6),(27,4,7),(28,4,8),(29,5,5),(30,5,6),(31,5,7),(32,5,8),(33,5,9),(34,5,10),(35,5,11),(36,5,12),(37,6,5),(38,6,6),(39,6,7),(40,6,8),(41,6,9),(42,6,10),(43,6,11),(44,6,12),(45,7,9),(46,7,10),(47,7,11),(48,7,12),(49,8,9),(50,8,10),(51,8,11),(52,8,12),(53,13,8),(55,15,1);
/*!40000 ALTER TABLE `cours_classes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `eleves`
--

DROP TABLE IF EXISTS `eleves`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `eleves` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(100) NOT NULL,
  `classe_id` int DEFAULT NULL,
  `id_anonymat` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `classe_id` (`classe_id`),
  CONSTRAINT `eleves_ibfk_1` FOREIGN KEY (`classe_id`) REFERENCES `classes` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `eleves`
--

LOCK TABLES `eleves` WRITE;
/*!40000 ALTER TABLE `eleves` DISABLE KEYS */;
INSERT INTO `eleves` VALUES (1,'Lucas Bernard',5,'6A001'),(2,'Emma Petit',5,'6A002'),(3,'Hugo Leroy',5,'6A003'),(4,'Chloé Moreau',8,'5B001'),(5,'Nathan Simon',8,'5B002'),(6,'Léa Laurent',8,'5B003'),(7,'Tom Richard',9,'2G001'),(8,'Manon Michel',9,'2G002'),(9,'Enzo Garcia',9,'2G003'),(10,'Camille David',12,'TES001'),(11,'Maxime Bertrand',12,'TES002'),(12,'Sarah Nguyen',12,'TES003'),(14,'russel sonwa',1,'A1751553960647'),(16,'sans sentiment',1,'A1751554085962'),(17,'lol',1,'A1751554249439'),(18,'12',4,'A1751555480907');
/*!40000 ALTER TABLE `eleves` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `enseignants`
--

DROP TABLE IF EXISTS `enseignants`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `enseignants` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `enseignants`
--

LOCK TABLES `enseignants` WRITE;
/*!40000 ALTER TABLE `enseignants` DISABLE KEYS */;
INSERT INTO `enseignants` VALUES (1,'Marie Dupon'),(2,'Jean Marti'),(3,'Sophie Lambert'),(4,'Pierre Durand'),(5,'Lucie Petit'),(6,'Thomas Moreau'),(7,'Amélie Roux'),(8,'Nicolas Lefebvre');
/*!40000 ALTER TABLE `enseignants` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `niveaux`
--

DROP TABLE IF EXISTS `niveaux`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `niveaux` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nom` (`nom`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `niveaux`
--

LOCK TABLES `niveaux` WRITE;
/*!40000 ALTER TABLE `niveaux` DISABLE KEYS */;
INSERT INTO `niveaux` VALUES (11,'1ère'),(10,'2nde'),(9,'3ème'),(8,'4ème'),(7,'5ème'),(6,'6ème'),(2,'CE1'),(3,'CE2'),(4,'CM1'),(5,'CM2'),(1,'CP'),(12,'Terminale');
/*!40000 ALTER TABLE `niveaux` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notes`
--

DROP TABLE IF EXISTS `notes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `eleve_id` int DEFAULT NULL,
  `cours_id` int DEFAULT NULL,
  `trimestre` int DEFAULT NULL,
  `note_cc` float DEFAULT NULL,
  `note_examen` float DEFAULT NULL,
  `moyenne` float DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `eleve_id` (`eleve_id`),
  KEY `fk_notes_cours` (`cours_id`),
  CONSTRAINT `fk_notes_cours` FOREIGN KEY (`cours_id`) REFERENCES `cours` (`id`) ON DELETE CASCADE,
  CONSTRAINT `notes_ibfk_1` FOREIGN KEY (`eleve_id`) REFERENCES `eleves` (`id`),
  CONSTRAINT `notes_chk_1` CHECK ((`trimestre` between 1 and 3))
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notes`
--

LOCK TABLES `notes` WRITE;
/*!40000 ALTER TABLE `notes` DISABLE KEYS */;
INSERT INTO `notes` VALUES (1,1,1,1,14.5,12,13.25),(2,1,2,1,12,15,13.5),(3,1,5,1,16,14.5,15.25),(4,4,1,1,18,17.5,17.75),(5,4,3,1,15,16,15.5),(6,4,6,1,20,18,19),(7,10,1,1,11,13.5,12.25),(8,10,7,1,14,12,13),(9,10,8,1,16,15,15.5),(10,2,1,1,10,12,11),(11,3,1,1,21,11,16);
/*!40000 ALTER TABLE `notes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `utilisateurs`
--

DROP TABLE IF EXISTS `utilisateurs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `utilisateurs` (
  `id` int NOT NULL AUTO_INCREMENT,
  `identifiant` varchar(50) NOT NULL,
  `mot_de_passe` varchar(100) NOT NULL,
  `role` enum('ADMINISTRATEUR','ENSEIGNANT','ELEVE') NOT NULL,
  `enseignant_id` int DEFAULT NULL,
  `eleve_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `identifiant` (`identifiant`),
  KEY `enseignant_id` (`enseignant_id`),
  KEY `eleve_id` (`eleve_id`),
  CONSTRAINT `utilisateurs_ibfk_1` FOREIGN KEY (`enseignant_id`) REFERENCES `enseignants` (`id`),
  CONSTRAINT `utilisateurs_ibfk_2` FOREIGN KEY (`eleve_id`) REFERENCES `eleves` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `utilisateurs`
--

LOCK TABLES `utilisateurs` WRITE;
/*!40000 ALTER TABLE `utilisateurs` DISABLE KEYS */;
INSERT INTO `utilisateurs` VALUES (15,'admin','admin123','ADMINISTRATEUR',NULL,NULL),(16,'m.dupont','math123','ENSEIGNANT',1,NULL),(17,'j.martin','french123','ENSEIGNANT',2,NULL),(18,'s.lambert','history123','ENSEIGNANT',3,NULL),(19,'6a001','eleve123','ELEVE',NULL,1),(20,'6a002','eleve123','ELEVE',NULL,2),(21,'tes001','eleve123','ELEVE',NULL,10);
/*!40000 ALTER TABLE `utilisateurs` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-03 20:45:52
