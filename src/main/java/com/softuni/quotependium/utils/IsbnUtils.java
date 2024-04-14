package com.softuni.quotependium.utils;

public class IsbnUtils {

    public static boolean checkIsbnValidity(String formattedIsbn) {
        return formattedIsbn.length() == 13 && isValidLong(formattedIsbn);
    }

    public static String formatIsbn(String isbn) {
        isbn = isbn.toUpperCase().trim();

        if (isbn.startsWith("ISBN")) {
            isbn = isbn.substring(4);
        }

        return isbn = isbn.replace("-", "").trim();
    }

    private static boolean isValidLong(String formattedIsbn) {

        try {
            long isbnInt = Long.parseLong(formattedIsbn);

        } catch (NumberFormatException numberFormatException) {
            return false;

        }
        return true;
    }
}
