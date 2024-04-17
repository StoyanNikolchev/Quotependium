package com.softuni.quotependium.services.schedulers;

import com.softuni.quotependium.services.QuoteOfTheDayService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.annotation.Scheduled;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class QuoteOfTheDaySchedulerTest {

    @Mock
    private QuoteOfTheDayService quoteOfTheDayService;
    private QuoteOfTheDayScheduler scheduler;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        scheduler = new QuoteOfTheDayScheduler(quoteOfTheDayService);
    }

    @Test
    public void testUpdateQuoteOfTheDay() {
        //ACT
        scheduler.updateQuoteOfTheDay();

        //ASSERT
        verify(quoteOfTheDayService, times(1)).updateQuoteOfTheDay();
    }

    @Test
    public void testScheduledAnnotation() throws NoSuchMethodException {
        //ARRANGE
        Scheduled scheduledAnnotation = QuoteOfTheDayScheduler.class.getDeclaredMethod("updateQuoteOfTheDay")
                .getAnnotation(Scheduled.class);

        //ACT
        String cronExpression = scheduledAnnotation.cron();

        //ASSERT
        assertEquals("0 0 0 * * *", cronExpression);
    }
}