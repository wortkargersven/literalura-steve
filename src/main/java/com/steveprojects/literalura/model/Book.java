package com.steveprojects.literalura.model;

import com.steveprojects.literalura.records.AuthorData;
import com.steveprojects.literalura.records.BookData;
import jakarta.persistence.*;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "books")

public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBook;

    private String title;

    @ManyToOne
    @JoinColumn(name = "id_author")
    private Author author;

    @Enumerated(EnumType.STRING)
    private Languages languages;

    public Long getIdBook() {
        return idBook;
    }

    public void setIdBook(Long idBook) {
        this.idBook = idBook;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Languages getLanguages() {
        return languages;
    }

    public void setLanguages(Languages languages) {
        this.languages = languages;
    }

    public Integer getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Integer downloadCount) {
        this.downloadCount = downloadCount;
    }

    private Integer downloadCount;

    public Book(BookData bookData, Author author) {
        this.title = bookData.title();



        List<AuthorData> authorsData = bookData.authors().stream()
                .limit(1).collect(Collectors.toList());
        if(!authorsData.isEmpty()) {
            AuthorData authorData = authorsData.get(0);
            this.author = new Author(authorData);
        }
        
        if (bookData.languages() != null && !bookData.languages().isEmpty()) {
            this.languages = Languages.fromGutendex(bookData.languages().get(0));
        }
        this.downloadCount = bookData.downloadCount();
    }
    

    @Override
    public String toString() {
        return """
            --------------------------
            Title: %s
            Author: %s
            Language: %s
            Times downloaded: %d
            --------------------------
            """.formatted(
                title,
                author != null ? author.getName() : "Unknown",
                languages == null ? "Unknown" : languages.getEnglish(),
                downloadCount != null ? downloadCount : 0
        );
    }
    
    
}
