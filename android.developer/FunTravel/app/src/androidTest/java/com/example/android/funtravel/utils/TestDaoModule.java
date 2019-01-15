package com.example.android.funtravel.utils;

import android.app.Application;
import androidx.room.Room;

import com.example.android.funtravel.dao.FunTravelDatabase;
import com.example.android.funtravel.di.module.DaoModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 *  Custom implementation of out {@link DaoModule} used solely for testing purposes.
 *  It's DATABASE_NAME is changed so that the app's DB is not affected by executing the
 *  UI tests (the UI tests will reset the DB contents on each run).
 */
@Module
public class TestDaoModule extends DaoModule {
    private static final String DATABASE_NAME = "testing-funtravel.db";

    @Provides
    @Singleton
    public FunTravelDatabase provideDatabase(Application application) {
        return Room.databaseBuilder(application, FunTravelDatabase.class, DATABASE_NAME)
                .build();
    }
}
