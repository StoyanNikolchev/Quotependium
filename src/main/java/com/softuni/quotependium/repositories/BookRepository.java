package com.softuni.quotependium.repositories;

import com.softuni.quotependium.domain.entities.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {
    Optional<BookEntity> findBookEntityByIsbn(String isbn);
    Page<BookEntity> findAll(Pageable pageable);
    Optional<BookEntity> findBookEntityById(Long id);
    @Query("SELECT b FROM BookEntity b JOIN b.authors a WHERE a.id = :authorId")
    Page<BookEntity> findBooksByAuthorId(@Param("authorId") Long authorId, Pageable pageable);
}