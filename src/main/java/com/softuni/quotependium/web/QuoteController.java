package com.softuni.quotependium.web;

import com.softuni.quotependium.domain.dtos.QuoteImportDto;
import com.softuni.quotependium.services.QuoteService;
import com.softuni.quotependium.services.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class QuoteController {
    private final Logger logger = LoggerFactory.getLogger(QuoteController.class);
    private final QuoteService quoteService;
    private final UserService userService;

    @Autowired
    public QuoteController(QuoteService quoteService, UserService userService) {
        this.quoteService = quoteService;
        this.userService = userService;
    }

    @GetMapping("/quotes/add")
    public String getAddQuote(@ModelAttribute("quoteImportDto") QuoteImportDto quoteImportDto,
                              @RequestParam("bookId") Long bookId,
                              Model model) {
        model.addAttribute("bookId", bookId);
        return "add-quote";
    }

    @PostMapping("/quotes/add")
    public String postQuote(@Valid @ModelAttribute("quoteImportDto") QuoteImportDto quoteImportDto,
                            BindingResult bindingResult,
                            @RequestParam("bookId") Long bookId,
                            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("bookId", bookId);
            return "add-quote";
        }

        Long currentUserId = this.userService.getCurrentUserId();
        this.quoteService.addQuote(quoteImportDto, currentUserId);
        this.logger.info("Added new quote from book with title '{}'", quoteImportDto.getBook().getTitle());
        return "redirect:/";
    }
}