package com.android.example.bakingapp.utils;

import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.android.example.bakingapp.di.module.NetModule;


/**
 *  Custom implementation of out {@link NetModule} used solely for testing purposes.
 *  It's BASE_URL points to where the {@link MockWebServer} is started.
 */
@Module
public class TestNetModule extends NetModule {
    private static final String BASE_URL = "http://localhost:8080/";

    @Provides
    @Singleton
    public Retrofit provideRetrofit(Gson gson, OkHttpClient httpClient) {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .client(httpClient)
                .build();
    }
}
