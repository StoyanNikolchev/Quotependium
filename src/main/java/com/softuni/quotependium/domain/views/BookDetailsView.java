package com.softuni.quotependium.domain.views;

import java.util.List;

public class BookDetailsView {
    private Long id;
    private String title;
    private String isbn;
    private List<String> authors;
    private Integer publicationYear;

    public String getTitle() {
        return title;
    }

    public BookDetailsView setTitle(String title) {
        this.title = title;
        return this;
    }

    public Long getId() {
        return id;
    }

    public BookDetailsView setId(Long id) {
        this.id = id;
        return this;
    }

    public String getIsbn() {
        return isbn;
    }

    public BookDetailsView setIsbn(String isbn) {
        this.isbn = isbn;
        return this;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public BookDetailsView setAuthors(List<String> authors) {
        this.authors = authors;
        return this;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public BookDetailsView setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
        return this;
    }

    public String getAuthorsFormatted() {
        return String.join(", ", this.authors);
    }
}
