package com.softuni.quotependium.web;

import com.softuni.quotependium.domain.dtos.BookDto;
import com.softuni.quotependium.services.BookService;
import com.softuni.quotependium.services.QuoteService;
import com.softuni.quotependium.utils.IsbnUtils;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.softuni.quotependium.domain.enums.Messages.ENTER_VALID_ISBN;
import static com.softuni.quotependium.utils.IsbnUtils.formatIsbn;

@Controller
public class BookController {
    private final Logger logger = LoggerFactory.getLogger(BookController.class);
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService, QuoteService quoteService) {
        this.bookService = bookService;
    }

    @GetMapping("/books/check")
    public String getIsbnCheck(Model model) {
        model.addAttribute("bookDto", new BookDto());
        return "check-book";
    }

    @PostMapping("/books/check")
    public String postIsbnCheck(@ModelAttribute("bookDto") BookDto bookDto,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {

        String formattedIsbn = formatIsbn(bookDto.getIsbn());
        boolean isbnIsValid = IsbnUtils.checkIsbnValidity(formattedIsbn);

        if (!isbnIsValid) {
            bindingResult.addError(new FieldError("bookDto", "isbn", ENTER_VALID_ISBN));
        }

        if (bindingResult.hasErrors()) {
            return "check-book";
        }

        if (!this.bookService.isbnExists(formattedIsbn)) {
            bookDto.setIsbn(formattedIsbn);
            redirectAttributes.addFlashAttribute("bookDto", bookDto);
            return "redirect:/books/add";
        }

        //No need to check the id's validity since we already confirmed the book exists.
        Long bookIdByIsbn = this.bookService.findBookIdByIsbn(formattedIsbn);
        redirectAttributes.addAttribute("bookId", bookIdByIsbn);
        return "redirect:/quotes/add";
    }

    @GetMapping("/books/add")
    public String getAddBook(@ModelAttribute("bookDto") BookDto bookDto) {
        return "add-book";
    }

    @PostMapping("/books/add")
    public String addBook(@Valid BookDto bookDto,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "add-book";
        }

        this.bookService.addBook(bookDto);

        String isbn = bookDto.getIsbn();
        this.logger.info("Added new book with ISBN {}", isbn);

        Long bookIdByIsbn = this.bookService.findBookIdByIsbn(isbn);
        redirectAttributes.addAttribute("bookId", bookIdByIsbn);
        return "redirect:/quotes/add";
    }
}