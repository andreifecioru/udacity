package com.example.android.funtravel.backend;


import com.example.android.funtravel.common.model.Review;
import com.example.android.funtravel.common.model.Reviews;

import java.util.List;

/** The object model for the reviews data we are sending through endpoints */
public class ReviewBean {
    private final long mOfferId;

    ReviewBean(long offerId) {
        mOfferId = offerId;
    }

    public List<Review> getReviews() {
        return InMemoryDB.listReviewsForOffer(mOfferId);
    }
}