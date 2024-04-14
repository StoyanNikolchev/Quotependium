package com.softuni.quotependium.services.schedulers;

import com.softuni.quotependium.services.QuoteOfTheDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class QuoteOfTheDayScheduler {
    private final QuoteOfTheDayService quoteOfTheDayService;

    @Autowired
    public QuoteOfTheDayScheduler(QuoteOfTheDayService quoteOfTheDayService) {
        this.quoteOfTheDayService = quoteOfTheDayService;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void updateQuoteOfTheDay() {
        this.quoteOfTheDayService.updateQuoteOfTheDay();
    }
}
