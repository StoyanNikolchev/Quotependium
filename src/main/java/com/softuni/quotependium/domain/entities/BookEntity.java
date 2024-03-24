package com.softuni.quotependium.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import java.util.Set;

@Entity
@Table(name = "books")
public class BookEntity extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, unique = true)
    private String isbn;

    @ManyToMany
    private Set<AuthorEntity> authors;

    @Column(name = "publication_year")
    private Integer publicationYear;

    public String getTitle() {
        return title;
    }

    public BookEntity setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getIsbn() {
        return isbn;
    }

    public BookEntity setIsbn(String isbn) {
        this.isbn = isbn;
        return this;
    }

    public Set<AuthorEntity> getAuthors() {
        return authors;
    }

    public BookEntity setAuthors(Set<AuthorEntity> authors) {
        this.authors = authors;
        return this;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public BookEntity setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
        return this;
    }
}
