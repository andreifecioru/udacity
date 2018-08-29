package com.example.android.popularmovies;

import android.app.Application;

import com.example.android.popularmovies.di.AppComponent;
import com.example.android.popularmovies.di.DaggerAppComponent;
import com.example.android.popularmovies.di.module.AppModule;

/**
 * Custom {@link Application} implementation for our app.
 *
 * Kicks-off our DI setup by providing our {@link AppComponent} as
 * an application-wide singleton.
 */
public class PopularMoviesApplication extends Application {
    private static AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppComponent = DaggerAppComponent.builder()
            .appModule(new AppModule(this))
            .build();
    }

    public static AppComponent getAppComponent() {
        return mAppComponent;
    }
}
