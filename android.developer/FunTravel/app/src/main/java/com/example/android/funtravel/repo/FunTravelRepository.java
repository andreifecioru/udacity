package com.example.android.funtravel.repo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Supplier;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.util.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.example.android.funtravel.api.FunTravelApi;
import com.example.android.funtravel.common.model.OfferType;
import com.example.android.funtravel.dao.OfferDao;
import com.example.android.funtravel.dao.ReviewDao;
import com.example.android.funtravel.model.OfferList;
import com.example.android.funtravel.model.ParcelableOffer;
import com.example.android.funtravel.model.ParcelableReview;
import com.example.android.funtravel.model.ReviewList;
import com.example.android.funtravel.ui.ListOffersActivity;
import com.example.android.funtravel.utils.ApiResponse;
import com.example.android.funtravel.utils.NetworkBoundResource;
import com.example.android.funtravel.utils.Resource;


/**
 * This class implements the "repository" abstraction for offers (and
 * related entities) as described in the AAC official docs. It is the
 * "single source of truth" for all offer-related info.
 *
 * See: https://developer.android.com/jetpack/docs/guide
 */
@Singleton
public class FunTravelRepository {
    private static final String LOG_TAG = FunTravelRepository.class.getSimpleName();

    private final FunTravelApi mFunTravelApi;

    // access DB via DAOs
    private final OfferDao mOfferDao;
    private final ReviewDao mReviewDao;

    // access the DB on a background thread
    private final Executor mExecutor;

    @Inject
    FunTravelRepository(FunTravelApi funTravelApi, Executor executor, Application application,
                        OfferDao offerDao, ReviewDao reviewDao) {
        mFunTravelApi = funTravelApi;

        mOfferDao = offerDao;
        mReviewDao = reviewDao;

        mExecutor = executor;
    }

