package cz.osu.swidemo.repositories;

import cz.osu.swidemo.entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    // Pomocná metoda – najde nám autora podle jména, abychom ho neměli v tabulce 10x
    Author findByName(String name);
}