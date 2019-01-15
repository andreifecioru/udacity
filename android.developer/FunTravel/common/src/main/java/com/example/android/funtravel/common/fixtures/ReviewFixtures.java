package com.example.android.funtravel.common.fixtures;

import java.util.ArrayList;
import java.util.List;

import com.example.android.funtravel.common.model.Review;
import com.example.android.funtravel.common.model.Reviews;

import static com.example.android.funtravel.common.fixtures.Utils.*;


public class ReviewFixtures {
    private static final List<Review> mReviews = new ArrayList<>();

    static {
        mReviews.add(Reviews.create(
            "Susan E.",
            "Where do I begin!?! This is the third time we have used a travel agent we " +
                    "found through Zicasso...Turkey, Spain, and now Italy.  Each trip has been exactly" +
                    " as we hoped and that is directly due to the travel agents that Zicasso recommended.",
            getRandomReviewRating()
        ));

        mReviews.add(Reviews.create(
            "Joy S.",
            "The trip was great! All of the tour guides and drivers that our travel company " +
                    "arranged were great and knowledgeable and punctual. The one problem we had was there " +
                    "seemed to be a communication problem at times explaining what we wanted.",
            getRandomReviewRating()
        ));

        mReviews.add(Reviews.create(
            "Jeff M.",
            "This trip was easily the trip of a lifetime for our family of four, and it will" +
                    " be quite some time before we stop talking about it!!  We have a done a lot of" +
                    " travel with our children alongside us, but the adventures that were found around every " +
                    "corner on this African safari were beyond our expectations.",
            getRandomReviewRating()
        ));

        mReviews.add(Reviews.create(
            "Stuart F.",
            "Few things command more respect than hard work, integrity, dedication and the ability " +
                    "to follow through. These are among the many attributes we experienced from start to " +
                    "finish when organizing our trip to India with this travel agent.",
            getRandomReviewRating()
        ));

        mReviews.add(Reviews.create(
            "Lexi A.",
            "My wife, daughter and myself cannot say enough about how wonderful a trip we had, " +
                    "how great this travel company - in particular our travel agent was from the beginning " +
                    "of our interest in the tour, helping us develop a fascinating and thorough schedule and, " +
                    "what was really impressive.",
            getRandomReviewRating()
        ));

        mReviews.add(Reviews.create(
            "Chelsea P.",
            "My husband and I could not have been happier with our experience working with this travel " +
                    "agency. Our agent assisted us in designing an absolutely spectacular itinerary for our " +
                    "two-week trip.",
            getRandomReviewRating()
        ));

        mReviews.add(Reviews.create(
            "Sharon W.",
            "We're sorry that we can only give this travel agency 5 stars -- we'd have preferred 10 " +
                    "stars or more!  What an awesome trip!  It helps that New Zealand is probably the most " +
                    "beautiful place on the planet, but more importantly, our travel agency arranged our " +
                    "access to the country’s wonders so seamlessly.",
            getRandomReviewRating()
        ));

        mReviews.add(Reviews.create(
            "Elizabeth R.",
            "We cannot recommend this travel company enough! My husband and I had been wanting to go " +
                    "to Germany/Austria for quite some time but had not done so due to lack of time to plan a " +
                    "worthwhile trip. Booking with this company was exactly what we needed.",
            getRandomReviewRating()
        ));

        mReviews.add(Reviews.create(
            "Barbara L.",
            "We just returned from an amazing week in Iceland.  Our travel agent planned an amazing " +
                    "week for us and wasn't fazed when we threw in the wrinkle of getting married on New Year's Eve " +
                    "two weeks before our trip.",
            getRandomReviewRating()
        ));

        mReviews.add(Reviews.create(
            "Gary C.",
            "I've traveled A LOT in my life, but this was the best trip I've ever taken and it had " +
                    "everything to do with using this travel company and this agent to plan our Honeymoon. " +
                    "Everything from the initial trip planning process to the actual trip itself was seamless " +
                    "and made for an unforgettable experience.",
            getRandomReviewRating()
        ));
    }

    public static List<Review> getReviews(int count) {
        List<Review> result = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            result.add(randomChoiceOf(mReviews));
        }

        return result;
    }

    private ReviewFixtures() {
        throw new IllegalStateException("Utility class");
    }
}
