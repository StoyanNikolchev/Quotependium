package com.softuni.quotependium.web;

import com.softuni.quotependium.domain.views.QuoteView;
import com.softuni.quotependium.services.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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

    @GetMapping("/api/quotes/{id}")
    public ResponseEntity<QuoteView> getQuote(@PathVariable Long id) {
        QuoteView quoteViewById = this.quoteService.findQuoteViewById(id);

        if (quoteViewById == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Quote not found");
        }

        return ResponseEntity.ok(quoteViewById);
    }
}
