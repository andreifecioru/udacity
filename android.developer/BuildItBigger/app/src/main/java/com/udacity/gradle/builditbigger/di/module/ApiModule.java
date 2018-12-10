package com.udacity.gradle.builditbigger.di.module;

import com.udacity.gradle.builditbigger.api.JokesApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * DI module providing access to the Recipe API endpoint.
 */
@Module
public class ApiModule {
    @Provides
    @Singleton
    public static JokesApi provideJokesApi(Retrofit retrofit) {
        return retrofit.create(JokesApi.class);
    }
}

