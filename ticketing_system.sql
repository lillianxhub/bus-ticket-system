CREATE DATABASE  IF NOT EXISTS `ticketing_system` /*!40100 DEFAULT CHARACTER SET latin1 */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `ticketing_system`;
-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: localhost    Database: ticketing_system
-- ------------------------------------------------------
-- Server version	9.2.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `bookings`
--

DROP TABLE IF EXISTS `bookings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bookings` (
  `bookingID` int NOT NULL AUTO_INCREMENT,
  `userID` int NOT NULL,
  `scheduleID` int DEFAULT NULL,
  `seatID` int DEFAULT NULL,
  `travelDate` date NOT NULL,
  `bookingDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `totalFare` decimal(10,2) NOT NULL,
  `status` enum('pending','confirmed') DEFAULT 'pending',
  PRIMARY KEY (`bookingID`),
  UNIQUE KEY `schedule_seat_date` (`scheduleID`,`seatID`,`travelDate`),
  KEY `id` (`userID`),
  KEY `seatID` (`seatID`),
  CONSTRAINT `bookings_ibfk_1` FOREIGN KEY (`userID`) REFERENCES `users` (`id`),
  CONSTRAINT `bookings_ibfk_2` FOREIGN KEY (`scheduleID`) REFERENCES `schedules` (`scheduleID`),
  CONSTRAINT `bookings_ibfk_3` FOREIGN KEY (`seatID`) REFERENCES `seats` (`seatID`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bookings`
--

LOCK TABLES `bookings` WRITE;
/*!40000 ALTER TABLE `bookings` DISABLE KEYS */;
INSERT INTO `bookings` VALUES (17,1,1,1,'2025-03-11','2025-03-11 09:24:31',600.00,'pending'),(18,1,1,2,'2025-03-11','2025-03-11 09:27:05',600.00,'pending'),(19,1,1,3,'2025-03-11','2025-03-11 09:27:05',600.00,'pending'),(33,1,1,28,'2025-03-11','2025-03-11 12:52:52',600.00,'pending'),(34,1,1,29,'2025-03-11','2025-03-11 12:52:52',600.00,'pending'),(35,1,1,30,'2025-03-11','2025-03-11 12:52:52',600.00,'pending'),(36,1,1,1,'2025-03-12','2025-03-11 12:53:06',600.00,'pending'),(37,1,1,2,'2025-03-12','2025-03-11 12:53:06',600.00,'pending'),(38,1,1,3,'2025-03-12','2025-03-11 12:53:06',600.00,'pending'),(39,1,1,1,'2025-03-13','2025-03-11 12:53:28',600.00,'pending'),(40,1,1,2,'2025-03-13','2025-03-11 12:53:28',600.00,'pending'),(41,1,1,3,'2025-03-13','2025-03-11 12:53:28',600.00,'pending');
/*!40000 ALTER TABLE `bookings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bus`
--

DROP TABLE IF EXISTS `bus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bus` (
  `busID` int NOT NULL AUTO_INCREMENT,
  `busName` varchar(50) NOT NULL,
  `busType` enum('GOLD_CLASS','FIRST_CLASS') DEFAULT NULL,
  PRIMARY KEY (`busID`),
  UNIQUE KEY `busName` (`busName`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bus`
--

LOCK TABLES `bus` WRITE;
/*!40000 ALTER TABLE `bus` DISABLE KEYS */;
INSERT INTO `bus` VALUES (1,'E-san','GOLD_CLASS'),(2,'Northern ','FIRST_CLASS');
/*!40000 ALTER TABLE `bus` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `schedules`
--

DROP TABLE IF EXISTS `schedules`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `schedules` (
  `scheduleID` int NOT NULL AUTO_INCREMENT,
  `busID` int NOT NULL,
  `origin` varchar(255) NOT NULL,
  `destination` varchar(255) NOT NULL,
  `departureTime` time NOT NULL,
  `arrivalTime` time NOT NULL,
  `fare` decimal(10,2) NOT NULL,
  PRIMARY KEY (`scheduleID`),
  KEY `busID` (`busID`),
  CONSTRAINT `schedules_ibfk_1` FOREIGN KEY (`busID`) REFERENCES `bus` (`busID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schedules`
--

LOCK TABLES `schedules` WRITE;
/*!40000 ALTER TABLE `schedules` DISABLE KEYS */;
INSERT INTO `schedules` VALUES (1,1,'Bangkok','Chiang-mai','00:00:00','01:00:00',600.00),(2,1,'Chiang-mai','Bangkok','01:00:00','02:00:00',620.00),(3,1,'Bangkok','Chiang-mai','02:00:00','03:00:00',610.00);
/*!40000 ALTER TABLE `schedules` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `seats`
--

DROP TABLE IF EXISTS `seats`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `seats` (
  `seatID` int NOT NULL AUTO_INCREMENT,
  `seatCode` char(2) DEFAULT NULL,
  PRIMARY KEY (`seatID`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `seats`
--

LOCK TABLES `seats` WRITE;
/*!40000 ALTER TABLE `seats` DISABLE KEYS */;
INSERT INTO `seats` VALUES (1,'0A'),(2,'0B'),(3,'0C'),(4,'1A'),(5,'1B'),(6,'1C'),(7,'2A'),(8,'2B'),(9,'2C'),(10,'3A'),(11,'3B'),(12,'3C'),(13,'4A'),(14,'4B'),(15,'4C'),(16,'5A'),(17,'5B'),(18,'5C'),(19,'6A'),(20,'6B'),(21,'6C'),(22,'7A'),(23,'7B'),(24,'7C'),(25,'8A'),(26,'8B'),(27,'8C'),(28,'9A'),(29,'9B'),(30,'9C');
/*!40000 ALTER TABLE `seats` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `fullName` varchar(255) NOT NULL,
  `address` text,
  `phone` varchar(20) DEFAULT NULL,
  `createdAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `active` tinyint(1) DEFAULT '1',
  `role` enum('ADMIN','CUSTOMER') DEFAULT 'CUSTOMER',
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  CONSTRAINT `chk_email` CHECK ((`email` like _utf8mb4'%@%.%'))
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'test','1234','test@gmail.com','test','test','0112223333','2025-03-10 01:57:40',1,'CUSTOMER'),(2,'j.doe','1234','johndoe@gmail.com','john doe','asdfklasd;jf','0998887777','2025-03-10 19:15:19',1,'CUSTOMER');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-03-11 19:54:41
