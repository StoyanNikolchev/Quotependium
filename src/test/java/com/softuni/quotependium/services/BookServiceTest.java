package com.softuni.quotependium.services;

import com.softuni.quotependium.domain.dtos.BookDto;
import com.softuni.quotependium.domain.entities.AuthorEntity;
import com.softuni.quotependium.domain.entities.BookEntity;
import com.softuni.quotependium.domain.views.BookDetailsView;
import com.softuni.quotependium.domain.views.BookTitleView;
import com.softuni.quotependium.repositories.AuthorRepository;
import com.softuni.quotependium.repositories.BookRepository;
import com.softuni.quotependium.testUtils.TestUtils;
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
        BookEntity testBook = TestUtils.getTwoTestBookEntities().get(0);
        when(bookRepository.findBookEntityByIsbn(testBook.getIsbn())).thenReturn(Optional.of(testBook));

        //ACT
        boolean result = bookService.isbnExists(testBook.getIsbn());

        //ASSERT
        assertTrue(result);
    }

    @Test
    public void givenExistingBookId_findBookById_returnsBookDetailsView() {
        //ARRANGE
        BookEntity testBook = TestUtils.getTwoTestBookEntities().get(0);
        AuthorEntity testAuthor = TestUtils.getTwoTestAuthorEntities().get(0);

        when(bookRepository.findById(testBook.getId())).thenReturn(Optional.of(testBook));

        //ACT
        BookDetailsView result = bookService.findBookById(testBook.getId());

        //ASSERT
        assertEquals(testBook.getId(), result.getId());
        assertEquals(testBook.getTitle(), result.getTitle());
        assertEquals(testBook.getPublicationYear(), result.getPublicationYear());
        assertEquals(testBook.getIsbn(), result.getIsbn());
        assertEquals(List.of(testAuthor.getFullName()), result.getAuthors());
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
        BookEntity testBook = TestUtils.getTwoTestBookEntities().get(0);
        when(bookRepository.findBookEntityByIsbn(testBook.getIsbn())).thenReturn(Optional.of(testBook));

        //ACT
        Long result = bookService.findBookIdByIsbn(testBook.getIsbn());

        //ASSERT
        assertEquals(id, result);
    }

    @Test
    public void givenBookDtoWithAuthors_addBook_savesAndFlushesBookEntity() {
        //ARRANGE
        BookDto bookDto = new BookDto()
                .setAuthorsString("Author 1, Author 2")
                .setTitle("Test Title")
                .setIsbn("1234567890000")
                .setPublicationYear(2013);

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
        List<BookEntity> books = TestUtils.getTwoTestBookEntities();

        Pageable pageable = mock(Pageable.class);
        Page<BookEntity> page = new PageImpl<>(books);

        when(bookRepository.findAll(pageable)).thenReturn(page);

        //ACT
        Page<BookTitleView> result = bookService.getAllTitles(pageable);

        //ASSERT
        assertEquals(2, result.getTotalElements());

        assertEquals(1L, result.getContent().get(0).getId());
        assertEquals(books.get(0).getTitle(), result.getContent().get(0).getTitle());

        assertEquals(2L, result.getContent().get(1).getId());
        assertEquals(books.get(1).getTitle(), result.getContent().get(1).getTitle());
    }

    @Test
    public void findBooksByAuthorId_shouldReturnPageOfBookTitleViewsByAuthorId() {
        //ARRANGE
        long authorId = 1L;
        List<BookEntity> books = TestUtils.getTwoTestBookEntities();

        Pageable pageable = mock(Pageable.class);
        Page<BookEntity> page = new PageImpl<>(books);

        when(bookRepository.findBooksByAuthorId(authorId, pageable)).thenReturn(page);

        //ACT
        Page<BookTitleView> result = bookService.findBooksByAuthorId(authorId, pageable);

        //ASSERT
        assertEquals(2, result.getTotalElements());
        assertEquals(books.get(0).getTitle(), result.getContent().get(0).getTitle());
        assertEquals(books.get(1).getTitle(), result.getContent().get(1).getTitle());
    }
}