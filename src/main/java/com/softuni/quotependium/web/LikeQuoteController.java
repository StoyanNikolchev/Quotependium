package com.softuni.quotependium.web;

import com.softuni.quotependium.services.QuoteOfTheDayService;
import com.softuni.quotependium.services.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/like-quote")
public class LikeQuoteController {

    private QuoteService quoteService;
    private QuoteOfTheDayService quoteOfTheDayService;

    @Autowired
    public LikeQuoteController(QuoteService quoteService, QuoteOfTheDayService quoteOfTheDayService) {
        this.quoteService = quoteService;
        this.quoteOfTheDayService = quoteOfTheDayService;
    }

    @PostMapping("/{quoteId}")
    public ResponseEntity<?> likeQuote(@PathVariable Long quoteId, @AuthenticationPrincipal UserDetails userDetails) {
        boolean isLiked = quoteService.toggleLike(quoteId, userDetails.getUsername());

        //Updates cached likes if the liked quote is the Quote of the Day
        if (quoteId.equals(this.quoteOfTheDayService.getQuoteOfTheDayView().getId())) {
            this.quoteOfTheDayService.updateQuoteOfTheDayLikes();
        }

        return ResponseEntity.ok(isLiked);
    }
}
