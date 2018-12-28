package com.example.android.funtravel.backend;

import com.example.android.funtravel.common.model.Offer;
import com.example.android.funtravel.common.model.Offers;
import com.example.android.funtravel.common.model.Review;
import com.example.android.funtravel.common.model.Reviews;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ThreadLocalRandom;

class InMemoryDB {
    private static final int MAX_OFFER_COUNT = 100;
    private static final int MAX_REVIEWS_PER_OFFER = 20;

    private static final Comparator<Offer> OFFER_COMPARATOR = new Comparator<Offer>() {
        @Override
        public int compare(Offer offer, Offer other) {
            return (int)(offer.getId() - other.getId());
        }
    };

    private static final SortedSet<Offer> mOffers = new TreeSet<>(OFFER_COMPARATOR);

    private static final Map<Long, List<Review>> mReviews = new HashMap<>();

    static {
        mOffers.addAll(Offers.generateOffers(MAX_OFFER_COUNT));
        for (Offer offer: mOffers) {
            int reviewCount = ThreadLocalRandom.current().nextInt(0, MAX_REVIEWS_PER_OFFER) + 1;
            List<Review> reviews = Reviews.generateReviews(offer.getId(), reviewCount);

            // Compute the avg rating for the offer
            if (reviews.size() > 0) {
                float sum = 0;
                for (Review review : reviews) {
                    sum += review.getRating();
                }
                offer.setAvgRating(sum / reviews.size());
            }

            mReviews.put(offer.getId(), reviews);
        }
    }

    static List<Offer> listOffers(int count) {
        List<Offer> result = new ArrayList<>();

        int idx = 0;
        for (Offer offer: mOffers) {
            result.add(offer);
            idx ++;
            if (idx == count) break;
        }

        return result;
    }

    static List<Review> listReviewsForOffer(long offerId) {
        List<Review> reviews = mReviews.get(offerId);
        return (reviews == null) ? Collections.<Review>emptyList() : reviews;
    }

    private InMemoryDB() {
        throw new IllegalStateException("Utility class");
    }
}
