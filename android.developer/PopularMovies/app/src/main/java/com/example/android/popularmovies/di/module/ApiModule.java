package com.example.android.popularmovies.di.module;

import com.example.android.popularmovies.services.moviedb.MovieDbApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * DI module providing access to the Movie DB API endpoint.
 */
@Module
public class ApiModule {
    @Provides
    @Singleton
    public static MovieDbApi providesMovieDbApi(Retrofit retrofit) {
        return retrofit.create(MovieDbApi.class);
    }
}
