-- MySQL dump 10.13  Distrib 8.0.22, for Linux (x86_64)
--
-- Host: 127.0.0.1    Database: downloadmanager
-- ------------------------------------------------------
-- Server version	8.0.22-0ubuntu0.20.04.2

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

--
-- Table structure for table `file_info`
--

DROP TABLE IF EXISTS `file_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `file_info` (
  `idFile` int NOT NULL AUTO_INCREMENT,
  `fileName` text NOT NULL,
  `localPath` text NOT NULL,
  `urlPath` text NOT NULL,
  `downloaded` mediumtext,
  `numThreads` int DEFAULT NULL,
  `size` mediumtext,
  `dateTime` datetime DEFAULT NULL,
  `status` double DEFAULT NULL,
  PRIMARY KEY (`idFile`)
) ENGINE=InnoDB AUTO_INCREMENT=121 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

Create table `sub_file_info`
(
    `idSubFile`     int NOT NULL AUTO_INCREMENT,
    `startPosition` mediumtext,
    `downloaded`    mediumtext,
    `endPosition`   mediumtext,
    `idFile`        int,
    PRIMARY KEY (`idSubFile`),
    CONSTRAINT FK_sub_file_info_file_info FOREIGN KEY (`idFile`)
        REFERENCES `file_info` (`idFile`)
);

--
-- Dumping data for table `file_info`
--

LOCK TABLES `file_info` WRITE;
/*!40000 ALTER TABLE `file_info` DISABLE KEYS */;
INSERT INTO `file_info` VALUES (117,'20MB.zip','/home/duydoan/Downloads/','http://ipv4.download.thinkbroadband.com/20MB.zip','20700035',1,'20971520','2020-11-29 16:04:28',98),(118,'20MB.zip','/home/duydoan/Downloads/','http://ipv4.download.thinkbroadband.com:81/20MB.zip','20971520',1,'20971520','2020-11-29 16:16:25',100),(120,'20MB.zip','/home/duydoan/Documents/','http://ipv4.download.thinkbroadband.com:81/20MB.zip','5500172',1,'20971520','2020-11-29 16:21:15',26);
/*!40000 ALTER TABLE `file_info` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-11-29 23:54:06
