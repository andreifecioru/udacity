package com.example.android.popularmovies.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.android.popularmovies.models.Review;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * DAO definition for the movie-review information.
 * Services provided:
 *  - retrieve the reviews for a particular movie
 *  - insert a list of reviews into DB
 */
@Dao
public interface ReviewDao {
    @Query("SELECT * FROM reviews WHERE movie_id = :movieId")
    LiveData<List<Review>> loadReviewsForMovie(long movieId);

    @Insert(onConflict = REPLACE)
    void insertReviews(Review... reviews);
}
