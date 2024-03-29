package com.softuni.quotependium.domain.dtos;

import com.softuni.quotependium.domain.entities.AuthorEntity;
import com.softuni.quotependium.validations.yearIsPastOrPresent.PastOrPresentYear;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

public class BookDto {

    @NotBlank(message = "Please enter a book title")
    @Length(min = 3, message = "Title must be at least 3 characters long")
    private String title;

    @NotBlank(message = "The ISBN cannot be blank")
    private String isbn;

    @NotBlank(message = "Please enter at least one author")
    @Length(min = 6, message = "Author names cannot be less than 6 characters long")
    private String authorsString;

    @PastOrPresentYear
    @Positive(message = "Year must be a positive number")
    private Integer publicationYear;

    private Set<AuthorEntity> authors;

    public Set<AuthorEntity> getAuthors() {
        return authors;
    }

    public BookDto setAuthors(Set<AuthorEntity> authors) {
        this.authors = authors;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public BookDto setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getIsbn() {
        return isbn;
    }

    public BookDto setIsbn(String isbn) {
        this.isbn = isbn;
        return this;
    }

    public String getAuthorsString() {
        return authorsString;
    }

    public BookDto setAuthorsString(String authorsString) {
        this.authorsString = authorsString;
        return this;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public BookDto setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
        return this;
    }
}
