package com.example.android.funtravel;

import android.app.Application;

import com.example.android.funtravel.di.AppComponent;
import com.example.android.funtravel.di.DaggerAppComponent;
import com.example.android.funtravel.di.module.AppModule;

/**
 * Custom {@link Application} implementation where we put together
 * our DI setup.
 */
public class FunTravelApp extends Application {
    private AppComponent mAppComponent;

    private static FunTravelApp mAppInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppInstance = FunTravelApp.this;
    }

    public static FunTravelApp getInstance() {
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
