package com.softuni.quotependium.domain.views;

import java.util.List;

public class QuoteView {
    private String text;
    private List<String> authors;
    private String bookTitle;

    public String getText() {
        return text;
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
        return bookTitle;
    }

    public QuoteView setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
        return this;
    }
}
