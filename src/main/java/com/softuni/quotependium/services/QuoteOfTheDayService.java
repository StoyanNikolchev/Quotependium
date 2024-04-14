package com.softuni.quotependium.services;

import com.softuni.quotependium.domain.entities.QuoteEntity;
import com.softuni.quotependium.domain.entities.QuoteOfTheDayEntity;
import com.softuni.quotependium.domain.views.QuoteView;
import com.softuni.quotependium.repositories.QuoteOfTheDayRepository;
import com.softuni.quotependium.repositories.QuoteRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

import static com.softuni.quotependium.utils.QuoteUtils.getNullPlaceholderQuoteView;
import static com.softuni.quotependium.utils.QuoteUtils.mapQuoteEntityToView;

@Service
public class QuoteOfTheDayService {

    private final QuoteRepository quoteRepository;
    private final QuoteOfTheDayRepository quoteOfTheDayRepository;

    @Autowired
    public QuoteOfTheDayService(QuoteRepository quoteRepository, QuoteOfTheDayRepository quoteOfTheDayRepository) {
        this.quoteRepository = quoteRepository;
        this.quoteOfTheDayRepository = quoteOfTheDayRepository;
    }

    @PostConstruct
    private void initializeQuoteOfTheDayOnStartup() {
        if (this.quoteOfTheDayRepository.findQuoteOfTheDayEntityByDate(LocalDate.now()).isEmpty()) {
            updateQuoteOfTheDay();
        }
    }

    public QuoteView getQuoteOfTheDayView() {

        if (!dbIsInit()) {
            return getNullPlaceholderQuoteView();
        }

        Optional<QuoteOfTheDayEntity> quoteOfTheDayOptional = this.quoteOfTheDayRepository.findQuoteOfTheDayEntityByDate(LocalDate.now());

        if (quoteOfTheDayOptional.isEmpty()) {
            return getNullPlaceholderQuoteView();
        }

        Long quoteId = quoteOfTheDayOptional.get().getQuoteId();
        QuoteEntity quoteEntity = this.quoteRepository.findById(quoteId).get();
        return mapQuoteEntityToView(quoteEntity);
    }

    public void updateQuoteOfTheDay() {
        if (!dbIsInit()) {
            return;
        }

        QuoteEntity randomQuote = this.quoteRepository.findRandomQuote();
        Long quoteId = randomQuote.getId();

        QuoteOfTheDayEntity quoteOfTheDay = new QuoteOfTheDayEntity()
                .setQuoteId(quoteId)
                .setDate(LocalDate.now());

        this.quoteOfTheDayRepository.saveAndFlush(quoteOfTheDay);
    }

    private boolean dbIsInit() {
        return this.quoteRepository.count() > 0;
    }
}