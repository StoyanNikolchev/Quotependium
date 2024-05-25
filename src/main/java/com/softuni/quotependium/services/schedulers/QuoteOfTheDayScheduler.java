package com.softuni.quotependium.services.schedulers;

import com.softuni.quotependium.services.QuoteOfTheDayService;
import com.softuni.quotependium.web.QuoteController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class QuoteOfTheDayScheduler {
    private final QuoteOfTheDayService quoteOfTheDayService;
    private final Logger logger = LoggerFactory.getLogger(QuoteOfTheDayScheduler.class);

    @Autowired
    public QuoteOfTheDayScheduler(QuoteOfTheDayService quoteOfTheDayService) {
        this.quoteOfTheDayService = quoteOfTheDayService;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void updateQuoteOfTheDay() {
        this.quoteOfTheDayService.updateQuoteOfTheDay();
        logger.info("Updated quote of the day!");
    }
}
