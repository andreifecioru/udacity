package com.example.android.funtravel.di.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import retrofit2.Retrofit;

import com.example.android.funtravel.api.FunTravelApi;


/**
 * DI module providing access to the FunTravel API endpoint.
 */
@Module
public class ApiModule {
    @Provides
    @Singleton
    public static FunTravelApi providesFunTravelApi(Retrofit retrofit) {
        return retrofit.create(FunTravelApi.class);
    }
}

