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
import com.softuni.quotependium.utils.QuoteUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import static com.softuni.quotependium.utils.FormattingUtils.removeQuotes;
import static com.softuni.quotependium.utils.QuoteUtils.getNullPlaceholderQuoteView;
import static com.softuni.quotependium.utils.QuoteUtils.mapQuoteEntityToView;

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

    public void addQuote(QuoteImportDto quoteImportDto, Long currentUserId) {
        BookEntity bookEntity = this.bookRepository.findById(quoteImportDto.getBookId()).get();
        UserEntity userEntity = this.userRepository.findById(currentUserId).get();

        quoteImportDto.setBook(bookEntity);
        quoteImportDto.setUser(userEntity);
        quoteImportDto.setLikes(0);
        quoteImportDto.setText(removeQuotes(quoteImportDto.getText()));

        QuoteEntity mappedEntity = new QuoteEntity()
                .setBook(bookEntity)
                .setLikes(0)
                .setText(quoteImportDto.getText())
                .setUser(userEntity)
                .setPageNumber(quoteImportDto.getPageNumber());

        this.quoteRepository.saveAndFlush(mappedEntity);
    }

    public QuoteView getRandomQuoteView() {

        if (this.quoteRepository.count() == 0) {
            return getNullPlaceholderQuoteView();
        }

        return mapQuoteEntityToView(this.quoteRepository.findRandomQuote());
    }

    public Page<QuoteView> getAllQuotes(Pageable pageable) {
        return this.quoteRepository.findAll(pageable)
                .map(QuoteUtils::mapQuoteEntityToView);
    }

    public Page<QuoteView> getAllQuotesByBookId(Long bookId, Pageable pageable) {
        return this.quoteRepository.findAllByBookId(bookId, pageable)
                .map(QuoteUtils::mapQuoteEntityToView);
    }
}
