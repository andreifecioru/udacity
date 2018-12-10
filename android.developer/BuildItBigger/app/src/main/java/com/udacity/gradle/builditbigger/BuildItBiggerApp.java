package com.udacity.gradle.builditbigger;

import android.app.Application;

import com.udacity.gradle.builditbigger.di.AppComponent;
import com.udacity.gradle.builditbigger.di.DaggerAppComponent;
import com.udacity.gradle.builditbigger.di.module.AppModule;

/**
 * Custom {@link Application} implementation for our app.
 *
 * Kicks-off our DI setup by providing our {@link AppComponent} as
 * an application-wide singleton.
 */
public class BuildItBiggerApp extends Application {
    private AppComponent mAppComponent;

    private static BuildItBiggerApp mAppInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppInstance = BuildItBiggerApp.this;
    }

    public static BuildItBiggerApp getInstance() {
        return mAppInstance;
    }

    public AppComponent getAppComponent() {
        if (mAppComponent == null) {
            mAppComponent = DaggerAppComponent.builder()
                    .appModule(new AppModule(this))
                    .build();
        }

        return mAppComponent;
    }
}
