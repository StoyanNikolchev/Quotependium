package com.softuni.quotependium.repositories;

import com.softuni.quotependium.domain.entities.QuoteOfTheDayEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface QuoteOfTheDayRepository extends JpaRepository<QuoteOfTheDayEntity, Long> {
    Optional<QuoteOfTheDayEntity> findQuoteOfTheDayEntityByDate(LocalDate date);
}
