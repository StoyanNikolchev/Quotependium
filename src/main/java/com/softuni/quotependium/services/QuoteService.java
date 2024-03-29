package com.softuni.quotependium.services;

import com.softuni.quotependium.domain.dtos.QuoteDto;
import com.softuni.quotependium.domain.entities.BookEntity;
import com.softuni.quotependium.domain.entities.QuoteEntity;
import com.softuni.quotependium.domain.entities.UserEntity;
import com.softuni.quotependium.repositories.BookRepository;
import com.softuni.quotependium.repositories.QuoteRepository;
import com.softuni.quotependium.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuoteService {
    private final QuoteRepository quoteRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Autowired
    public QuoteService(QuoteRepository quoteRepository, BookRepository bookRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.quoteRepository = quoteRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    public void addQuote(QuoteDto quoteDto, Long currentUserId) {
        BookEntity bookEntity = this.bookRepository.findById(quoteDto.getBookId()).get();
        UserEntity userEntity = this.userRepository.findById(currentUserId).get();

        quoteDto.setBook(bookEntity);
        quoteDto.setUser(userEntity);
        quoteDto.setLikes(0);

        QuoteEntity mappedEntity = new QuoteEntity()
                .setBook(bookEntity)
                .setLikes(0)
                .setText(quoteDto.getText())
                .setUser(userEntity)
                .setPageNumber(quoteDto.getPageNumber());

        this.quoteRepository.saveAndFlush(mappedEntity);
    }
}
