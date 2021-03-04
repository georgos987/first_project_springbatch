-- phpMyAdmin SQL Dump
-- version 5.1.0
-- https://www.phpmyadmin.net/
--
-- Host: mysql-service:3306
-- Erstellungszeit: 26. Feb 2021 um 15:03
-- Server-Version: 8.0.23
-- PHP-Version: 7.4.15

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Datenbank: `batch`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `person`
--

CREATE TABLE `person` (
  `id` bigint NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `birthday` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `revenue` bigint DEFAULT NULL,
  `customer` tinyint(1) DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Daten für Tabelle `person`
--

INSERT INTO `person` (`id`, `name`, `birthday`, `email`, `revenue`, `customer`) VALUES
(1, 'Daliah Shah', '1987-04-19', 'Daliah.Shah@domain.xyz', 3, 0),
(2, 'Wei Lang', '1988-04-19', 'Wei.Lang@domain.xyz', 5, 0),
(3, 'Zane Sanchez', '1987-09-19', 'Zane.Sanchez@domain.xyz', 0, 1),
(4, 'Sofia Carvalho', '2000-04-19', 'Sofia.Carvalho@domain.xyz', 6, 1),
(5, 'Avery Gill', '1980-04-19', 'Avery.Gill@domain.xyz', 9, 0),
(6, 'Georgos Makhool', '1980-01-01', 'Avery.Gill@domain.xyz', 4, 1);

--
-- Indizes der exportierten Tabellen
--

--
-- Indizes für die Tabelle `person`
--
ALTER TABLE `person`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT für exportierte Tabellen
--

--
-- AUTO_INCREMENT für Tabelle `person`
--
ALTER TABLE `person`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
