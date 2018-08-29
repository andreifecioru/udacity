package com.example.android.popularmovies.di.module;

import android.net.Uri;

import com.example.android.popularmovies.utils.LiveDataCallAdapterFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
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
    public static final String MOVIE_DB_API_KEY = "210c9ee381717d30c2aaa0f06b795b5a";

    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";

    static final private Uri.Builder MOVIE_DB_URI_BUILDER_IMAGES = new Uri.Builder()
            .scheme("https")
            .authority("image.tmdb.org")
            .appendPath("t").appendPath("p");

    public static final Uri.Builder MOVIE_DB_URI_BUILDER_POSTER = to(MOVIE_DB_URI_BUILDER_IMAGES)
            .appendPath("w342");

    public static final Uri.Builder MOVIE_DB_URI_BUILDER_BACKDROP = to(MOVIE_DB_URI_BUILDER_IMAGES)
            .appendPath("w342");

    @Provides
    @Singleton
    public static Gson provideGson() {
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
    }

    @Provides
    @Singleton
    public static OkHttpClient provideHttpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        return httpClient.build();
    }

    @Provides
    @Singleton
    public static Retrofit provideRetrofit(Gson gson, OkHttpClient httpClient) {
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
