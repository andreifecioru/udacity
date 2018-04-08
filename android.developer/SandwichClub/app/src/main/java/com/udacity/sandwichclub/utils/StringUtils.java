package com.udacity.sandwichclub.utils;

import java.util.List;

public class StringUtils {
    /**
     * Joins a {@link List<String>} into a single {@link String} by joining elements with a
     * specified delimiter.
     *
     * NOTE: Apparently, {@link String#join} is not available until API v26. We'll just roll our own.
     *
     * @param delimiter String to be used as a delimiter.
     * @param strings A list of strings to be joined.
     *
     * @return The joined string.
     */
    public static String joinStrings(String delimiter, List<String> strings) {
        // Fast exit
        if (strings.isEmpty()) return "";

        int i;
        StringBuilder sb = new StringBuilder();

        // Add elements up to (excluding) the last one
        for (i = 0; i < strings.size() - 1; i++) {
            sb.append(strings.get(i)).append(delimiter);
        }

        // Add the last element
        sb.append(strings.get(i));

        return sb.toString();
    }
}
