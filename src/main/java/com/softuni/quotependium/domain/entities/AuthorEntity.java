package com.softuni.quotependium.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "authors")
public class AuthorEntity extends BaseEntity {

    @Column(nullable = false, unique = true)
    @Size(min = 6, max = 50)
    private String fullName;

    public String getFullName() {
        return fullName;
    }

    public AuthorEntity setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    @Override
    public AuthorEntity setId(Long id) {
        super.setId(id);
        return this;
    }
}
