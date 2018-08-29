package com.example.android.popularmovies.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * A collection of trailers as provided by the Movie DB API.
 *
 * Used only for parsing the incoming JSON response.
 */
public class TrailerList {
    @SerializedName("results")
    @Expose
    private List<Trailer> mTrailers;

    public List<Trailer> getTrailers() { return mTrailers; }
}
