package com.softuni.quotependium.domain.views;

public class BookTitleView {

    private Long id;
    private String title;

    public Long getId() {
        return id;
    }

    public BookTitleView setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public BookTitleView setTitle(String title) {
        this.title = title;
        return this;
    }
}
