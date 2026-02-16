SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

CREATE TABLE authors (
    id INT AUTO_INCREMENT PRIMARY KEY,
    jmeno VARCHAR(100),
    prijmeni VARCHAR(100),
    narodnost VARCHAR(50)
);


CREATE TABLE books (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nazev VARCHAR(255) NOT NULL,
    isbn VARCHAR(20) UNIQUE,
    rok_vydani INT,
    autor_id INT,
    FOREIGN KEY (autor_id) REFERENCES authors(id)
);


CREATE TABLE members (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cele_jmeno VARCHAR(200) NOT NULL,
    email VARCHAR(150) UNIQUE,
    datum_registrace DATE
);


CREATE TABLE loans (
    id INT AUTO_INCREMENT PRIMARY KEY,
    kniha_id INT NOT NULL,
    ctenar_id INT NOT NULL,
    datum_vypujceni DATE,
    datum_vraceni DATE,
    FOREIGN KEY (kniha_id) REFERENCES books(id),
    FOREIGN KEY (ctenar_id) REFERENCES members(id)
);
