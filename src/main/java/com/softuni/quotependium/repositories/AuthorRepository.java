package com.softuni.quotependium.repositories;

import com.softuni.quotependium.domain.entities.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<AuthorEntity, Long> {
    Optional<AuthorEntity> findAuthorEntityByFullName(String name);
}

