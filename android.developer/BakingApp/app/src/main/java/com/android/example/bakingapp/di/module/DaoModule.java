package com.android.example.bakingapp.di.module;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.android.example.bakingapp.dao.*;

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
    private static final String DATABASE_NAME = "baking-app.db";

    @Provides
    @Singleton
    public RecipeDatabase provideDatabase(Application application) {
        return Room.databaseBuilder(application, RecipeDatabase.class, DATABASE_NAME)
                .build();
    }

    @Provides
    @Singleton
    public RecipeDao provideRecipeDao(RecipeDatabase db) {
        return db.getRecipeDao();
    }

    @Provides
    @Singleton
    public IngredientDao provideIngredientDao(RecipeDatabase db) {
        return db.getIngredientDao();
    }

    @Provides
    @Singleton
    public StepDao provideStepDao(RecipeDatabase db) {
        return db.getStepDao();
    }

    @Provides
    @Singleton
    public Executor provideSingleThreadExecutor() {
        return Executors.newSingleThreadExecutor();
    }
}
