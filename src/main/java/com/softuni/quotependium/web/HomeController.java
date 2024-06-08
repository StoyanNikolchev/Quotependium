package com.softuni.quotependium.web;

import com.softuni.quotependium.domain.views.QuoteView;
import com.softuni.quotependium.services.QuoteOfTheDayService;
import com.softuni.quotependium.services.UserService;
import com.softuni.quotependium.services.schedulers.QuoteOfTheDayScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private final QuoteOfTheDayService quoteOfTheDayService;
    private final UserService userService;

    @Autowired
    public HomeController(QuoteOfTheDayScheduler quoteOfTheDayScheduler, QuoteOfTheDayService quoteOfTheDayService, UserService userService) {
        this.quoteOfTheDayService = quoteOfTheDayService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String getHome(Model model) {
        QuoteView quoteOfTheDay = this.quoteOfTheDayService.getQuoteOfTheDayView();
        model.addAttribute("quoteOfTheDay", quoteOfTheDay);

        Long currentUserId = this.userService.getCurrentUserId();
        model.addAttribute("currentUserId", currentUserId);
        return "index";
    }
}
