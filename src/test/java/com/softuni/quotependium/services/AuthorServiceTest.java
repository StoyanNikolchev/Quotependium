package com.softuni.quotependium.services;

import com.softuni.quotependium.domain.entities.AuthorEntity;
import com.softuni.quotependium.domain.views.AuthorView;
import com.softuni.quotependium.repositories.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

public class AuthorServiceTest {
    private final String testAuthorNameOne = "Author 1";
    private final String testAuthorNameTwo = "Author 2";

    @Mock
    private AuthorRepository authorRepository;
    private AuthorService authorService;

    @BeforeEach
    public void setUp() {
        authorRepository = mock(AuthorRepository.class);
        authorService = new AuthorService(authorRepository);
    }

    @Test
    public void givenAuthorsInRepository_getAllAuthors_returnsAuthorsPage() {
        //ARRANGE
        Pageable pageable = mock(Pageable.class);
        List<AuthorEntity> authorEntities = List.of(
                new AuthorEntity().setFullName(testAuthorNameOne).setId(1L),
                new AuthorEntity().setFullName(testAuthorNameTwo).setId(2L)
        );
        Page<AuthorEntity> authorEntitiesPage = new PageImpl<>(authorEntities);
        when(authorRepository.findAll(pageable)).thenReturn(authorEntitiesPage);

        //ACT
        Page<AuthorView> result = authorService.getAllAuthors(pageable);

        //ASSERT
        assertEquals(2, result.getTotalElements());
        verify(authorRepository, times(1)).findAll(pageable);
    }

    @Test
    public void givenExistingAuthorId_findAuthorById_returnsAuthorView() {
        //ARRANGE
        Long id = 1L;
        AuthorEntity authorEntity = new AuthorEntity();
        authorEntity.setId(id);
        authorEntity.setFullName(testAuthorNameOne);
        when(authorRepository.findAuthorEntityById(id)).thenReturn(Optional.of(authorEntity));

        //ACT
        AuthorView authorView = authorService.findAuthorById(id);

        //ASSERT
        assertEquals(id, authorView.getId());
        assertEquals(testAuthorNameOne, authorView.getFullName());
        verify(authorRepository, times(1)).findAuthorEntityById(id);
    }

    @Test
    public void givenNonExistentAuthorId_findAuthorById_returnsNull() {
        //ARRANGE
        Long id = 1L;
        when(authorRepository.findAuthorEntityById(id)).thenReturn(Optional.empty());

        //ACT
        AuthorView authorView = authorService.findAuthorById(id);

        //ASSERT
        assertNull(authorView);
        verify(authorRepository, times(1)).findAuthorEntityById(id);
    }
}