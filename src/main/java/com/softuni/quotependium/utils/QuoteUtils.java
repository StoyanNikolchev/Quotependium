package com.softuni.quotependium.utils;

import com.softuni.quotependium.domain.entities.AuthorEntity;
import com.softuni.quotependium.domain.entities.QuoteEntity;
import com.softuni.quotependium.domain.views.QuoteView;

import java.util.List;
import java.util.stream.Collectors;

public class QuoteUtils {

    public static QuoteView mapQuoteEntityToView(QuoteEntity quoteEntity) {
        return new QuoteView()
                .setId(quoteEntity.getId())
                .setText(quoteEntity.getText())
                .setAuthors(quoteEntity.getBook()
                        .getAuthors().stream()
                        .map(AuthorEntity::getFullName)
                        .collect(Collectors.toList()))
                .setBookTitle(quoteEntity.getBook().getTitle());
    }

    public static QuoteView getNullPlaceholderQuoteView() {
        return new QuoteView()
                .setText("NULL. No quotes in the database!!!")
                .setId(null)
                .setBookTitle("The Book of Nothingness")
                .setAuthors(List.of("Mr Nobody"));
    }
}
