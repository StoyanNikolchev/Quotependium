package com.softuni.quotependium.web;

import com.softuni.quotependium.domain.views.AuthorView;
import com.softuni.quotependium.domain.views.BookDetailsView;
import com.softuni.quotependium.domain.views.BookTitleView;
import com.softuni.quotependium.domain.views.QuoteView;
import com.softuni.quotependium.services.AuthorService;
import com.softuni.quotependium.services.BookService;
import com.softuni.quotependium.services.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/browse")
public class BrowseController {
    private final QuoteService quoteService;
    private final BookService bookService;
    private final AuthorService authorService;

    @Autowired
    public BrowseController(QuoteService quoteService, BookService bookService, AuthorService authorService) {
        this.quoteService = quoteService;
        this.bookService = bookService;
        this.authorService = authorService;
    }

    @GetMapping("/authors")
    public String getAllAuthors(Model model,
                                @PageableDefault(
                                        sort = "id",
                                        direction = Sort.Direction.ASC,
                                        size = 10,
                                        page = 0)
                                Pageable pageable) {
        Page<AuthorView> authorsPage = this.authorService.getAllAuthors(pageable);
        model.addAttribute("authors", authorsPage);
        return "browse-authors";
    }

    @GetMapping("authors/{authorId}")
    public String getAuthorDetails(@PathVariable Long authorId,
                                   Model model,
                                   @PageableDefault(
                                           sort = "id",
                                           direction = Sort.Direction.ASC,
                                           size = 3,
                                           page = 0)
                                       Pageable pageable) {

        AuthorView authorById = this.authorService.findAuthorById(authorId);

        if (authorById == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found");
        }

        Page<BookTitleView> books = this.bookService.findBooksByAuthorId(authorId, pageable);
        model.addAttribute("author", authorById);
        model.addAttribute("books", books);

        return "author-details";
    }

    @GetMapping("/quotes")
    public String getAllQuotes(Model model,
                               @PageableDefault(
                                       sort = "id",
                                       direction = Sort.Direction.ASC,
                                       size = 3,
                                       page = 0)
                               Pageable pageable) {

        Page<QuoteView> quotesPage = this.quoteService.getAllQuotes(pageable);
        model.addAttribute("quotes", quotesPage);
        return "browse-quotes";
    }

    @GetMapping("/books")
    public String getAllBooks(Model model,
                              @PageableDefault(
                                      sort = "id",
                                      direction = Sort.Direction.ASC,
                                      size = 10,
                                      page = 0)
                              Pageable pageable) {

        Page<BookTitleView> allTitlesPage = this.bookService.getAllTitles(pageable);
        model.addAttribute("bookTitles", allTitlesPage);
        return "browse-books";
    }

    @GetMapping("books/{bookId}")
    public String getBookDetails(@PathVariable Long bookId,
                                 Model model,
                                 @PageableDefault(
                                         sort = "id",
                                         direction = Sort.Direction.ASC,
                                         size = 3,
                                         page = 0)
                                 Pageable pageable) {
        BookDetailsView bookById = this.bookService.findBookById(bookId);

        if (bookById == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
        }

        Page<QuoteView> quotes = this.quoteService.getAllQuotesByBookId(bookId, pageable);
        model.addAttribute("book", bookById);
        model.addAttribute("quotes", quotes);

        return "book-details";
    }
}