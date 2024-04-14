package com.softuni.quotependium.utils;

import java.util.HashSet;
import java.util.Set;

public class FormattingUtils {

    public static Set<String> formatAuthorsIntoSet(String authors) {
        Set<String> authorNamesFormatted = new HashSet<>();
        String[] authorArray = authors.split(",");

        for (String author : authorArray) {
            String correctedAuthorName = correctAuthorName(author.trim());
            authorNamesFormatted.add(correctedAuthorName);
        }

        return authorNamesFormatted;
    }

    public static String removeQuotes(String text) {
        text = text.trim();
        if (text.startsWith("\"") && text.endsWith("\"")) {
            return text.substring(1, text.length() - 1);
        }
        return text;
    }

    private static String correctAuthorName(String name) {
        String[] nameParts = name.split("\\s+");
        StringBuilder correctedName = new StringBuilder();

        for (String part : nameParts) {
            String correctedPart = part.substring(0, 1).toUpperCase() + part.substring(1).toLowerCase();
            correctedName.append(correctedPart).append(" ");
        }

        return correctedName.toString().trim();
    }
}
