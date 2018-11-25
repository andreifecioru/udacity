package com.android.example.bakingapp.utils;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.android.example.bakingapp.dao.RecipeDatabase;
import com.android.example.bakingapp.di.module.DaoModule;

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
    private static final String DATABASE_NAME = "testing-baking-app.db";

    @Provides
    @Singleton
    public RecipeDatabase provideDatabase(Application application) {
        return Room.databaseBuilder(application, RecipeDatabase.class, DATABASE_NAME)
                .build();
    }
}
