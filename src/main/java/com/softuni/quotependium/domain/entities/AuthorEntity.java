package com.softuni.quotependium.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "authors")
public class AuthorEntity extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String fullName;

    public String getFullName() {
        return fullName;
    }

    public AuthorEntity setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }
}
