package com.softuni.quotependium.utils;

public class IsbnUtils {

    public static boolean checkIsbnValidity(String formattedIsbn) {
        return formattedIsbn.length() == 13 && isValidLong(formattedIsbn);
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
