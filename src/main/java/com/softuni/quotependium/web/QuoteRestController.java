package com.softuni.quotependium.web;

import com.softuni.quotependium.domain.views.QuoteView;
import com.softuni.quotependium.services.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuoteRestController {
    private final QuoteService quoteService;

    @Autowired
    public QuoteRestController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @GetMapping("/api/quotes/random")
    public QuoteView getRandomQuote() {
        return this.quoteService.getRandomQuoteView();
    }
}
