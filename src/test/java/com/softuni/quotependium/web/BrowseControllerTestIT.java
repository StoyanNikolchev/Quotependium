package com.softuni.quotependium.web;

import com.softuni.quotependium.domain.views.AuthorView;
import com.softuni.quotependium.domain.views.BookDetailsView;
import com.softuni.quotependium.services.AuthorService;
import com.softuni.quotependium.services.BookService;
import com.softuni.quotependium.services.QuoteService;
import com.softuni.quotependium.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BrowseControllerTestIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private BookService bookService;

    @MockBean
    private QuoteService quoteService;

    @MockBean
    private UserService userService;

    @Test
    public void getAllAuthors_shouldReturnBrowseAuthors() throws Exception {
        when(authorService.getAllAuthors(any(Pageable.class))).thenReturn(mock(Page.class));

        mockMvc.perform(get("/browse/authors"))
                .andExpect(model().attributeExists("authors"))
                .andExpect(status().isOk())
                .andExpect(view().name("browse-authors"));
    }

    @Test
    public void getAuthorDetails_whenAuthorExists_shouldReturnAuthorDetails() throws Exception {
        when(authorService.findAuthorById(1L)).thenReturn(mock(AuthorView.class));
        when(bookService.findBooksByAuthorId(any(Long.class), any(Pageable.class))).thenReturn(mock(Page.class));

        mockMvc.perform(get("/browse/authors/1"))
                .andExpect(model().attributeExists("author"))
                .andExpect(model().attributeExists("books"))
                .andExpect(status().isOk())
                .andExpect(view().name("author-details"));
    }

    @Test
    public void getAuthorDetails_whenAuthorDoesNotExist_shouldReturnNotFound() throws Exception {
        when(authorService.findAuthorById(1L)).thenReturn(null);

        mockMvc.perform(get("/browse/authors/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getAllQuotes_shouldReturnBrowseQuotes() throws Exception {
        when(quoteService.getAllQuotes(any(Pageable.class))).thenReturn(mock(Page.class));
        when(userService.getCurrentUserId()).thenReturn(1L);

        mockMvc.perform(get("/browse/quotes"))
                .andExpect(model().attributeExists("currentUserId", "quotes"))
                .andExpect(status().isOk())
                .andExpect(view().name("browse-quotes"));

    }

    @Test
    public void getAllBooks_shouldReturn_browseBooks() throws Exception {
        when(bookService.getAllTitles(any(Pageable.class))).thenReturn(mock(Page.class));
        when(userService.getCurrentUserId()).thenReturn(1L);

        mockMvc.perform(get("/browse/books"))
                .andExpect(model().attributeExists("currentUserId", "bookTitles"))
                .andExpect(status().isOk())
                .andExpect(view().name("browse-books"));
    }

    @Test
    public void getBookDetails_whenBookDoesNotExist_shouldReturnNotFound() throws Exception {
        when(bookService.findBookById(1L)).thenReturn(null);

        mockMvc.perform(get("/browse/books/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getBookDetails_whenBookExists_shouldReturnBookDetails() throws Exception {
        when(bookService.findBookById(1L)).thenReturn(mock(BookDetailsView.class));
        when(quoteService.getAllQuotesByBookId(eq(1L), any(Pageable.class))).thenReturn(mock(Page.class));

        mockMvc.perform(get("/browse/books/1"))
                .andExpect(model().attributeExists("book", "quotes"))
                .andExpect(status().isOk())
                .andExpect(view().name("book-details"));
    }
}
