package com.example.android.popularmovies.models;

import java.io.Serializable;


/**
 * Implements the "movie" abstraction.
 * For our purposes, a movie is defined by a title, synopsis,
 * user rating, etc.
 */
public class Movie implements Serializable {
    private final Long mId;
    private final String mTitle;
    private final String mPosterUrl;
    private final String mBackdropUrl;
    private final String mSynopsis;
    private final double mUserRating;
    private final String mReleaseDate;

    /**
     * Creates a {@link Movie} instance (constructor).
     *
     * @param id The id of the movie.
     * @param title The title of the movie.
     * @param posterUrl The full URL pointing to the movie poster image.
     * @param backdropUrl The full URL pointing to the movie backdrop image.
     * @param synopsis Short description of the movie's plot.
     * @param userRating A floating point number (0-10) representing the movie's average user rating.
     * @param releaseDate The movie's release date (format yyyy-mm-dd)
     */
    Movie(Long id, String title, String posterUrl,
          String backdropUrl, String synopsis,
          double userRating, String releaseDate) {
        mId = id;
        mTitle = title;
        mPosterUrl = posterUrl;
        mBackdropUrl = backdropUrl;
        mSynopsis = synopsis;
        mUserRating = userRating;
        mReleaseDate = releaseDate;
    }

    /**
     * (Getter) Returns the id of the movie.
     *
     * @return The movie ID.
     */
    public Long getId() { return mId; }

    /**
     * (Getter) Returns the title of the movie.
     *
     * @return The movie title.
     */
    public String getTitle() { return mTitle; }

    /**
     * (Getter) Returns the movie's poster image full URL.
     *
     * @return The full URL for the movie poster image.
     */
    public String getPosterUrl() { return mPosterUrl; }

    /**
     * (Getter) Returns the movie's backdrop image full URL.
     *
     * @return The full URL for the movie backdrop image.
     */
    public String getBackdropUrl() { return mBackdropUrl; }

    /**
     * (Getter) Returns the movie's synopsis.
     *
     * @return The movie synopsis.
     */
    public String getSynopsis() { return mSynopsis; }

    /**
     * (Getter) Returns the movie's average user rating.
     *
     * @return The movie average user rating.
     */
    public double getUserRating() { return mUserRating; }

    /**
     * (Getter) Returns the movie's release date (format yyyy-mm-dd)
     *
     * @return The movie release date.
     */
    public String getReleaseDate() { return mReleaseDate; }

    /**
     * Provides a string representation of a movie (JSON format).
     *
     * Used for debugging/logging purposes.
     *
     * @return A string representation of the movie.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("movie: {\n");
        sb.append("  id: ").append(mId).append(", \n");
        sb.append("  title: ").append(mTitle).append(", \n");
        sb.append("  poster: ").append(mPosterUrl).append(", \n");
        sb.append("  backdrop: ").append(mBackdropUrl).append(", \n");
        sb.append("  url: ").append(mBackdropUrl).append(", \n");
        sb.append("  rating: ").append(mUserRating).append(", \n");
        sb.append("  release date: ").append(mReleaseDate).append("}");

        return sb.toString();
    }
}
