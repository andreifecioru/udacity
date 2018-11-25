package com.android.example.bakingapp.di.module;

import android.net.Uri;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import com.android.example.bakingapp.utils.LiveDataCallAdapterFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * DI module providing low level networking objects like HTTP/Retrofit clients,
 * JSON response parsers, etc.
 */
@Module
public class NetModule {
    // Change this is you want to get the data from a different location
    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";

    // Local end-point used during testing
//    private static final String BASE_URL = "http://10.0.2.2:8000/";

    @Provides
    @Singleton
    public Gson provideGson() {
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
    }

    @Provides
    @Singleton
    public OkHttpClient provideHttpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        return httpClient.build();
    }

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

    public static Uri.Builder to(Uri.Builder builder) {
        return builder.build().buildUpon();
    }
}
