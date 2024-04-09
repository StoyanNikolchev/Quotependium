package com.softuni.quotependium.domain.views;

public class AuthorView {

    private String fullName;

    private Long id;
    public String getFullName() {
        return fullName;
    }

    public AuthorView setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public Long getId() {
        return id;
    }

    public AuthorView setId(Long id) {
        this.id = id;
        return this;
    }
}
