package com.example.android.funtravel.utils;

import com.example.android.funtravel.di.AppComponent;
import com.example.android.funtravel.FunTravelApp;
import com.example.android.funtravel.di.module.AppModule;
import com.example.android.funtravel.di.DaggerAppComponent;

/**
 * Custom implementation of {@link FunTravelApp} where we are able to
 * inject the various "testing" modules (i.e. modules tweaked specifically for
 * testing purposes.
 */
public class TestFunTravelApp extends FunTravelApp {
    @Override
    public AppComponent getAppComponent() {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new TestNetModule())
                .daoModule(new TestDaoModule())
                .build();
    }

}
