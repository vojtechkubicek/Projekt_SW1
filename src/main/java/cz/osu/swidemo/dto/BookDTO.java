package cz.osu.swidemo.dto;

public class BookDTO {
    private Long id;
    private String title;
    private String author;
    private int publishYear;
    private boolean available;

    public BookDTO() {}

    // Opravený konstruktor, který už správně přijímá a ukládá 'available'
    public BookDTO(Long id, String title, String author, int publishYear, boolean available) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publishYear = publishYear;
        this.available = available;
    }

    // --- Gettery a Settery ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public int getPublishYear() { return publishYear; }
    public void setPublishYear(int publishYear) { this.publishYear = publishYear; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
}