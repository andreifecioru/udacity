package com.example.android.funtravel.di.module;

import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.android.funtravel.utils.LiveDataCallAdapterFactory;


/**
 * DI module providing low level networking objects like HTTP/Retrofit clients,
 * JSON response parsers, etc.
 */
@Module
public class NetModule {
    // In a real-world app this would be the URL of our backend (deployed somewhere in the cloud).
    // For now, we deploy the backend locally (on the host machine) and
    // assume that the app is deployed on an emulated device.
    private static final String BASE_URL = "http://10.0.2.2:8080/_ah/api/";

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

