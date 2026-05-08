package cz.osu.swidemo.entities;

import jakarta.persistence.*;
import java.util.List;
import jakarta.persistence.Column;

@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(name = "publish_year")
    private int publishYear;

    @Column(nullable = false)
    private boolean available = true; // Výchozí stav: kniha je volná

    @ManyToMany
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<Author> authors;

    public Book() {}

    public Book(String title, int publishYear) {
        this.title = title;
        this.publishYear = publishYear;
        this.available = true;
    }

    // --- Gettery a Settery ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public int getPublishYear() { return publishYear; }
    public void setPublishYear(int publishYear) { this.publishYear = publishYear; }
    public List<Author> getAuthors() { return authors; }
    public void setAuthors(List<Author> authors) { this.authors = authors; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
}