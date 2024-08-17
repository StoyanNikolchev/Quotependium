package com.softuni.quotependium.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "quotes")
public class QuoteEntity extends BaseEntity {

    @ManyToOne
    @NotNull
    private UserEntity user;

    @ManyToOne
    @NotNull
    private BookEntity book;

    @Column(columnDefinition = "TEXT", nullable = false)
    @Size(min = 10, max = 500)
    private String text;

    @Column(name = "page_number", nullable = false)
    private Integer pageNumber;

    @Column(nullable = false)
    private Integer likes;

    @ManyToMany(mappedBy = "likedQuotes")
    private Set<UserEntity> likedByUsers = new HashSet<>();

    public Set<UserEntity> getLikedByUsers() {
        return likedByUsers;
    }

    public QuoteEntity setLikedByUsers(Set<UserEntity> likedByUsers) {
        this.likedByUsers = likedByUsers;
        return this;
    }

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

    @Override
    public QuoteEntity setId(Long id) {
        super.setId(id);
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
