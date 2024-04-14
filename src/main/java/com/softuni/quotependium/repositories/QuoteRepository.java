package com.softuni.quotependium.repositories;

import com.softuni.quotependium.domain.entities.QuoteEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuoteRepository extends JpaRepository<QuoteEntity, Long> {
    Optional<QuoteEntity> findTopByOrderByIdDesc();
    Page<QuoteEntity> findAll(Pageable pageable);
    Page<QuoteEntity> findAllByBookId(Long bookId, Pageable pageable);
    @Query(value = """
            SELECT q.*
            FROM quotes AS q
            JOIN (SELECT CEIL(RAND() * (SELECT MAX(id) FROM quotes)) AS id) AS q2
            WHERE q.id >= q2.id
            ORDER BY q.id ASC
            LIMIT 1;""", nativeQuery = true)
    QuoteEntity findRandomQuote();
}
