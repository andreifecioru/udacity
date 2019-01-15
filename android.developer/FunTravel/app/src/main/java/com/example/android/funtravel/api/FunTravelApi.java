package com.example.android.funtravel.api;

import androidx.lifecycle.LiveData;

import retrofit2.http.GET;
import retrofit2.http.Query;

import com.example.android.funtravel.model.OfferList;
import com.example.android.funtravel.model.ReviewList;
import com.example.android.funtravel.utils.ApiResponse;


/**
 * Definition of the FunTravel web API as required by the Retrofit lib.
 */
public interface FunTravelApi {
    /** Requests targeting the "offers" endpoint
     *
     * @param count query param for specifying the max number of offers returned by the server
     *              NOTE: the server may return less than this number if there are not enough
     *              offers on the server side to satisfy the request.
     * @param flags a set bit-flags specifying what type of offers to be included in the result.
     * @return The offer list.
     */
    @GET("offers/v1/list")
    LiveData<ApiResponse<OfferList>> getOfferList(
        @Query("count") int count,
        @Query("flags") int flags
    );

    /** Requests targeting the "reviews" endpoint
     *
     * @param offerId The offer ID for which we are retrieving the review list.
     * @return The review list for the specified offer.
     */
    @GET("reviews/v1/list")
    LiveData<ApiResponse<ReviewList>> getReviewsForOffer(@Query("offer_id") long offerId);
}
