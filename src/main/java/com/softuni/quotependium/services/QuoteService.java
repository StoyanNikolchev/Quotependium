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
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import static com.softuni.quotependium.utils.FormattingUtils.removeQuotes;

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
        Optional<QuoteEntity> quoteWithTopId = this.quoteRepository.findTopByOrderByIdDesc();

        if (quoteWithTopId.isEmpty()) {
            return null;
        }

        Long topId = quoteWithTopId.get().getId();
        Random random = new Random();

        Optional<QuoteEntity> randomQuoteOptional = Optional.empty();

        while (randomQuoteOptional.isEmpty()) {
            Long randomId = random.nextLong(topId + 1);
            randomQuoteOptional = this.quoteRepository.findById(randomId);
        }

        return map(randomQuoteOptional.get());
    }

    private QuoteView map(QuoteEntity randomQuoteEntity) {
        return new QuoteView()
                .setText(randomQuoteEntity.getText())
                .setAuthors(randomQuoteEntity.getBook()
                        .getAuthors().stream()
                        .map(AuthorEntity::getFullName)
                        .collect(Collectors.toList()))
                .setBookTitle(randomQuoteEntity.getBook().getTitle());
    }
}
