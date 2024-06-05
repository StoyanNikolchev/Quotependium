package com.softuni.quotependium.testUtils;

import com.softuni.quotependium.domain.entities.*;
import com.softuni.quotependium.domain.enums.UserRoleEnum;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TestUtils {
    public static List<AuthorEntity> getTwoTestAuthorEntities() {
        return List.of(new AuthorEntity()
                        .setId(1L)
                        .setFullName("Pesho Baftata"),
                new AuthorEntity()
                        .setId(2L)
                        .setFullName("Besho Paftata"));
    }

    public static List<BookEntity> getTwoTestBookEntities() {
        return List.of(new BookEntity()
                        .setId(1L)
                        .setTitle("Test Title One")
                        .setIsbn("1111111111111")
                        .setPublicationYear(1990)
                        .setAuthors(Set.of(getTwoTestAuthorEntities().get(0))),
                new BookEntity()
                        .setId(2L)
                        .setTitle("Test Title Two")
                        .setIsbn("2222222222222")
                        .setPublicationYear(1980)
                        .setAuthors(new HashSet<>(getTwoTestAuthorEntities())));
    }

    public static UserEntity getTestUserEntity() {
        UserEntity user = new UserEntity().setId(1L)
                .setUsername("Pesho")
                .setEmail("test@test.com")
                .setFullName("Pesho Petrov")
                .setRoles(List.of(new UserRoleEntity().setRole(UserRoleEnum.USER)))
                .setPassword("secretPassword");

        Set<QuoteEntity> likedQuotes = new LinkedHashSet<>();
        likedQuotes.add(new QuoteEntity().setId(3L)
                .setText("Test quote text")
                .setLikes(4)
                .setLikedByUsers(Set.of(user)));
        likedQuotes.add(new QuoteEntity().setId(5L)
                .setText("Test quote text 2")
                .setLikes(5)
                .setLikedByUsers(Set.of(user)));

        user.setLikedQuotes(likedQuotes);
        return user;
    }
}