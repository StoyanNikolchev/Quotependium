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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
                              @RequestParam("bookId") Long bookId,
                              HttpSession session) {
        session.setAttribute("bookId", bookId);
        return "add-quote";
    }

    @PostMapping("/quotes/add")
    public String postQuote(@Valid QuoteDto quoteDto,
                            HttpSession session,
                            BindingResult bindingResult) {

        Long bookId = (Long) session.getAttribute("bookId");
        session.removeAttribute("bookId");

        if (bindingResult.hasErrors()) {
            return "add-quote";
        }

        Long currentUserId = this.userService.getCurrentUserId();
        this.quoteService.addQuote(quoteDto, bookId, currentUserId);
        return "redirect:/";
    }
}
