package com.example.android.funtravel.common.model;

import com.example.android.funtravel.common.fixtures.Fixtures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class Reviews {
    private static final AtomicLong mReviewId = new AtomicLong(0L);

    public static List<Review> generateReviews(long offerId, int count) {
        if (count <= 0) return Collections.emptyList();

        List<Review> result = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            result.add(generateReview(offerId));
        }

        return result;
    }

    private static Review generateReview(long offerId) {
        Review review = new Review();
        review.setId(mReviewId.incrementAndGet());
        review.setOfferId(offerId);
        review.setAuthor(Fixtures.getRandomReviewAuthor());
        review.setContent(Fixtures.getRandomReviewContent());
        review.setRating(Fixtures.getRandomReviewRating());

        return review;
    }

    private Reviews() {
        throw new IllegalStateException("Utility class");
    }
}
