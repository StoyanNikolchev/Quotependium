package com.softuni.quotependium.services;

import com.softuni.quotependium.domain.dtos.QuoteImportDto;
import com.softuni.quotependium.domain.entities.BookEntity;
import com.softuni.quotependium.domain.entities.QuoteEntity;
import com.softuni.quotependium.domain.entities.UserEntity;
import com.softuni.quotependium.domain.views.QuoteView;
import com.softuni.quotependium.repositories.BookRepository;
import com.softuni.quotependium.repositories.QuoteRepository;
import com.softuni.quotependium.repositories.UserRepository;
import com.softuni.quotependium.utils.QuoteUtils;
import com.softuni.quotependium.utils.SecurityUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Page<QuoteView> getCurrentUserFavoriteQuotes(Pageable pageable) {
        String currentUserUsername = SecurityUtils.getCurrentUser().getName();
        Long currentUserId = this.userRepository
                .findUserEntityByUsername(currentUserUsername)
                .get()
                .getId();

        return this.quoteRepository.findQuotesLikedByUser(currentUserId, pageable)
                .map(QuoteUtils::mapQuoteEntityToView);
    }

    @Transactional
    public boolean toggleLike(Long quoteId, String username) {
        UserEntity user = userRepository.findUserEntityByUsername(username).get();
        QuoteEntity quote = quoteRepository.findById(quoteId).get();

        if (user.getLikedQuotes().contains(quote)) {
            user.getLikedQuotes().remove(quote);
            quote.getLikedByUsers().remove(user);
            quote.setLikes(quote.getLikes() - 1);
        } else {
            user.getLikedQuotes().add(quote);
            quote.getLikedByUsers().add(user);
            quote.setLikes(quote.getLikes() + 1);
        }

        userRepository.save(user);
        quoteRepository.save(quote);

        return user.getLikedQuotes().contains(quote);
    }
}
