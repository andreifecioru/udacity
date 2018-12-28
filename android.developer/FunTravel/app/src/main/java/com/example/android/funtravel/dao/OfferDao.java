package com.example.android.funtravel.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.android.funtravel.model.ParcelableOffer;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;


/**
 * DAO definition for the basic offer information.
 * Services provided:
 *  - retrieve all the offers from DB
 *  - retrieve a particular offer by ID
 *  - insert a list of offers into DB
 *  - clear all the offers from DB (used during testing and
 *      will also clear the reviews due to the way
 *      foreign keys are set up)
 */
@Dao
public interface OfferDao {
    @Query("SELECT * FROM offers WHERE id = :offerId")
    LiveData<List<ParcelableOffer>> getOffer(long offerId);

    @Query("SELECT * FROM offers ORDER BY id ASC LIMIT 1 OFFSET :position")
    LiveData<List<ParcelableOffer>> getOfferAtPosition(long position);

    @Query("SELECT * FROM offers ORDER BY price ASC LIMIT :count")
    List<ParcelableOffer> getBestOffers(int count);

    @Query("SELECT count(*) FROM offers")
    LiveData<Long> getOfferCount();

    @Insert(onConflict = REPLACE)
    void insertOffers(ParcelableOffer... recipes);

    @Query("DELETE FROM offers")
    void deleteAllOffers();
}
