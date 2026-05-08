package cz.osu.swidemo.config;

import cz.osu.swidemo.entities.Role;
import cz.osu.swidemo.entities.User;
import cz.osu.swidemo.repositories.RoleRepository;
import cz.osu.swidemo.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        Role adminRole = createRoleIfNotFound("ROLE_ADMIN");
        Role readerRole = createRoleIfNotFound("ROLE_READER");

        if (userRepository.findByUsername("admin") == null) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@knihovna.cz");
            admin.addRole(adminRole);
            admin.addRole(readerRole); // Admin může všechno
            userRepository.save(admin);
            System.out.println("LOG: Účet ADMIN vytvořen.");
        }

        if (userRepository.findByUsername("ctenar") == null) {
            User reader = new User();
            reader.setUsername("ctenar");
            reader.setPassword(passwordEncoder.encode("ctenar123"));
            reader.setEmail("ctenar@knihovna.cz");
            reader.addRole(readerRole);
            userRepository.save(reader);
            System.out.println("LOG: Účet ČTENÁŘ vytvořen.");
        }
    }

    private Role createRoleIfNotFound(String name) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            roleRepository.save(role);
        }
        return role;
    }
}