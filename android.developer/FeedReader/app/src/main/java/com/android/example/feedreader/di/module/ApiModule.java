package com.android.example.feedreader.di.module;

import com.android.example.feedreader.api.FeedApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * DI module providing access to the Feed API endpoint.
 */
@Module
public class ApiModule {
    @Provides
    @Singleton
    public static FeedApi providesFeedApi(Retrofit retrofit) {
        return retrofit.create(FeedApi.class);
    }
}

