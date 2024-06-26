package com.softuni.quotependium.domain.dtos;

import com.softuni.quotependium.domain.entities.BookEntity;
import com.softuni.quotependium.domain.entities.UserEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

public class QuoteImportDto {

    @NotBlank
    @Length(min = 10, message = "Quote must be at least 10 characters long")
    private String text;

    @Range(min = 1, message = "Please enter a valid page number")
    @NotNull(message = "Please enter a page number")
    private Integer pageNumber;
    private UserEntity user;
    private BookEntity book;
    private Integer likes;
    private Long bookId;

    public Long getBookId() {
        return bookId;
    }

    public QuoteImportDto setBookId(Long bookId) {
        this.bookId = bookId;
        return this;
    }

    public Integer getLikes() {
        return likes;
    }

    public QuoteImportDto setLikes(Integer likes) {
        this.likes = likes;
        return this;
    }

    public String getText() {
        return text;
    }

    public QuoteImportDto setText(String text) {
        this.text = text;
        return this;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public QuoteImportDto setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
        return this;
    }

    public UserEntity getUser() {
        return user;
    }

    public QuoteImportDto setUser(UserEntity user) {
        this.user = user;
        return this;
    }

    public BookEntity getBook() {
        return book;
    }

    public QuoteImportDto setBook(BookEntity book) {
        this.book = book;
        return this;
    }
}
