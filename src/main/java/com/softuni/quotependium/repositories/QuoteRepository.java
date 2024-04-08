package com.softuni.quotependium.repositories;

import com.softuni.quotependium.domain.entities.QuoteEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuoteRepository extends JpaRepository<QuoteEntity, Long> {
    Optional<QuoteEntity> findTopByOrderByIdDesc();
    Page<QuoteEntity> findAll(Pageable pageable);
    Page<QuoteEntity> findAllByBookId(Long bookId, Pageable pageable);
}
