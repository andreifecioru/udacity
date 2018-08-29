package com.example.android.popularmovies.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.android.popularmovies.models.Movie;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * DAO definition for the basic movie information.
 * Services provided:
 *  - retrieve the movies from DB orders either by rating or popularity
 *  - retrieve the movies from DB that were marked as favourites
 *  - update existing movie into DB
 *  - insert a list of movies into DB
 *  - clear all the movies from DB
 *    (will also clear the reviews and trailers due to the way
 *     foreign keys are set up)
 */
@Dao
public interface MovieDao {
    @Query("SELECT * FROM movies WHERE sort_by = 'top_rated' ORDER BY user_rating DESC")
    LiveData<List<Movie>> loadTopRatedMovies();

    @Query("SELECT * FROM movies WHERE sort_by = 'popular' ORDER BY popularity DESC")
    LiveData<List<Movie>> loadPopularMovies();

    @Query("SELECT * FROM movies WHERE is_favourite = 1")
    LiveData<List<Movie>> loadFavouriteMovies();

    @Update(onConflict = REPLACE)
    void updateMovie(Movie movie);

    @Insert(onConflict = REPLACE)
    void insertMovies(Movie... movies);

    @Query("DELETE FROM movies WHERE sort_by = :sortBy")
    void deleteAllMovies(String sortBy);
}
