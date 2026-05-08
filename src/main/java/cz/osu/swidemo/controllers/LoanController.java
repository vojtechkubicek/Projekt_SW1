package cz.osu.swidemo.controllers;

import cz.osu.swidemo.entities.Book;
import cz.osu.swidemo.entities.Loan;
import cz.osu.swidemo.entities.User;
import cz.osu.swidemo.repositories.BookRepository;
import cz.osu.swidemo.repositories.LoanRepository;
import cz.osu.swidemo.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/loans")
@CrossOrigin(origins = "http://localhost:3000")
public class LoanController {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public LoanController(LoanRepository loanRepository, BookRepository bookRepository, UserRepository userRepository) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/{bookId}")
    public ResponseEntity<?> loanBook(@PathVariable Long bookId, Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        Book book = bookRepository.findById(bookId).orElse(null);

        if (book == null) return ResponseEntity.notFound().build();

        // 1. KONTROLA: Je kniha volná?
        if (!book.isAvailable()) {
            return ResponseEntity.badRequest().body("Kniha je již půjčená.");
        }

        // 2. PROVEDENÍ VÝPŮJČKY
        Loan loan = new Loan(user, book, LocalDate.now());

        // 3. ZMĚNA STAVU KNIHY: Nastavíme, že už není dostupná
        book.setAvailable(false);
        bookRepository.save(book); // Uložíme změnu v tabulce 'book'

        loanRepository.save(loan); // Uložíme záznam v tabulce 'loan'

        return ResponseEntity.ok().body("Kniha '" + book.getTitle() + "' byla úspěšně vypůjčena.");
    }

    @GetMapping("/my-loans")
    public List<Loan> getMyLoans(Principal principal) {
        return loanRepository.findByUserUsername(principal.getName());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> returnBook(@PathVariable Long id, Principal principal) {
        return loanRepository.findById(id).map(loan -> {
            // Kontrola oprávnění
            if (loan.getUser().getUsername().equals(principal.getName()) || principal.getName().equals("admin")) {

                // 4. VRÁCENÍ KNIHY DO OBĚHU: Najdeme knihu z výpůjčky a nastavíme ji jako volnou
                Book book = loan.getBook();
                if (book != null) {
                    book.setAvailable(true);
                    bookRepository.save(book); // Uložíme změnu v tabulce 'book'
                }

                loanRepository.delete(loan); // Smažeme záznam o výpůjčce
                return ResponseEntity.ok().body("Kniha byla úspěšně vrácena.");
            } else {
                return ResponseEntity.status(403).body("Nemáte oprávnění vrátit cizí výpůjčku.");
            }
        }).orElse(ResponseEntity.notFound().build());
    }
}