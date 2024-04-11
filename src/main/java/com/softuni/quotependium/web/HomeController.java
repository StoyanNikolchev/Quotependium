package com.softuni.quotependium.web;

import com.softuni.quotependium.domain.views.QuoteView;
import com.softuni.quotependium.services.schedulers.QuoteOfTheDayScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private final QuoteOfTheDayScheduler quoteOfTheDayScheduler;

    @Autowired
    public HomeController(QuoteOfTheDayScheduler quoteOfTheDayScheduler) {
        this.quoteOfTheDayScheduler = quoteOfTheDayScheduler;
    }

    @GetMapping("/")
    public String getHome(Model model) {
        QuoteView quoteOfTheDay = this.quoteOfTheDayScheduler.getQuoteOfTheDay();
        model.addAttribute("quoteOfTheDay", quoteOfTheDay);
        return "index";
    }
}
