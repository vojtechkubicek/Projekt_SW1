package cz.osu.swidemo.controllers;

import cz.osu.swidemo.entities.*;
import cz.osu.swidemo.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired private UserRepository userRepository;
    @Autowired private DepartmentRepository departmentRepository;
    @Autowired private BookRepository bookRepository;
    @Autowired private AuthorRepository authorRepository;

    @PostMapping("/generate")
    public ResponseEntity<?> generate() {
        // --- 1. HROMADNÉ GENEROVÁNÍ KNIH ---
        String[][] testBooks = {
                {"1984", "George Orwell", "1949"},
                {"Zaklínač: Poslední přání", "Andrzej Sapkowski", "1993"},
                {"Hra o trůny", "George R. R. Martin", "1996"},
                {"Velký Gatsby", "F. Scott Fitzgerald", "1925"},
                {"Hobit", "J.R.R. Tolkien", "1937"},
                {"Malý princ", "Antoine de Saint-Exupéry", "1943"},
                {"Farma zvířat", "George Orwell", "1945"}
        };

        for (String[] bookData : testBooks) {
            String title = bookData[0];
            String authorName = bookData[1];
            int year = Integer.parseInt(bookData[2]);

            // Najdeme nebo vytvoříme autora
            Author author = authorRepository.findByName(authorName);
            if (author == null) {
                author = new Author(authorName);
                author = authorRepository.save(author);
            }

            // Vložíme knihu, jen pokud v DB ještě není
            boolean exists = bookRepository.findAll().stream()
                    .anyMatch(b -> title.equals(b.getTitle()));

            if (!exists) {
                Book book = new Book(title, year);
                book.setAuthors(List.of(author));
                book.setAvailable(true); // Nastavíme jako volnou
                bookRepository.save(book);
            }
        }

        // --- 2. GENEROVÁNÍ ODDĚLENÍ ---
        Department dep = departmentRepository.findAll().stream()
                .filter(d -> "Vývojové oddělení".equals(d.getName()))
                .findFirst()
                .orElse(null);

        if (dep == null) {
            dep = new Department("Vývojové oddělení");
            departmentRepository.save(dep);
        }

        // --- 3. GENEROVÁNÍ TESTOVACÍHO UŽIVATELE ---
        // Vytvoříme roli (pokud ji tvoje entita Role nevyžaduje v DB jako unikátní, stačí takto)
        Role role = new Role("DEVELOPER");

        User user = new User();
        // Náhodný username, aby šlo klikat víckrát a nevznikla duplicita
        user.setUsername("dev-" + UUID.randomUUID().toString().substring(0,4));
        user.setFirstName("Petr");
        user.setLastName("Tester");
        user.setEmail("petr@osu.cz");
        user.setAge(25);
        user.setPassword("heslo123");
        user.setDepartment(dep);
        user.addRole(role);

        userRepository.save(user);

        return ResponseEntity.ok().body("Systém byl úspěšně naplněn hromadou testovacích dat!");
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}