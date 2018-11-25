package com.android.example.bakingapp.ui;

import android.app.Application;

import com.android.example.bakingapp.di.AppComponent;
import com.android.example.bakingapp.di.DaggerAppComponent;
import com.android.example.bakingapp.di.module.AppModule;

/**
 * Custom {@link Application} implementation for our app.
 *
 * Kicks-off our DI setup by providing our {@link AppComponent} as
 * an application-wide singleton.
 */
public class BakingApplication extends Application {
    private AppComponent mAppComponent;

    private static BakingApplication mAppInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppInstance = BakingApplication.this;
    }

    public static BakingApplication getInstance() {
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
