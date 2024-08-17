package com.softuni.quotependium.domain.dtos;

import com.softuni.quotependium.domain.entities.AuthorEntity;
import com.softuni.quotependium.validations.yearIsPastOrPresent.PastOrPresentYear;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

import static com.softuni.quotependium.domain.enums.Messages.*;

public class BookDto {

    @NotBlank(message = ENTER_BOOK_TITLE)
    @Length(min = 3, max = 50, message = INVALID_TITLE_LENGTH)
    private String title;

    @NotBlank(message = BLANK_ISBN)
    private String isbn;

    @NotBlank(message = BLANK_AUTHORS)
    @Length(min = 6, max = 50, message = INVALID_AUTHORS_LENGTH)
    private String authorsString;

    @PastOrPresentYear
    @Positive(message = NEGATIVE_YEAR)
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
