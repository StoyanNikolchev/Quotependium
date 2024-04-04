package com.softuni.quotependium.web;

import com.softuni.quotependium.domain.dtos.BookDto;
import com.softuni.quotependium.services.BookService;
import com.softuni.quotependium.utils.IsbnUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.softuni.quotependium.utils.FormattingUtils.formatIsbn;

@Controller
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
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
            bindingResult.addError(new FieldError("bookDto", "isbn", "Invalid ISBN format. Please check the back of your book for a 13-digit ISBN"));
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
        Long bookIdByIsbn = this.bookService.findBookIdByIsbn(bookDto.getIsbn());
        redirectAttributes.addAttribute("bookId", bookIdByIsbn);
        return "redirect:/quotes/add";
    }


}