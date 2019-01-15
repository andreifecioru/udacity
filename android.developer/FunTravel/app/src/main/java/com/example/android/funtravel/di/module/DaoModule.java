package com.example.android.funtravel.di.module;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import android.app.Application;
import androidx.room.Room;

import com.example.android.funtravel.dao.FunTravelDatabase;
import com.example.android.funtravel.dao.OfferDao;
import com.example.android.funtravel.dao.ReviewDao;


/**
 * DI module providing access to the DB via various DAO objects.
 */
@Module
public class DaoModule {
    private static final String DATABASE_NAME = "fun-travel.db";

    @Provides
    @Singleton
    public FunTravelDatabase provideDatabase(Application application) {
        return Room.databaseBuilder(application, FunTravelDatabase.class, DATABASE_NAME)
                .build();
    }

    @Provides
    @Singleton
    public OfferDao provideOfferDao(FunTravelDatabase db) {
        return db.getOfferDao();
    }

    @Provides
    @Singleton
    public ReviewDao provideReviewDao(FunTravelDatabase db) {
        return db.getReviewDao();
    }

    @Provides
    @Singleton
    public Executor provideSingleThreadExecutor() {
        return Executors.newSingleThreadExecutor();
    }
}
