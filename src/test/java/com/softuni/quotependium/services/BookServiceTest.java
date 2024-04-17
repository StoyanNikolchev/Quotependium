package com.softuni.quotependium.services;

import com.softuni.quotependium.domain.dtos.BookDto;
import com.softuni.quotependium.domain.entities.AuthorEntity;
import com.softuni.quotependium.domain.entities.BookEntity;
import com.softuni.quotependium.domain.views.BookDetailsView;
import com.softuni.quotependium.domain.views.BookTitleView;
import com.softuni.quotependium.repositories.AuthorRepository;
import com.softuni.quotependium.repositories.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookServiceTest {
    private final String testFormattedIsbn = "1234567890123";
    private final String testBookTitleOne = "Test Book";
    private final String testBookTitleTwo = "Test Book 2";
    private final int testBookPublicationYear = 2000;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private ModelMapper modelMapper;
    private BookService bookService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        bookService = new BookService(bookRepository, authorRepository, modelMapper);
    }

    @Test
    public void givenExistingIsbn_isbnExists_returnsTrue() {
        //ARRANGE
        when(bookRepository.findBookEntityByIsbn(testFormattedIsbn)).thenReturn(Optional.of(new BookEntity()));

        //ACT
        boolean result = bookService.isbnExists(testFormattedIsbn);

        //ASSERT
        assertTrue(result);
    }

    @Test
    public void givenExistingBookId_findBookById_returnsBookDetailsView() {
        //ARRANGE
        Long id = 1L;

        BookEntity bookEntity = new BookEntity().
                setId(id)
                .setTitle(testBookTitleOne)
                .setAuthors(Set.of(new AuthorEntity().setId(1L).setFullName("Test Author")))
                .setPublicationYear(testBookPublicationYear)
                .setIsbn(testFormattedIsbn);

        when(bookRepository.findById(id)).thenReturn(Optional.of(bookEntity));

        //ACT
        BookDetailsView result = bookService.findBookById(id);

        //ASSERT
        assertEquals(id, result.getId());
        assertEquals(testBookTitleOne, result.getTitle());
        assertEquals(testBookPublicationYear, result.getPublicationYear());
        assertEquals(testFormattedIsbn, result.getIsbn());
        assertEquals(List.of("Test Author"), result.getAuthors());
    }

    @Test
    public void givenNonexistentId_findBookById_returnsNull() {
        //ARRANGE
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        //ACT
        BookDetailsView result = bookService.findBookById(1L);

        //ASSERT
        assertNull(result);
    }

    @Test
    public void givenExistingIsbn_findBookIdByIsbn_returnsBookId() {
        //ARRANGE
        Long id = 1L;
        when(bookRepository.findBookEntityByIsbn(testFormattedIsbn)).thenReturn(Optional.of(new BookEntity().setId(id)));

        //ACT
        Long result = bookService.findBookIdByIsbn(testFormattedIsbn);

        //ASSERT
        assertEquals(id, result);
    }

    @Test
    public void givenBookDtoWithAuthors_addBook_savesAndFlushesBookEntity() {
        //ARRANGE
        BookDto bookDto = new BookDto()
                .setAuthorsString("Author 1, Author 2")
                .setTitle(testBookTitleOne)
                .setIsbn(testFormattedIsbn)
                .setPublicationYear(testBookPublicationYear);

        AuthorEntity authorEntity1 = new AuthorEntity().setFullName("Author 1");

        when(authorRepository.findAuthorEntityByFullName("Author 1")).thenReturn(Optional.of(authorEntity1));
        when(authorRepository.findAuthorEntityByFullName("Author 2")).thenReturn(Optional.empty());
        when(modelMapper.map(bookDto, BookEntity.class)).thenReturn(
                new BookEntity()
                        .setAuthors(bookDto.getAuthors())
                        .setIsbn(bookDto.getIsbn())
                        .setTitle(bookDto.getTitle())
                        .setPublicationYear(bookDto.getPublicationYear()));

        //ACT
        bookService.addBook(bookDto);

        //ASSERT
        verify(bookRepository, times(1)).saveAndFlush(any(BookEntity.class));
    }

    @Test
    public void getAllTitles_shouldReturnPageOfBookTitleViews() {
        //ARRANGE
        List<BookEntity> books = List.of(
                new BookEntity().setId(1L).setTitle(testBookTitleOne),
                new BookEntity().setId(2L).setTitle(testBookTitleTwo)
        );

        Pageable pageable = mock(Pageable.class);
        Page<BookEntity> page = new PageImpl<>(books);

        when(bookRepository.findAll(pageable)).thenReturn(page);

        //ACT
        Page<BookTitleView> result = bookService.getAllTitles(pageable);

        //ASSERT
        assertEquals(2, result.getTotalElements());

        assertEquals(1L, result.getContent().get(0).getId());
        assertEquals(testBookTitleOne, result.getContent().get(0).getTitle());

        assertEquals(2L, result.getContent().get(1).getId());
        assertEquals(testBookTitleTwo, result.getContent().get(1).getTitle());
    }

    @Test
    public void findBooksByAuthorId_shouldReturnPageOfBookTitleViewsByAuthorId() {
        //ARRANGE
        long authorId = 1L;
        List<BookEntity> books = List.of(
                new BookEntity().setId(1L).setTitle(testBookTitleOne),
                new BookEntity().setId(2L).setTitle(testBookTitleTwo)
        );
        Pageable pageable = mock(Pageable.class);
        Page<BookEntity> page = new PageImpl<>(books);

        when(bookRepository.findBooksByAuthorId(authorId, pageable)).thenReturn(page);

        //ACT
        Page<BookTitleView> result = bookService.findBooksByAuthorId(authorId, pageable);

        //ASSERT
        assertEquals(2, result.getTotalElements());
        assertEquals(testBookTitleOne, result.getContent().get(0).getTitle());
        assertEquals(testBookTitleTwo, result.getContent().get(1).getTitle());
    }
}