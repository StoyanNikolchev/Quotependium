package com.softuni.quotependium.web;

import com.softuni.quotependium.domain.dtos.BookDto;
import com.softuni.quotependium.services.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerTestIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Test
    public void getIsbnCheck_whenLoggedOut_shouldRedirectToLogin() throws Exception {
        mockMvc.perform(get("/books/check"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/users/login"));
    }

    @Test
    @WithMockUser
    public void getIsbnCheck_whenLoggedIn_shouldGetThePage() throws Exception {
        mockMvc.perform(get("/books/check"))
                .andExpect(status().isOk())
                .andExpect(view().name("check-book"));
    }

    @Test
    @WithMockUser
    public void postIsbnCheck_whenIsbnIsInvalid_shouldReturnCheckBookViewWithFieldErrors() throws Exception {
        mockMvc.perform(post("/books/check").param("isbn", "invalid-isbn").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("check-book"))
                .andExpect(model().attributeHasFieldErrors("bookDto", "isbn"));

    }

    @Test
    @WithMockUser
    public void postIsbnCheck_whenIsbnDoesNotExist_shouldRedirectToBooksAdd() throws Exception {
        when(bookService.isbnExists(anyString())).thenReturn(false);

        mockMvc.perform(post("/books/check").param("isbn", "1234567890000").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books/add"))
                .andExpect(flash().attributeExists("bookDto"));
    }

    @Test
    @WithMockUser
    public void postIsbnCheck_whenIsbnExists_shouldRedirectToQuotesAdd() throws Exception {
        when(bookService.isbnExists(anyString())).thenReturn(true);

        mockMvc.perform(post("/books/check").param("isbn", "1234567890000").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/quotes/add?**"))
                .andExpect(model().attributeExists("bookId"));
    }

    @Test
    @WithMockUser
    public void getAddBook_withLoggedInUser_shouldReturnBooksAdd() throws Exception {
        mockMvc.perform(get("/books/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-book"));
    }

    @Test
    public void getAddBook_withoutLoggedInUser_ShouldRedirectToLogin() throws Exception {
        mockMvc.perform(get("/books/add"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/users/login"));
    }

    @Test
    @WithMockUser
    public void addBook_whenValidationErrors_shouldReturnAddBookViewWithErrors() throws Exception {
        mockMvc.perform(post("/books/add")
                        .param("isbn", "")
                        .param("title", "No")
                        .param("authorsString", "Short")
                        .param("publicationYear", "9999")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("add-book"))
                .andExpect(model().attributeHasFieldErrors("bookDto", "isbn"))
                .andExpect(model().attributeHasFieldErrors("bookDto", "title"))
                .andExpect(model().attributeHasFieldErrors("bookDto", "authorsString"))
                .andExpect(model().attributeHasFieldErrors("bookDto", "publicationYear"));
    }

    @Test
    @WithMockUser
    public void addBook_whenDataIsValid_shouldAddBookAndRedirectToAddQuote() throws Exception {
        when(bookService.findBookIdByIsbn("1234567890000")).thenReturn(Long.valueOf(1L));

        mockMvc.perform(post("/books/add")
                        .param("isbn", "1234567890000")
                        .param("title", "Valid Title")
                        .param("authorsString", "Some Author")
                        .param("publicationYear", "2012")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/quotes/add?bookId=1"));
    }
}