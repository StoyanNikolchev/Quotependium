package com.softuni.quotependium.services;

import com.softuni.quotependium.domain.entities.AuthorEntity;
import com.softuni.quotependium.domain.views.AuthorView;
import com.softuni.quotependium.repositories.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Page<AuthorView> getAllAuthors(Pageable pageable) {
        return this.authorRepository.findAll(pageable)
                .map(authorEntity -> new AuthorView()
                        .setFullName(authorEntity.getFullName())
                        .setId(authorEntity.getId()));
    }

    public AuthorView findAuthorById(Long id) {
        Optional<AuthorEntity> authorEntityOptional = this.authorRepository.findAuthorEntityById(id);

        return authorEntityOptional.map(authorEntity -> new AuthorView()
                .setId(authorEntity.getId())
                .setFullName(authorEntity.getFullName())).orElse(null);
    }
}