-- phpMyAdmin SQL Dump
-- version 4.5.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Apr 10, 2017 at 12:21 PM
-- Server version: 10.1.19-MariaDB
-- PHP Version: 5.6.28

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `parking_allocation`
--

-- --------------------------------------------------------

--
-- Table structure for table `bookinginfo`
--

CREATE TABLE `bookinginfo` (
  `bookid` int(20) NOT NULL,
  `uid` int(20) NOT NULL,
  `booking_time` varchar(50) NOT NULL,
  `booking_start_time` varchar(50) NOT NULL,
  `booking_end_time` varchar(50) NOT NULL,
  `parking_id` varchar(100) NOT NULL,
  `status` varchar(10) NOT NULL,
  `pay_type` varchar(10) NOT NULL,
  `user_lat` varchar(150) NOT NULL,
  `user_long` varchar(150) NOT NULL,
  `dest_lat` varchar(150) NOT NULL,
  `dest_long` varchar(150) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `bookinginfo`
--

INSERT INTO `bookinginfo` (`bookid`, `uid`, `booking_time`, `booking_start_time`, `booking_end_time`, `parking_id`, `status`, `pay_type`, `user_lat`, `user_long`, `dest_lat`, `dest_long`) VALUES
(1, 0, '', '', '', '', '', '', 'user_lat', 'user_long', 'dest_lat', 'dest_long'),
(2, 0, '', '', '', '', '', '', 'user_lat', 'user_long', 'dest_lat', 'dest_long'),
(3, 0, '', '', '', '', '', '', 'a', 'b', 'c', 'd'),
(4, 0, '', '', '', '', '', '', '22.5006067', 'b', 'c', 'd'),
(5, 0, '', '', '', '', '', '', '22.5006067', '88.3459423', 'c', 'd'),
(6, 0, '', '', '', '', '', '', '22.502389', '88.3467181', '22.510329499999997', '88.4151674'),
(7, 0, '', '', '', '', '', '', '22.501727', '88.3462458', '22.510329499999997', '88.4151674');

-- --------------------------------------------------------

--
-- Table structure for table `parking_slots`
--

CREATE TABLE `parking_slots` (
  `id` int(20) NOT NULL,
  `p_lat` varchar(20) NOT NULL,
  `p_long` varchar(20) NOT NULL,
  `p_id` varchar(500) NOT NULL,
  `p_name` varchar(100) NOT NULL,
  `no_of_slots` int(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `parking_slots`
--

INSERT INTO `parking_slots` (`id`, `p_lat`, `p_long`, `p_id`, `p_name`, `no_of_slots`) VALUES
(1, '22.5133251', '88.4032273', 'ChIJGxYlTP5zAjoRoay7KisCubM', 'Ruby General Hospital', 0),
(2, '22.5413065', '88.3961892', 'ChIJBZ8ab592AjoRu9R1cOXl1KM', 'Science City Bus Stop', 0),
(5, '22.5170076', '88.365819', 'ChIJGyc4wdR2AjoRvtXtzeOKOsg', 'Gariahat', 0),
(8, '22.518128', '88.3865642', 'Ej5SYXNoIEJlaGFyaSBBdmUsIFRhbCBCYWdhbiwgS2FzYmEsIEtvbGthdGEsIFdlc3QgQmVuZ2FsLCBJbmRpYQ', 'Tal Bagan, Rashbehari Avenue', 0),
(9, '22.5378858', '88.368246', 'ChIJPX-il912AjoRD1_zod3Xgv0', 'Park Circus', 0);

-- --------------------------------------------------------

--
-- Table structure for table `usertable`
--

CREATE TABLE `usertable` (
  `uid` int(20) NOT NULL,
  `username` varchar(100) NOT NULL,
  `password` varchar(512) NOT NULL,
  `mob_no` varchar(20) NOT NULL,
  `email` varchar(250) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `usertable`
--

INSERT INTO `usertable` (`uid`, `username`, `password`, `mob_no`, `email`) VALUES
(1, 'tanmoy', '21232f297a57a5a743894a0e4a801fc3', '8962', 'tan@gmail.com'),
(2, '123456', '81dc9bdb52d04dc20036dbd8313ed055', '1234567890', 'iamstk14@gmail.com'),
(3, 'xyzz', '81dc9bdb52d04dc20036dbd8313ed055', '1234567890', 'xyz@gmail.com');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `bookinginfo`
--
ALTER TABLE `bookinginfo`
  ADD PRIMARY KEY (`bookid`);

--
-- Indexes for table `parking_slots`
--
ALTER TABLE `parking_slots`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `usertable`
--
ALTER TABLE `usertable`
  ADD PRIMARY KEY (`uid`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `bookinginfo`
--
ALTER TABLE `bookinginfo`
  MODIFY `bookid` int(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;
--
-- AUTO_INCREMENT for table `parking_slots`
--
ALTER TABLE `parking_slots`
  MODIFY `id` int(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;
--
-- AUTO_INCREMENT for table `usertable`
--
ALTER TABLE `usertable`
  MODIFY `uid` int(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
