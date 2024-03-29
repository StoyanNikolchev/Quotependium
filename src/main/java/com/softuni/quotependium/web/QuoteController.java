package com.softuni.quotependium.web;

import com.softuni.quotependium.domain.dtos.QuoteDto;
import com.softuni.quotependium.services.QuoteService;
import com.softuni.quotependium.services.UserService;
import jakarta.servlet.http.HttpSession;
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
    public String getAddQuote(@ModelAttribute("quoteDto") QuoteDto quoteDto,
                              @RequestParam("bookId") Long bookId) {
        return "add-quote";
    }

    @PostMapping("/quotes/add")
    public String postQuote(@Valid QuoteDto quoteDto,
                            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "add-quote";
        }

        Long currentUserId = this.userService.getCurrentUserId();
        Long bookId = quoteDto.getBookId();
        this.quoteService.addQuote(quoteDto, currentUserId);
        return "redirect:/";
    }
}
