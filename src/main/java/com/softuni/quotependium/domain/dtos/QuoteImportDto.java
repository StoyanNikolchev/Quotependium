package com.softuni.quotependium.domain.dtos;

import com.softuni.quotependium.domain.entities.BookEntity;
import com.softuni.quotependium.domain.entities.UserEntity;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import static com.softuni.quotependium.domain.enums.Messages.*;

public class QuoteImportDto {

    @Length(min = 10, max = 500, message = INVALID_QUOTE_TEXT_LENGTH)
    private String text;

    @Range(min = 1, message = INVALID_PAGE_NUMBER)
    @NotNull(message = INVALID_PAGE_NUMBER)
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
