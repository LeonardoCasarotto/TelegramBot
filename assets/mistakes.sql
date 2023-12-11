-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Dec 11, 2023 at 09:39 PM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `patenteBot`
--

-- --------------------------------------------------------

--
-- Table structure for table `mistakes`
--

CREATE TABLE `mistakes` (
  `chatId` bigint(20) NOT NULL,
  `date` datetime NOT NULL,
  `mistakes` int(11) NOT NULL,
  `totalQuestions` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `mistakes`
--

INSERT INTO `mistakes` (`chatId`, `date`, `mistakes`, `totalQuestions`) VALUES
(6072829831, '2023-12-11 21:36:04', 0, 0),
(6072829831, '2023-12-11 21:36:19', 1, 5),
(6072829831, '2023-12-11 21:37:21', 2, 5),
(6072829831, '2023-12-11 21:37:28', 2, 5);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
