package cz.osu.swidemo.config;

import cz.osu.swidemo.entities.User;
import cz.osu.swidemo.repositories.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("DEBUG: Pokus o přihlášení uživatele: " + username); // Toto uvidíš v IntelliJ

        cz.osu.swidemo.entities.User user = userRepository.findByUsername(username);

        if (user == null) {
            System.out.println("DEBUG: Uživatel " + username + " v databázi NENÍ!");
            throw new UsernameNotFoundException("Uživatel nenalezen");
        }

        System.out.println("DEBUG: Uživatel nalezen, role: " + user.getRoles().size());

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getRoles().stream()
                        .map(role -> new org.springframework.security.core.authority.SimpleGrantedAuthority(role.getName()))
                        .collect(java.util.stream.Collectors.toList()))
                .build();
    }
}