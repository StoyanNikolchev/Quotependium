package com.softuni.quotependium.services;

import com.softuni.quotependium.domain.dtos.BookDto;
import com.softuni.quotependium.domain.entities.AuthorEntity;
import com.softuni.quotependium.domain.entities.BaseEntity;
import com.softuni.quotependium.domain.entities.BookEntity;
import com.softuni.quotependium.domain.views.BookDetailsView;
import com.softuni.quotependium.domain.views.BookTitleView;
import com.softuni.quotependium.repositories.AuthorRepository;
import com.softuni.quotependium.repositories.BookRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

    public BookDetailsView findBookById(Long id) {
        Optional<BookEntity> bookOptional = this.bookRepository.findById(id);

        if (bookOptional.isEmpty()) {
            return null;
        }

        BookEntity bookById = bookOptional.get();

        return new BookDetailsView()
                .setIsbn(bookById.getIsbn())
                .setPublicationYear(bookById.getPublicationYear())
                .setTitle(bookById.getTitle())
                .setId(bookById.getId())
                .setAuthors(bookById.getAuthors()
                        .stream().map(AuthorEntity::getFullName).collect(Collectors.toList()));
    }

    public Long findBookIdByIsbn(String isbn) {
        Optional<BookEntity> bookOptional = this.bookRepository.findBookEntityByIsbn(isbn);

        return bookOptional.map(BaseEntity::getId).orElse(null);
    }

    public void addBook(BookDto bookDto) {
        bookDto.setAuthors(getAuthorEntitySetFromStringSet(formatAuthorsIntoSet(bookDto.getAuthorsString())));
        this.bookRepository.saveAndFlush(this.modelMapper.map(bookDto, BookEntity.class));
    }

    public Page<BookTitleView> getAllTitles(Pageable pageable) {
        return this.bookRepository.findAll(pageable)
                .map(book -> new BookTitleView()
                        .setTitle(book.getTitle())
                        .setId(book.getId()));
    }

    private Set<AuthorEntity> getAuthorEntitySetFromStringSet(Set<String> names) {
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
