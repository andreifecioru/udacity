package com.example.android.popularmovies.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.android.popularmovies.models.Trailer;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * DAO definition for the movie-trailer information.
 * Services provided:
 *  - retrieve the trailers for a particular movie
 *  - insert a list of trailers into DB
 */
@Dao
public interface TrailerDao {
    @Query("SELECT * FROM trailers WHERE movie_id = :movieId")
    LiveData<List<Trailer>> loadTrailersForMovie(long movieId);

    @Insert(onConflict = REPLACE)
    void insertTrailers(Trailer... trailers);
}
