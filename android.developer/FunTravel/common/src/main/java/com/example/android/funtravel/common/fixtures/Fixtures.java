package com.example.android.funtravel.common.fixtures;

import com.example.android.funtravel.common.model.OfferType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Fixtures {
    private static final List<String> offerTitles = new ArrayList<>();
    private static final List<String> offerDescriptions = new ArrayList<>();
    private static final List<String> offerTypes = new ArrayList<>();
    private static final List<String> offerPhotoUrls = new ArrayList<>();
    private static final List<String> offerVideoUrls = new ArrayList<>();
    private static final List<String> reviewAuthors = new ArrayList<>();
    private static final List<String> reviewContents = new ArrayList<>();

    private static final int MAX_RATING = 5;
    private static final int MIN_PRICE = 200;
    private static final int MAX_PRICE = 500;


    static {
        offerTitles.add("Lorem ipsum dolor sit amet");
        offerDescriptions.add(
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor " +
                "incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud");

        offerPhotoUrls.add("http://s3.amazonaws.com/afecioru-udacity/funtravel/data/images/photo-001.jpg");
        offerVideoUrls.add("https://s3.amazonaws.com/afecioru-udacity/funtravel/data/videos/video-001.mp4");

        reviewAuthors.add("John Doe");
        reviewContents.add(
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor " +
                "incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud" +
                " exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute " +
                "irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat " +
                "nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui " +
                "officia deserunt mollit anim id est laborum.");

        for (OfferType offerType: OfferType.values()) {
            offerTypes.add(offerType.asString());
        }
    }

    public static String getRandomOfferTitle() { return randomChoiceOf(offerTitles); }
    public static String getRandomOfferDescription() { return randomChoiceOf(offerDescriptions); }
    public static String getRandomOfferType() { return randomChoiceOf(offerTypes); }
    public static float getRandomPrice() { return ThreadLocalRandom.current().nextInt(MIN_PRICE, MAX_PRICE); }
    public static String getRandomOfferPhotoUrl() { return randomChoiceOf(offerPhotoUrls); }
    public static String getRandomOfferVideoUrl() { return randomChoiceOf(offerVideoUrls); }
    public static String getRandomReviewAuthor() { return randomChoiceOf(reviewAuthors); }
    public static String getRandomReviewContent() { return randomChoiceOf(reviewContents); }
    public static float getRandomReviewRating() { return ThreadLocalRandom.current().nextInt(0, MAX_RATING) + 1; }

    private static <T> T randomChoiceOf(List<T> input) {
        if (input == null || input.isEmpty()) return null;

        return input.get(ThreadLocalRandom.current().nextInt(0, input.size()));
    }

    private Fixtures() {
        throw new IllegalStateException("Utility class");
    }
}
