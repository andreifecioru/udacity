package com.example.android.funtravel.api;

import java.util.List;

import android.arch.lifecycle.LiveData;

import retrofit2.http.GET;
import retrofit2.http.Query;

import com.example.android.funtravel.model.OfferList;
import com.example.android.funtravel.model.ReviewList;
import com.example.android.funtravel.utils.ApiResponse;


/**
 * Definition of the FunTravel web API as required by the Retrofit lib.
 */
public interface FunTravelApi {
    @GET("offers/v1/list")
    LiveData<ApiResponse<OfferList>> getOfferList(@Query("count") int count);

    @GET("reviews/v1/list")
    LiveData<ApiResponse<ReviewList>> getReviewsForOffer(@Query("offer_id") long offerId);
}
