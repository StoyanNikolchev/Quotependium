package com.softuni.quotependium.web;

import com.softuni.quotependium.domain.dtos.QuoteImportDto;
import com.softuni.quotependium.services.QuoteService;
import com.softuni.quotependium.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class QuoteController {
    private final QuoteService quoteService;
    private final UserService userService;

    @Autowired
    public QuoteController(QuoteService quoteService, UserService userService) {
        this.quoteService = quoteService;
        this.userService = userService;
    }

    @GetMapping("/quotes/add")
    public String getAddQuote(@ModelAttribute("quoteImportDto") QuoteImportDto quoteImportDto,
                              @RequestParam("bookId") Long bookId) {
        return "add-quote";
    }

    @PostMapping("/quotes/add")
    public String postQuote(@Valid QuoteImportDto quoteImportDto,
                            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "add-quote";
        }

        Long currentUserId = this.userService.getCurrentUserId();
        Long bookId = quoteImportDto.getBookId();
        this.quoteService.addQuote(quoteImportDto, currentUserId);
        return "redirect:/";
    }
}
