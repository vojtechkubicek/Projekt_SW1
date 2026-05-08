package cz.osu.swidemo.repositories;

import cz.osu.swidemo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> { // Změna na String
    User findByUsername(String username);
}