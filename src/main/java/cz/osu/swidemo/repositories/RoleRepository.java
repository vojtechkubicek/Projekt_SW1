package cz.osu.swidemo.repositories;

import cz.osu.swidemo.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name); // <-- Tuto řádku přidat
}