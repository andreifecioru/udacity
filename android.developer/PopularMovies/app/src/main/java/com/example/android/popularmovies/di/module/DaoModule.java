package com.example.android.popularmovies.di.module;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.example.android.popularmovies.dao.MovieDao;
import com.example.android.popularmovies.dao.MovieDatabase;
import com.example.android.popularmovies.dao.ReviewDao;
import com.example.android.popularmovies.dao.TrailerDao;
import com.example.android.popularmovies.models.Review;
import com.example.android.popularmovies.models.Trailer;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * DI module providing access to the DB via various DAO objects.
 */
@Module
public class DaoModule {
    private static final String DATABASE_NAME = "popular-movies";

    @Provides
    @Singleton
    public MovieDatabase provideDatabase(Application application) {
        return Room.databaseBuilder(application, MovieDatabase.class, DATABASE_NAME)
                .build();
    }

    @Provides
    @Singleton
    public MovieDao provideMovieDao(MovieDatabase db) {
        return db.getMovieDao();
    }

    @Provides
    @Singleton
    public TrailerDao provideTrailerDao(MovieDatabase db) {
        return db.getTrailerDao();
    }

    @Provides
    @Singleton
    public ReviewDao provideReviewDao(MovieDatabase db) {
        return db.getReviewDao();
    }

    @Provides
    @Singleton
    public Executor provideSingleThreadExecutor() {
        return Executors.newSingleThreadExecutor();
    }
}
