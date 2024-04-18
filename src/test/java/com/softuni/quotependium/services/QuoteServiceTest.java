package com.softuni.quotependium.services;

import com.softuni.quotependium.domain.dtos.QuoteImportDto;
import com.softuni.quotependium.domain.entities.AuthorEntity;
import com.softuni.quotependium.domain.entities.BookEntity;
import com.softuni.quotependium.domain.entities.QuoteEntity;
import com.softuni.quotependium.domain.entities.UserEntity;
import com.softuni.quotependium.domain.views.QuoteView;
import com.softuni.quotependium.repositories.BookRepository;
import com.softuni.quotependium.repositories.QuoteRepository;
import com.softuni.quotependium.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

public class QuoteServiceTest {

    @Captor
    ArgumentCaptor<QuoteEntity> quoteEntityArgumentCaptor;

    @Mock
    private QuoteRepository quoteRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private QuoteService quoteService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void whenGivenQuoteDtoAndUserId_addQuote_shouldSaveQuoteToRepository() {
        //ARRANGE
        BookEntity bookEntity = new BookEntity();
        UserEntity userEntity = new UserEntity();

        QuoteImportDto quoteImportDto = new QuoteImportDto()
                .setText("Text")
                .setBookId(1L)
                .setPageNumber(1);

        doReturn(Optional.of(bookEntity)).when(bookRepository).findById(1L);
        doReturn(Optional.of(userEntity)).when(userRepository).findById(1L);

        //ACT
        quoteService.addQuote(quoteImportDto, 1L);

        //ASSERT
        verify(quoteRepository, times(1)).saveAndFlush(quoteEntityArgumentCaptor.capture());

        QuoteEntity savedQuoteEntity = quoteEntityArgumentCaptor.getValue();

        assertEquals(bookEntity, savedQuoteEntity.getBook());
        assertEquals(userEntity, savedQuoteEntity.getUser());
        assertEquals(quoteImportDto.getText(), savedQuoteEntity.getText());
        assertEquals(0, savedQuoteEntity.getLikes());
        assertEquals(1, savedQuoteEntity.getPageNumber());
    }

    @Test
    public void whenQuoteRepositoryIsEmpty_getRandomQuoteView_shouldReturnNullPlaceholderView() {
        //ARRANGE
        when(quoteRepository.count()).thenReturn(0L);

        //ACT
        QuoteView randomQuoteView = quoteService.getRandomQuoteView();

        //ASSERT
        assertEquals("Error: no quote of the day!", randomQuoteView.getText());
        assertNull(randomQuoteView.getId());
        assertEquals("Book: The Book of Nothingness", randomQuoteView.getBookTitle());
        assertEquals(List.of("Mr Nobody"), randomQuoteView.getAuthors());
    }

    @Test
    public void whenQuoteRepositoryIsNotEmpty_getRandomQuoteView_shouldReturnQuoteView() {
        //ARRANGE
        QuoteEntity randomQuoteEntity = new QuoteEntity()
                .setId(1L)
                .setBook(new BookEntity().setTitle("Book Title")
                        .setAuthors(Set.of(new AuthorEntity().setFullName("Pesho Baftata"))))
                .setText("Text");

        when(quoteRepository.count()).thenReturn(1L);
        when(quoteRepository.findRandomQuote()).thenReturn(randomQuoteEntity);

        //ACT
        QuoteView randomQuoteView = quoteService.getRandomQuoteView();

        //ASSERT
        assertEquals(randomQuoteEntity.getId(), randomQuoteView.getId());
        assertEquals(randomQuoteEntity.getText(), randomQuoteView.getText());

        assertEquals("Book: " + randomQuoteEntity
                        .getBook()
                        .getTitle(),
                randomQuoteView.getBookTitle());

        assertEquals(randomQuoteEntity
                        .getBook()
                        .getAuthors()
                        .stream()
                        .map(AuthorEntity::getFullName)
                        .collect(Collectors.toList()),
                randomQuoteView.getAuthors());
    }

    @Test
    public void testGetAllQuotes() {
        //ARRANGE
        Pageable pageable = PageRequest.of(0, 10);

        List<QuoteEntity> quoteEntities = Arrays.asList(
                new QuoteEntity().setText("Text 1")
                        .setBook(new BookEntity()
                                .setAuthors(Set.of(new AuthorEntity()
                                        .setFullName("Pesho Baftata")))),
                new QuoteEntity().setText("Text 2")
                        .setBook(new BookEntity()
                                .setAuthors(Set.of(new AuthorEntity()
                                        .setFullName("Pesho Baftata")))));

        Page<QuoteEntity> quoteEntityPage = new PageImpl<>(quoteEntities);

        when(quoteRepository.findAll(pageable)).thenReturn(quoteEntityPage);

        //ACT
        Page<QuoteView> result = quoteService.getAllQuotes(pageable);

        //ASSERT
        assertEquals(quoteEntities.size(), result.getContent().size());
        assertEquals(quoteEntities.get(1).getText(), result.getContent().get(1).getText());
        assertEquals("Book: " + quoteEntities.get(0).getBook().getTitle(), result.getContent().get(0).getBookTitle());
    }

    @Test
    public void testGetAllQuotesByBookId() {
        //ASSERT
        Long bookId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        List<QuoteEntity> quoteEntities = Arrays.asList(
                new QuoteEntity().setText("Text 1")
                        .setBook(new BookEntity()
                                .setId(bookId)
                                .setAuthors(Set.of(new AuthorEntity()
                                        .setFullName("Pesho Baftata")))),
                new QuoteEntity().setText("Text 2")
                        .setBook(new BookEntity()
                                .setId(bookId)
                                .setAuthors(Set.of(new AuthorEntity()
                                        .setFullName("Pesho Baftata")))));

        Page<QuoteEntity> quoteEntityPage = new PageImpl<>(quoteEntities);

        when(quoteRepository.findAllByBookId(bookId, pageable)).thenReturn(quoteEntityPage);

        //ACT
        Page<QuoteView> result = quoteService.getAllQuotesByBookId(bookId, pageable);

        //ASSERT
        assertEquals(quoteEntities.size(), result.getContent().size());
        assertEquals(quoteEntities.get(0)
                .getBook().getAuthors()
                .stream().map(AuthorEntity::getFullName)
                .collect(Collectors.toList()),
                result.getContent().get(0).getAuthors());

        assertEquals(quoteEntities.get(1)
                .getBook().getAuthors()
                .stream()
                .map(AuthorEntity::getFullName)
                .collect(Collectors.toList()),
                result.getContent().get(1).getAuthors());
    }
}