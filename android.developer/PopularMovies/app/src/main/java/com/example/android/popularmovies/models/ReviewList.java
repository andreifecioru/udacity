package com.example.android.popularmovies.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * A collection of reviews as provided by the Movie DB API.
 *
 * Used only for parsing the incoming JSON response.
 */
public class ReviewList {
    @SerializedName("results")
    @Expose
    private List<Review> mReviews;

    public List<Review> getReviews() { return mReviews; }
}
