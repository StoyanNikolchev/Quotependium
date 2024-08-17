package com.softuni.quotependium.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
@Table(name = "quote_of_the_day")
public class QuoteOfTheDayEntity extends BaseEntity {

    @Column(name = "date", unique = true, nullable = false)
    private LocalDate date;

    @Column(name = "quote_id")
    @NotNull
    private Long quoteId;

    public LocalDate getDate() {
        return date;
    }

    public QuoteOfTheDayEntity setDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public Long getQuoteId() {
        return quoteId;
    }

    public QuoteOfTheDayEntity setQuoteId(Long quoteId) {
        this.quoteId = quoteId;
        return this;
    }

    @Override
    public QuoteOfTheDayEntity setId(Long id) {
        super.setId(id);
        return this;
    }
}
