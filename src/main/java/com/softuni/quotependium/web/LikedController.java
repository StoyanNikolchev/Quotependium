package com.softuni.quotependium.web;

import com.softuni.quotependium.domain.views.QuoteView;
import com.softuni.quotependium.services.QuoteService;
import com.softuni.quotependium.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/liked")
public class LikedController {
    private QuoteService quoteService;
    private UserService userService;

    @Autowired
    public LikedController(QuoteService quoteService, UserService userService) {
        this.quoteService = quoteService;
        this.userService = userService;
    }

    @GetMapping
    public String getFavorites(Model model,
                               @PageableDefault(
                                       sort = "id",
                                       direction = Sort.Direction.ASC,
                                       size = 3,
                                       page = 0)
                               Pageable pageable) {
        Page<QuoteView> quotesPage = this.quoteService.getCurrentUserFavoriteQuotes(pageable);
        model.addAttribute("quotes", quotesPage);

        Long currentUserId = this.userService.getCurrentUserId();
        model.addAttribute("currentUserId", currentUserId);
        return "liked";
    }
}