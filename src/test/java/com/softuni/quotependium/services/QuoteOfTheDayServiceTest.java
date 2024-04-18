package com.softuni.quotependium.services;

import com.softuni.quotependium.domain.entities.*;
import com.softuni.quotependium.domain.views.QuoteView;
import com.softuni.quotependium.repositories.QuoteOfTheDayRepository;
import com.softuni.quotependium.repositories.QuoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

public class QuoteOfTheDayServiceTest {

    @Captor
    ArgumentCaptor<QuoteOfTheDayEntity> quoteOfTheDayEntityArgumentCaptor;

    @Mock
    private QuoteRepository quoteRepository;

    @Mock
    private QuoteOfTheDayRepository quoteOfTheDayRepository;

    @InjectMocks
    private QuoteOfTheDayService quoteOfTheDayService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void whenQuoteRepositoryIsEmpty_getQuoteOfTheDayView_shouldReturnNullPlaceholderView() {
        //ARRANGE
        when(quoteRepository.count()).thenReturn(0L);

        //ACT
        QuoteView quoteOfTheDayView = quoteOfTheDayService.getQuoteOfTheDayView();

        //ASSERT
        assertEquals("Error: no quote of the day!", quoteOfTheDayView.getText());
        assertNull(quoteOfTheDayView.getId());
        assertEquals("Book: The Book of Nothingness", quoteOfTheDayView.getBookTitle());
        assertEquals(List.of("Mr Nobody"), quoteOfTheDayView.getAuthors());
    }

    @Test
    public void whenQuoteOfTheDayIsNotPresentAndDbIsInit_getQuoteOfTheDayView_shouldReturnNullPlaceholderView() {
        //ARRANGE
        when(quoteRepository.count()).thenReturn(1L);
        doReturn(Optional.empty())
                .when(quoteOfTheDayRepository)
                .findQuoteOfTheDayEntityByDate(LocalDate.now());

        //ACT
        QuoteView quoteOfTheDayView = quoteOfTheDayService.getQuoteOfTheDayView();

        //ASSERT
        assertEquals("Error: no quote of the day!", quoteOfTheDayView.getText());
        assertNull(quoteOfTheDayView.getId());
        assertEquals("Book: The Book of Nothingness", quoteOfTheDayView.getBookTitle());
        assertEquals(List.of("Mr Nobody"), quoteOfTheDayView.getAuthors());
    }

    @Test
    public void whenQuoteOfTheDayExists_getQuoteOfTheDayView_shouldReturnIt() {
        //ARRANGE
        QuoteOfTheDayEntity quoteOfTheDayEntity = new QuoteOfTheDayEntity()
                .setQuoteId(1L)
                .setId(1L)
                .setDate(LocalDate.now());

        QuoteEntity quoteEntity = new QuoteEntity()
                .setId(1L)
                .setBook(new BookEntity()
                        .setTitle("Title")
                        .setAuthors(Set.of(new AuthorEntity().setFullName("Pesho Baftata"))))
                .setText("Text")
                .setLikes(0)
                .setUser(new UserEntity())
                .setPageNumber(1);

        when(quoteRepository.count()).thenReturn(1L);

        doReturn(Optional.of(quoteEntity))
                .when(quoteRepository)
                .findById(1L);

        doReturn(Optional.of(quoteOfTheDayEntity))
                .when(quoteOfTheDayRepository)
                .findQuoteOfTheDayEntityByDate(LocalDate.now());

        //ACT
        QuoteView quoteOfTheDayView = quoteOfTheDayService.getQuoteOfTheDayView();

        //ASSERT
        assertEquals(quoteOfTheDayEntity.getId(), quoteOfTheDayView.getId());

        assertEquals("Book: " + quoteEntity.getBook().getTitle(), quoteOfTheDayView.getBookTitle());
        assertEquals(quoteEntity.getText(), quoteOfTheDayView.getText());
        assertEquals(quoteEntity.getBook().getAuthors().stream().map(AuthorEntity::getFullName).collect(Collectors.toList()), quoteOfTheDayView.getAuthors());
    }

    @Test
    public void whenDbIsEmpty_updateQuoteOfTheDay_shouldDoNothing() {
        //ARRANGE
        when(quoteRepository.count()).thenReturn(0L);

        //ACT
        quoteOfTheDayService.updateQuoteOfTheDay();

        //ASSERT
        verify(quoteRepository, times(0)).findRandomQuote();
        verify(quoteOfTheDayRepository, times(0)).saveAndFlush(any());
    }

    @Test
    public void whenDbIsInit_updateQuoteOfTheDay_shouldUpdateTheQuoteOfTheDay() {
        //ARRANGE
        QuoteEntity randomQuote = new QuoteEntity()
                .setId(1L);

        when(quoteRepository.count()).thenReturn(1L);
        doReturn(randomQuote).when(quoteRepository).findRandomQuote();

        //ACT
        quoteOfTheDayService.updateQuoteOfTheDay();

        //ASSERT
        verify(quoteOfTheDayRepository, times(1)).saveAndFlush(quoteOfTheDayEntityArgumentCaptor.capture());
        QuoteOfTheDayEntity quoteOfTheDayEntity = quoteOfTheDayEntityArgumentCaptor.getValue();
        assertEquals(randomQuote.getId(), quoteOfTheDayEntity.getQuoteId());
        assertEquals(LocalDate.now(), quoteOfTheDayEntity.getDate());
    }
}