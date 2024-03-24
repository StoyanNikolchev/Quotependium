package com.softuni.quotependium.domain.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "quotes")
public class QuoteEntity extends BaseEntity {

    @ManyToOne
    private UserEntity user;

    @ManyToOne
    private BookEntity book;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String text;

    @Column(name = "page_number", nullable = false)
    private Integer pageNumber;

    @Column(nullable = false)
    private Integer likes;

    public QuoteEntity setUser(UserEntity user) {
        this.user = user;
        return this;
    }

    public QuoteEntity setBook(BookEntity book) {
        this.book = book;
        return this;
    }

    public QuoteEntity setText(String text) {
        this.text = text;
        return this;
    }

    public QuoteEntity setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
        return this;
    }

    public QuoteEntity setLikes(Integer likes) {
        this.likes = likes;
        return this;
    }

    public UserEntity getUser() {
        return user;
    }

    public BookEntity getBook() {
        return book;
    }

    public String getText() {
        return text;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public Integer getLikes() {
        return likes;
    }
}
