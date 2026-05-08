package cz.osu.swidemo.controllers;

import cz.osu.swidemo.entities.Role;
import cz.osu.swidemo.entities.User;
import cz.osu.swidemo.repositories.RoleRepository;
import cz.osu.swidemo.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public String registerUser(@RequestBody User newUser) {
        // 1. Kontrola, zda uživatel již neexistuje
        if (userRepository.findByUsername(newUser.getUsername()) != null) {
            return "Uživatelské jméno je již obsazené!";
        }

        // 2. Zašifrování hesla
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        // 3. Přidělení základní role Čtenáře
        Role readerRole = roleRepository.findByName("ROLE_READER");
        if (readerRole != null) {
            newUser.addRole(readerRole);
        }

        // 4. Uložení
        userRepository.save(newUser);
        return "Registrace proběhla úspěšně! Nyní se můžete přihlásit.";
    }
}