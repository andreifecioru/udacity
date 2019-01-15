package com.example.android.funtravel.common.fixtures;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Utils {
    private static final int MAX_RATING = 5;
    private static final int MIN_PRICE = 200;
    private static final int MAX_PRICE = 500;

    public static <T> T randomChoiceOf(List<T> input) {
        if (input == null || input.isEmpty()) return null;

        return input.get(ThreadLocalRandom.current().nextInt(0, input.size()));
    }

    public static float getRandomReviewRating() { return ThreadLocalRandom.current().nextInt(0, MAX_RATING) + 1; }

    public static float getRandomPrice() { return ThreadLocalRandom.current().nextInt(MIN_PRICE, MAX_PRICE); }

    private Utils() {
        throw new IllegalStateException("Utility class");
    }
}
