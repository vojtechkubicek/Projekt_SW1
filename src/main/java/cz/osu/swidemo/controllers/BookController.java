package cz.osu.swidemo.controllers;

import cz.osu.swidemo.dto.BookDTO;
import cz.osu.swidemo.entities.Author;
import cz.osu.swidemo.entities.Book;
import cz.osu.swidemo.repositories.AuthorRepository;
import cz.osu.swidemo.repositories.BookRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/books")
@CrossOrigin
public class BookController {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookController(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @GetMapping
    public List<BookDTO> getAllBooks() {
        List<BookDTO> result = new ArrayList<>();
        for (Book book : bookRepository.findAll()) {
            String authorName = "";
            if (book.getAuthors() != null && !book.getAuthors().isEmpty()) {
                authorName = book.getAuthors().get(0).getName();
            }
            // Předáváme i informaci o dostupnosti
            result.add(new BookDTO(book.getId(), book.getTitle(), authorName, book.getPublishYear(), book.isAvailable()));
        }
        return result;
    }

    @PostMapping
    public BookDTO addBook(@RequestBody BookDTO newBookDTO) {
        Author author = authorRepository.findByName(newBookDTO.getAuthor());
        if (author == null) {
            author = new Author(newBookDTO.getAuthor());
            author = authorRepository.save(author);
        }

        Book book = new Book(newBookDTO.getTitle(), newBookDTO.getPublishYear());
        book.setAuthors(List.of(author));
        book.setAvailable(true); // Nová kniha je vždy volná
        book = bookRepository.save(book);

        return new BookDTO(book.getId(), book.getTitle(), author.getName(), book.getPublishYear(), book.isAvailable());
    }

    // --- NOVÁ METODA PRO PŮJČENÍ ---
    @PostMapping("/{id}/borrow")
    public ResponseEntity<?> borrowBook(@PathVariable Long id) {
        Book book = bookRepository.findById(id).orElse(null);
        if (book == null) return ResponseEntity.notFound().build();

        if (!book.isAvailable()) {
            return ResponseEntity.badRequest().body("Kniha je již půjčená.");
        }

        book.setAvailable(false); // Změníme stav na půjčeno
        bookRepository.save(book);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookRepository.deleteById(id);
    }
}