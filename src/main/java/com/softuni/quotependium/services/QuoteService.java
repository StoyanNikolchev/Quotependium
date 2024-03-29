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
    private final ModelMapper modelMapper;

    @Autowired
    public QuoteService(QuoteRepository quoteRepository, BookRepository bookRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.quoteRepository = quoteRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public void addQuote(QuoteDto quoteDto, Long bookId, Long currentUserId) {
        BookEntity bookEntity = this.bookRepository.findById(bookId).get();
        UserEntity userEntity = this.userRepository.findById(currentUserId).get();

        quoteDto.setBook(bookEntity);
        quoteDto.setUser(userEntity);
        quoteDto.setLikes(0);

        this.quoteRepository.saveAndFlush(this.modelMapper.map(quoteDto, QuoteEntity.class));
    }
}
