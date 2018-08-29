package com.example.android.popularmovies.services.moviedb;

import android.arch.lifecycle.LiveData;

import com.example.android.popularmovies.di.module.NetModule;
import com.example.android.popularmovies.models.MovieList;
import com.example.android.popularmovies.models.ReviewList;
import com.example.android.popularmovies.models.TrailerList;
import com.example.android.popularmovies.utils.ApiResponse;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Definition of the Movie DB web API as required by the Retrofit lib.
 */
public interface MovieDbApi {
    @GET("{sort_by}")
    LiveData<ApiResponse<MovieList>> getMovieList(@Path("sort_by") String sortBy, @QueryMap Map<String, String> options);

    @GET("{id}/videos")
    LiveData<ApiResponse<TrailerList>> getTrailerList(@Path("id") long movieId, @QueryMap Map<String, String> options);

    @GET("{id}/reviews")
    LiveData<ApiResponse<ReviewList>> getReviewList(@Path("id") long movieId, @QueryMap Map<String, String> options);
}