    /**
     * Deletes all offers in local cache on bg. thread. Calls callback on completion.
     *
     * @param onComplete callback invoked when the operation is complete.
     * */
    @WorkerThread
    public void deleteAllOffers(final Runnable onComplete) {
        // we delete all offers on a background thread.
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mOfferDao.deleteAllOffers();
                onComplete.run();
            }
        });
    }

    /**
     * Deletes all reviews for a particular offer from the local cache on bg. thread.
     * Calls callback on completion.
     *
     * @param offerId the ID of the offer for which we are deleting the reviews
     * @param onComplete callback invoked when the operation is complete.
     * */
    @WorkerThread
    public void deleteAllReviewsForOffer(final long offerId, final Runnable onComplete) {
        // we delete all offers on a background thread.
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mReviewDao.deleteAllReviewsForOffer(offerId);
                onComplete.run();
            }
        });
    }

    /**
     * We fetch the offer list using the {@link NetworkBoundResource} implementation.
     *
     * This allows us to download the offers only once and then hit the DB cache
     * multiple times when needed. This also provides the off-line support we want.
     */
    public LiveData<Resource<Long>> fetchOffers(final int count) {
        return new NetworkBoundResource<Long, OfferList>() {

            @Override
            protected void saveCallResult(@NonNull OfferList data) {
                Log.d(LOG_TAG, "Saving offer data to the DB (" + data.getOffers().size() +" entries).");

                List<ParcelableOffer> validOffers = new ArrayList<>();

                // Validate the incoming offer data
                for (ParcelableOffer offer: data.getOffers()) {
                    // Offer type must be one of the known types (discard everything else).
                    if (offer.getTypeAsEnum() != null) {
                        validOffers.add(offer);
                    } else {
                        Log.w(LOG_TAG, "Invalid offer: " + offer);
                    }
                }

                // Perform DB insertions
                ParcelableOffer[] arrOffers = new ParcelableOffer[validOffers.size()];
                data.getOffers().toArray(arrOffers);
                mOfferDao.insertOffers(arrOffers);
            }

            @Override
            protected boolean shouldFetch(@Nullable Long offerCount) {
                // We fetch data when there is no data cached locally
                if (offerCount == null || offerCount == 0) {
                    Log.d(LOG_TAG, "No data stored locally. Fetch from network");
                    return true;
                }

                return false;
            }

            @NonNull
            @Override
            protected LiveData<Long> loadFromDb() {
                Log.d(LOG_TAG, "Loading data from DB.");

                return mOfferDao.getOfferCount();
            }

            @Override
            protected LiveData<ApiResponse<OfferList>> createCall() {
                return mFunTravelApi.getOfferList(count);
            }
        }.getAsLiveData();
    }

    /**
     * We fetch the review list for a particular offer using the {@link NetworkBoundResource}
     * implementation.
     *
     * This allows us to download the reviews only once and then hit the DB cache
     * multiple times when needed. This also provides the off-line support we want.
     */
    public LiveData<Resource<Long>> getReviewsForOffer(final long offerId) {
        return new NetworkBoundResource<Long, ReviewList>() {

            @Override
            protected void saveCallResult(@NonNull ReviewList data) {
                Log.d(LOG_TAG, "Saving review data for offer " + offerId + "to the DB (" + data.getReviews().size() +" entries).");

                // Enforce the foreign key restriction for all incoming reviews
                for (ParcelableReview review: data.getReviews()) {
                    review.setOfferId(offerId);
                }

                // Perform DB insertions
                ParcelableReview[] arrReviews = new ParcelableReview[data.getReviews().size()];
                data.getReviews().toArray(arrReviews);
                mReviewDao.insertReviews(arrReviews);
            }

            @Override
            protected boolean shouldFetch(@Nullable Long reviewCount) {
                // we fetch data when there is no data cached locally
                if ((reviewCount == null) || reviewCount == 0) {
                    Log.d(LOG_TAG, "No review data stored locally for offer " + offerId + ". Fetch from network");
                    return true;
                }

                return false;
            }

            @NonNull
            @Override
            protected LiveData<Long> loadFromDb() {
                Log.d(LOG_TAG, "Loading review data from DB for offer " + offerId + ".");

                return mReviewDao.getReviewCount(offerId);
            }

            @Override
            protected LiveData<ApiResponse<ReviewList>> createCall() {
                return mFunTravelApi.getReviewsForOffer(offerId);
            }
        }.getAsLiveData();
    }

    /**
     * Fetch the offer with a specified ID and wrap the result in LiveData.
     * Also fetches the reviews for the said offer.
     *
     * @param offerId the ID of the offer we are fetching.
     */
    public LiveData<ParcelableOffer> getOffer(final long offerId) {
        return Transformations.map(mOfferDao.getOffer(offerId), new Function<List<ParcelableOffer>, ParcelableOffer>() {
            @Override
            public ParcelableOffer apply(List<ParcelableOffer> offers) {
                return (offers == null || offers.isEmpty()) ? null : offers.get(0);
            }
        });
    }

    /**
     * Fetch the n-th offer (sorted by ID) and wrap the result in LiveData.
     * Acts pretty much like a DB cursor (i.e. provides random access to the
     * rows in the DB).
     *
     * This query is used by the various adapters that access offers based on
     * a "position".
     *
     * @param position the "position" of the article in the set of offers sorted
     *                 by their ID.
     */
    public LiveData<ParcelableOffer> getOfferAtPosition(final long position) {
        return Transformations.map(mOfferDao.getOfferAtPosition(position),
                new Function<List<ParcelableOffer>, ParcelableOffer>() {
                    @Override
                    public ParcelableOffer apply(List<ParcelableOffer> offers) {
                        if (offers != null && !offers.isEmpty()) {
                            return offers.get(0);
                        }
                        return null;
                    }
                });
    }

    /**
     * Retrieve best offers (by price).
     *
     * @param count max number of offers returned by the query.
     */
    public List<ParcelableOffer> getBestOffers(int count) {
        return mOfferDao.getBestOffers(count);
    }

    /**
     * Fetch the n-th review (sorted by ID) and wrap the result in LiveData.
     * Acts pretty much like a DB cursor (i.e. provides random access to the
     * rows in the DB).
     *
     * This query is used by the various adapters that access offers based on
     * a "position".
     *
     * @param position the "position" of the article in the set of offers sorted
     *                 by their ID.
     */
    public LiveData<ParcelableReview> getReviewAtPositionForOffer(final long position, final long offerId) {
        return Transformations.map(mReviewDao.getReviewAtPositionForOffer(position, offerId),
                new Function<List<ParcelableReview>, ParcelableReview>() {
                    @Override
                    public ParcelableReview apply(List<ParcelableReview> reviews) {
                        if (reviews != null && !reviews.isEmpty()) {
                            return reviews.get(0);
                        }
                        return null;
                    }
                });
    }
}
