package com.softuni.quotependium.repositories;

import com.softuni.quotependium.domain.entities.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {
    Optional<BookEntity> findBookEntityByIsbn(String isbn);
}
