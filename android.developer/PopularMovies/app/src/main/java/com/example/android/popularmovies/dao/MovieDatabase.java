package com.example.android.popularmovies.dao;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.models.Review;
import com.example.android.popularmovies.models.Trailer;

/**
 * The "movies" DB contains three tables:
 *  - the "movies" table contains basic movie info (like title, rating, release date, etc)
 *  - the "trailers" table (with FK pointing to the "movies" table)
 *  - the "reviews" table (with FK pointing to the "movies" table)
 */
@Database(
        entities = {Movie.class, Trailer.class, Review.class},
        version = 1,
        exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {
    public abstract MovieDao getMovieDao();
    public abstract TrailerDao getTrailerDao();
    public abstract ReviewDao getReviewDao();
}
