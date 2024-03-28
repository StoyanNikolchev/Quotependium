package com.softuni.quotependium.services;

import com.softuni.quotependium.domain.dtos.BookDto;
import com.softuni.quotependium.domain.entities.AuthorEntity;
import com.softuni.quotependium.domain.entities.BookEntity;
import com.softuni.quotependium.repositories.AuthorRepository;
import com.softuni.quotependium.repositories.BookRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

import static com.softuni.quotependium.utils.FormattingUtils.formatAuthorsIntoSet;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public BookService(BookRepository bookRepository, AuthorRepository authorRepository, ModelMapper modelMapper) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.modelMapper = modelMapper;
    }

    public boolean isbnExists(String formattedIsbn) {
        return this.bookRepository.findBookEntityByIsbn(formattedIsbn).isPresent();
    }

    public void addBook(BookDto bookDto) {
        bookDto.setAuthors(getAuthorEntities(formatAuthorsIntoSet(bookDto.getAuthorsString())));
        this.bookRepository.saveAndFlush(this.modelMapper.map(bookDto, BookEntity.class));
    }

    private Set<AuthorEntity> getAuthorEntities(Set<String> names) {
        Set<AuthorEntity> authors = new HashSet<>();

        for (String name : names) {
            AuthorEntity author = this.authorRepository.findAuthorEntityByFullName(name)
                    .orElseGet(() -> {
                        AuthorEntity newAuthor = new AuthorEntity();
                        newAuthor.setFullName(name);
                        return authorRepository.save(newAuthor);
                    });

            authors.add(author);
        }
        return authors;
    }
}
