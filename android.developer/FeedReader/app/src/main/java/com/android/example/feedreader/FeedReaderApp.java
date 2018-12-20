package com.android.example.feedreader;

import android.app.Application;

import com.android.example.feedreader.di.AppComponent;
import com.android.example.feedreader.di.DaggerAppComponent;
import com.android.example.feedreader.di.module.AppModule;


public class FeedReaderApp extends Application {
    private AppComponent mAppComponent;

    private static FeedReaderApp mAppInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppInstance = FeedReaderApp.this;
    }

    public static FeedReaderApp getInstance() {
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
