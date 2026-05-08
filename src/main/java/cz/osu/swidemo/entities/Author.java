package cz.osu.swidemo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "authors")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // Předpokládám, že autor má v databázi jméno

    // Propojení zpět na knihy
    @ManyToMany(mappedBy = "authors")
    @JsonIgnore // Zabrání tomu, aby se Kniha a Autor donekonečna vyvolávali
    private List<Book> books;

    public Author() {}

    public Author(String name) {
        this.name = name;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<Book> getBooks() { return books; }
    public void setBooks(List<Book> books) { this.books = books; }
}