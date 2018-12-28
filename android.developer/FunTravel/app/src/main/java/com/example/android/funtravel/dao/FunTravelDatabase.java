package com.example.android.funtravel.dao;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.android.funtravel.model.ParcelableOffer;
import com.example.android.funtravel.model.ParcelableReview;


/**
 * The "fun-travel" DB contains three tables:
 *  - the "offers" table contains basic offer info (like name, description, photo URL, etc).
 *  - the "reviews" table (with FK pointing to the "offers" table)
 */
@Database(
        entities = {ParcelableOffer.class, ParcelableReview.class},
        version = 1,
        exportSchema = false)
public abstract class FunTravelDatabase extends RoomDatabase {
    public abstract OfferDao getOfferDao();
    public abstract ReviewDao getReviewDao();
}

