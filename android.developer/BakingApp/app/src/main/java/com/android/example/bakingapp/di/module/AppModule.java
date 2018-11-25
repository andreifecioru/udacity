package com.android.example.bakingapp.di.module;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * DI module providing access to the application and application context.
 */
@Module
public class AppModule {
    final private Application mApplication;

    public AppModule(Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    public  Application provideApplication() {
        return mApplication;
    }
}
