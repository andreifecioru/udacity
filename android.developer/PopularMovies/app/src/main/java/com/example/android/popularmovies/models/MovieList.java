package com.example.android.popularmovies.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * A collection of movies as provided by the Movie DB API.
 *
 * Used only for parsing the incoming JSON response.
 */
public class MovieList {
    @SerializedName("results")
    @Expose
    private List<? extends Movie> mMovies;

    public List<? extends Movie> getMovies() { return mMovies; }
}
