package com.android.example.bakingapp.di.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import retrofit2.Retrofit;

import com.android.example.bakingapp.api.RecipeApi;


/**
 * DI module providing access to the Recipe API endpoint.
 */
@Module
public class ApiModule {
    @Provides
    @Singleton
    public static RecipeApi providesMovieDbApi(Retrofit retrofit) {
        return retrofit.create(RecipeApi.class);
    }
}
