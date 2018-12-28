package com.example.android.funtravel.dao;

import java.util.List;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.android.funtravel.model.ParcelableReview;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;


/**
 * DAO definition for the offer-review information.
 *
 * Services provided:
 *  - retrieve the offer-reviews for a particular offer from DB
 *  - insert a list of offer-review into DB
 */
@Dao
public interface ReviewDao {
    @Query("SELECT count(*) FROM reviews WHERE offer_id = :offerId")
    LiveData<Long> getReviewCount(long offerId);

    @Query("SELECT * FROM reviews WHERE offer_id = :offerId ORDER BY id ASC LIMIT 1 OFFSET :position")
    LiveData<List<ParcelableReview>> getReviewAtPositionForOffer(long position, long offerId);

    @Insert(onConflict = REPLACE)
    void insertReviews(ParcelableReview... reviews);

    @Query("DELETE FROM reviews WHERE offer_id = :offerId")
    void deleteAllReviewsForOffer(long offerId);
}
