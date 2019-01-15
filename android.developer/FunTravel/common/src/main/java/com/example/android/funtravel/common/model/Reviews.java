package com.example.android.funtravel.common.model;


public class Reviews {
    public static Review create(String author, String content, float rating) {
        Review review = new Review();
        review.setId(0L);
        review.setOfferId(0L);
        review.setAuthor(author);
        review.setContent(content);
        review.setRating(rating);

        return review;
    }

    private Reviews() {
        throw new IllegalStateException("Utility class");
    }
}
