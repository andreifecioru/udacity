package com.example.android.funtravel.backend;

import com.example.android.funtravel.common.fixtures.OfferFixtures;
import com.example.android.funtravel.common.fixtures.ReviewFixtures;
import com.example.android.funtravel.common.model.Offer;
import com.example.android.funtravel.common.model.OfferType;
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
    private static final int MAX_OFFER_COUNT_PER_TYPE = 10;
    private static final int MAX_REVIEWS_PER_OFFER = 20;

    private static final List<Offer> mOffers = new ArrayList<>();

    // Map (offer_id -> review)
    private static final Map<Long, List<Review>> mReviews = new HashMap<>();

    synchronized private static void regenerateOffers() {
        // Reset the DB - start from a clean slate
        mOffers.clear();

        // Add offers from all the offer types
        mOffers.addAll(OfferFixtures.getOffers(OfferType.CITYSCAPE, MAX_OFFER_COUNT_PER_TYPE));
        mOffers.addAll(OfferFixtures.getOffers(OfferType.RESORT, MAX_OFFER_COUNT_PER_TYPE));
        mOffers.addAll(OfferFixtures.getOffers(OfferType.TREKKING, MAX_OFFER_COUNT_PER_TYPE));

        long offerId = 1L;
        long reviewId = 1L;

        for (Offer offer: mOffers) {
            // Set the offer IDs
            offer.setId(offerId);

            int reviewCount = ThreadLocalRandom.current().nextInt(0, MAX_REVIEWS_PER_OFFER) + 1;
            List<Review> reviews = ReviewFixtures.getReviews(reviewCount);

            // Compute the avg rating for the offer and set the review IDs
            if (reviews.size() > 0) {
                float sum = 0;

                for (Review review : reviews) {
                    review.setId(reviewId);
                    reviewId ++;
                    sum += review.getRating();
                }

                offer.setAvgRating(sum / reviews.size());
            }

            mReviews.put(offer.getId(), reviews);

            offerId ++;
        }
    }

    static List<Offer> listOffers(int count, int flags) {
        // We regenerate the list of offers with each offer-list request.
        // This ensures that the users sees a new set of offers whenever
        // it refreshes the UI in the ListOfferActivity screen.
        regenerateOffers();

        List<Offer> result = new ArrayList<>();

        int idx = 0;
        for (Offer offer: mOffers) {
            // we only add the offer to the response if the flags
            // on the incoming request match the offer type
            if (offer.getTypeAsEnum().checkFlags(flags)) {
                result.add(offer);
                idx ++;
            }

            if (idx == count) {
                // we have enough offers ->  break out
                break;
            }
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
