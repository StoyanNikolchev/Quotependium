package com.softuni.quotependium.domain.views;

import java.util.List;
import java.util.Set;

public class QuoteView {
    private String text;
    private Long id;
    private List<String> authors;
    private String bookTitle;
    private Set<Long> likedByUserIds;
    private Integer likes;

    public Integer getLikes() {
        return likes;
    }

    public QuoteView setLikes(Integer likes) {
        this.likes = likes;
        return this;
    }

    public Set<Long> getLikedByUserIds() {
        return likedByUserIds;
    }

    public QuoteView setLikedByUserIds(Set<Long> likedByUserIds) {
        this.likedByUserIds = likedByUserIds;
        return this;
    }

    public String getText() {
        return text;
    }

    public Long getId() {
        return id;
    }

    public QuoteView setId(Long id) {
        this.id = id;
        return this;
    }

    public QuoteView setText(String text) {
        this.text = text;
        return this;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public QuoteView setAuthors(List<String> authors) {
        this.authors = authors;
        return this;
    }

    public String getBookTitle() {
        return "Book: " + bookTitle;
    }

    public QuoteView setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
        return this;
    }

    public String getAuthorsFormatted() {
        return "Authors: " + String.join(", ", this.authors);
    }
    public String getContentWithQuotationMarks() {
        return "\"" + this.text + "\"";
    }
}
