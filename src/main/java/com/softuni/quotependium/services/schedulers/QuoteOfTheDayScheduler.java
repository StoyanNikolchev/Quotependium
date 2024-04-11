package com.softuni.quotependium.services.schedulers;

import com.softuni.quotependium.domain.views.QuoteView;
import com.softuni.quotependium.services.QuoteService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class QuoteOfTheDayScheduler {

    private QuoteView quoteOfTheDay;
    private final QuoteService quoteService;

    @Autowired
    public QuoteOfTheDayScheduler(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @PostConstruct
    public void initializeQuoteOfTheDay() {
        this.updateQuoteOfTheDay();
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void updateQuoteOfTheDay() {
        this.quoteOfTheDay = this.quoteService.getRandomQuoteView();
    }

    public QuoteView getQuoteOfTheDay() {
        return this.quoteOfTheDay;
    }
}
