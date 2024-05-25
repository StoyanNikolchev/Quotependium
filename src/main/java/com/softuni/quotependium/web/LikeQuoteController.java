package com.softuni.quotependium.web;

import com.softuni.quotependium.services.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/like-quote")
public class LikeQuoteController {

    private QuoteService quoteService;

    @Autowired
    public LikeQuoteController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @PostMapping("/{quoteId}")
    public ResponseEntity<?> likeQuote(@PathVariable Long quoteId, @AuthenticationPrincipal UserDetails userDetails) {
        boolean isLiked = quoteService.toggleLike(quoteId, userDetails.getUsername());
        return ResponseEntity.ok(isLiked);
    }
}
