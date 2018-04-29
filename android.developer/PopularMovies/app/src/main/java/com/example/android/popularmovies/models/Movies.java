package com.example.android.popularmovies.models;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Utility class for the {@link Movie} class. Provides a set of factory functions
 * for creating {@link Movie} instances.
 */
public final class Movies {
    private static final String MOVIE_DB_API_KEY = "210c9ee381717d30c2aaa0f06b795b5a";

    public static List<Movie> EMPTY = new ArrayList<>();

    /**
     * A set of {@link Uri.Builder} instances used throughout the app
     * to talk to the movie DB service.
     */
    public static final Uri.Builder MOVIE_DB_URI_BUILDER_API = new Uri.Builder()
            .scheme("https")
            .authority("api.themoviedb.org")
            .appendPath("3").appendPath("movie")
            .appendQueryParameter("api_key", MOVIE_DB_API_KEY);

    private static final Uri.Builder MOVIE_DB_URI_BUILDER_IMAGES = new Uri.Builder()
            .scheme("https")
            .authority("image.tmdb.org")
            .appendPath("t").appendPath("p");

    private static final Uri.Builder MOVIE_DB_URI_BUILDER_POSTER = to(MOVIE_DB_URI_BUILDER_IMAGES)
            .appendPath("w342");

    private static final Uri.Builder MOVIE_DB_URI_BUILDER_BACKDROP = to(MOVIE_DB_URI_BUILDER_IMAGES)
            .appendPath("w342");

    private static final String LOG_TAG = Movies.class.getSimpleName();

    /**
     * This is a helper method for "cloning" instances of {@link Uri.Builder}
     * so we can repeatedly build upon it.
     *
     * @param builder {@link Uri.Builder} to be cloned.
     *
     * @return Returns a "clone" of the input {@link Uri.Builder} instance.
     */
    public static Uri.Builder to(Uri.Builder builder) {
        return builder.build().buildUpon();
    }

    /**
     * Creates a {@link List<Movie>} from a JSON-formatted string
     * (as returned by the movie DB service).
     *
     * @param data The JSON-formatted string as provided by the movie DB service.
     *
     * @return A {@link List<Movie>} with the movies extracted from the input data.
     */
    public static List<Movie> fromJSON(String data) {
        // Start with an empty list
        List<Movie> movies = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(data);
            JSONArray results = root.getJSONArray("results");

            Log.d(LOG_TAG, "Received " + results.length() + " movies.");

            // Sift through each movie
            for(int i = 0; i < results.length(); i++) {
                JSONObject movie = results.getJSONObject(i);
                Long id = movie.getLong("id");
                String title = movie.getString("title");
                String posterPath = movie.getString("poster_path").replace("/", "");
                String backdropPath = movie.getString("backdrop_path").replace("/", "");
                String synopsis = movie.getString("overview");
                double userRating = movie.getDouble("vote_average");
                String releaseDate = movie.getString("release_date");

                // Compose the full URL for image resources
                String posterUrl = to(MOVIE_DB_URI_BUILDER_POSTER).appendPath(posterPath).build().toString();
                String backdropUrl = to(MOVIE_DB_URI_BUILDER_BACKDROP).appendPath(backdropPath).build().toString();

                movies.add(new Movie(id, title, posterUrl, backdropUrl, synopsis, userRating, releaseDate));
            }

        } catch (JSONException e) {
            Log.w(LOG_TAG, "Failed to parse movie list: " + e.getMessage());
        }

        return movies;
    }
}
