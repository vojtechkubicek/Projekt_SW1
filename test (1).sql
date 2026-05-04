-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Počítač: 127.0.0.1
-- Vytvořeno: Pon 04. kvě 2026, 07:24
-- Verze serveru: 10.4.32-MariaDB
-- Verze PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Databáze: `test`
--

-- --------------------------------------------------------

--
-- Struktura tabulky `authors`
--

CREATE TABLE `authors` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Vypisuji data pro tabulku `authors`
--

INSERT INTO `authors` (`id`, `name`) VALUES
(1, 'George Orwell'),
(2, 'Andrzej Sapkowski'),
(3, 'George R. R. Martin'),
(4, 'F. Scott Fitzgerald'),
(5, 'J.R.R. Tolkien'),
(6, 'Antoine de Saint-Exupéry');

-- --------------------------------------------------------

--
-- Struktura tabulky `book`
--

CREATE TABLE `book` (
  `available` bit(1) NOT NULL,
  `publish_year` int(11) DEFAULT NULL,
  `id` bigint(20) NOT NULL,
  `title` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Vypisuji data pro tabulku `book`
--

INSERT INTO `book` (`available`, `publish_year`, `id`, `title`) VALUES
(b'1', 1949, 1, '1984'),
(b'1', 1993, 2, 'Zaklínač: Poslední přání'),
(b'1', 1996, 3, 'Hra o trůny'),
(b'1', 1925, 4, 'Velký Gatsby'),
(b'1', 1937, 5, 'Hobit'),
(b'1', 1943, 6, 'Malý princ'),
(b'1', 1945, 7, 'Farma zvířat');

-- --------------------------------------------------------

--
-- Struktura tabulky `book_author`
--

CREATE TABLE `book_author` (
  `author_id` bigint(20) NOT NULL,
  `book_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Vypisuji data pro tabulku `book_author`
--

INSERT INTO `book_author` (`author_id`, `book_id`) VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4),
(5, 5),
(6, 6),
(1, 7);

-- --------------------------------------------------------

--
-- Struktura tabulky `departments`
--

CREATE TABLE `departments` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Vypisuji data pro tabulku `departments`
--

INSERT INTO `departments` (`id`, `name`) VALUES
(2, 'Marketing'),
(1, 'Vývoj softwaru'),
(3, 'Vývojové oddělení');

-- --------------------------------------------------------

--
-- Struktura tabulky `loans`
--

CREATE TABLE `loans` (
  `loan_date` date DEFAULT NULL,
  `return_date` date DEFAULT NULL,
  `book_id` bigint(20) DEFAULT NULL,
  `id` bigint(20) NOT NULL,
  `user_id` varchar(36) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Struktura tabulky `roles`
--

CREATE TABLE `roles` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Vypisuji data pro tabulku `roles`
--

INSERT INTO `roles` (`id`, `name`) VALUES
(4, 'DEVELOPER'),
(2, 'ROLE_ADMIN'),
(3, 'ROLE_READER'),
(1, 'ROLE_USER');

-- --------------------------------------------------------

--
-- Struktura tabulky `users`
--

CREATE TABLE `users` (
  `age` int(11) NOT NULL,
  `department_id` bigint(20) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `id` varchar(36) NOT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Vypisuji data pro tabulku `users`
--

INSERT INTO `users` (`age`, `department_id`, `email`, `first_name`, `id`, `last_name`, `password`, `username`) VALUES
(0, NULL, 'admin@knihovna.cz', NULL, '3b2681c0-923f-480b-954e-5b3e26af2deb', NULL, '$2a$10$F5PFWoz.7IBOs0GLZLydp.XBd.7Ro7B/IzXkgja.QKKQPneQe/f2K', 'admin'),
(28, 1, 'john.doe@example.com', 'John', '550e8400-e29b-41d4-a716-446655440001', 'Doe', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lpk1C3G6', 'john_doe'),
(34, 2, 'jane.smith@example.com', 'Jane', '550e8400-e29b-41d4-a716-446655440002', 'Smith', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lpk1C3G6', 'jane_smith'),
(45, 1, 'bob.wilson@example.com', 'Bob', '550e8400-e29b-41d4-a716-446655440003', 'Wilson', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lpk1C3G6', 'bob_wilson'),
(25, 3, 'petr@osu.cz', 'Petr', '78425ab7-8f24-46b9-a12f-85bd818bf61f', 'Tester', 'heslo123', 'dev-f5ac'),
(0, NULL, 'ctenar@knihovna.cz', NULL, 'f087fc92-e289-472d-9cf3-e882a09befec', NULL, '$2a$10$2jL/xnX7GVW9lbDLlp7vwuzV8D6yiWlf2VkugYUe18.cA0NPYj6pO', 'ctenar');

-- --------------------------------------------------------

--
-- Struktura tabulky `user_roles`
--

CREATE TABLE `user_roles` (
  `role_id` bigint(20) NOT NULL,
  `user_id` varchar(36) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Vypisuji data pro tabulku `user_roles`
--

INSERT INTO `user_roles` (`role_id`, `user_id`) VALUES
(1, '550e8400-e29b-41d4-a716-446655440001'),
(1, '550e8400-e29b-41d4-a716-446655440002'),
(1, '550e8400-e29b-41d4-a716-446655440003'),
(2, '3b2681c0-923f-480b-954e-5b3e26af2deb'),
(2, '550e8400-e29b-41d4-a716-446655440001'),
(3, '3b2681c0-923f-480b-954e-5b3e26af2deb'),
(3, 'f087fc92-e289-472d-9cf3-e882a09befec'),
(4, '78425ab7-8f24-46b9-a12f-85bd818bf61f');

--
-- Indexy pro exportované tabulky
--

--
-- Indexy pro tabulku `authors`
--
ALTER TABLE `authors`
  ADD PRIMARY KEY (`id`);

--
-- Indexy pro tabulku `book`
--
ALTER TABLE `book`
  ADD PRIMARY KEY (`id`);

--
-- Indexy pro tabulku `book_author`
--
ALTER TABLE `book_author`
  ADD KEY `FKro54jqpth9cqm1899dnuu9lqg` (`author_id`),
  ADD KEY `FKhwgu59n9o80xv75plf9ggj7xn` (`book_id`);

--
-- Indexy pro tabulku `departments`
--
ALTER TABLE `departments`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKj6cwks7xecs5jov19ro8ge3qk` (`name`);

--
-- Indexy pro tabulku `loans`
--
ALTER TABLE `loans`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKrejr2o36qqkd4il8ri31vbkn` (`book_id`),
  ADD KEY `FK6xxlcjc0rqtn5nq28vjnx5t9d` (`user_id`);

--
-- Indexy pro tabulku `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKofx66keruapi6vyqpv6f2or37` (`name`);

--
-- Indexy pro tabulku `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`),
  ADD UNIQUE KEY `UKr43af9ap4edm43mmtq01oddj6` (`username`),
  ADD KEY `FKsbg59w8q63i0oo53rlgvlcnjq` (`department_id`);

--
-- Indexy pro tabulku `user_roles`
--
ALTER TABLE `user_roles`
  ADD PRIMARY KEY (`role_id`,`user_id`),
  ADD KEY `FKhfh9dx7w3ubf1co1vdev94g3f` (`user_id`);

--
-- AUTO_INCREMENT pro tabulky
--

--
-- AUTO_INCREMENT pro tabulku `authors`
--
ALTER TABLE `authors`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT pro tabulku `book`
--
ALTER TABLE `book`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT pro tabulku `departments`
--
ALTER TABLE `departments`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pro tabulku `loans`
--
ALTER TABLE `loans`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pro tabulku `roles`
--
ALTER TABLE `roles`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Omezení pro exportované tabulky
--

--
-- Omezení pro tabulku `book_author`
--
ALTER TABLE `book_author`
  ADD CONSTRAINT `FKhwgu59n9o80xv75plf9ggj7xn` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`),
  ADD CONSTRAINT `FKro54jqpth9cqm1899dnuu9lqg` FOREIGN KEY (`author_id`) REFERENCES `authors` (`id`);

--
-- Omezení pro tabulku `loans`
--
ALTER TABLE `loans`
  ADD CONSTRAINT `FK6xxlcjc0rqtn5nq28vjnx5t9d` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `FKrejr2o36qqkd4il8ri31vbkn` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`);

--
-- Omezení pro tabulku `users`
--
ALTER TABLE `users`
  ADD CONSTRAINT `FKsbg59w8q63i0oo53rlgvlcnjq` FOREIGN KEY (`department_id`) REFERENCES `departments` (`id`);

--
-- Omezení pro tabulku `user_roles`
--
ALTER TABLE `user_roles`
  ADD CONSTRAINT `FKh8ciramu9cc9q3qcqiv4ue8a6` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`),
  ADD CONSTRAINT `FKhfh9dx7w3ubf1co1vdev94g3f` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
