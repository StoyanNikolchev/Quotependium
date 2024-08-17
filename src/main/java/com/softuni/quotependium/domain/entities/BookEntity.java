package com.softuni.quotependium.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

@Entity
@Table(name = "books")
public class BookEntity extends BaseEntity {

    @Column(nullable = false)
    @Size(min = 3, max = 50)
    private String title;

    @Column(nullable = false, unique = true)
    @Size(min = 13, max = 13)
    private String isbn;

    @ManyToMany(fetch= FetchType.EAGER)
    @JoinTable(
            name = "books_authors",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private Set<AuthorEntity> authors;

    @Column(name = "publication_year")
    @NotNull
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

    @Override
    public BookEntity setId(Long id) {
        super.setId(id);
        return this;
    }
}
