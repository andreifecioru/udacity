package com.android.example.bakingapp.utils;

import com.android.example.bakingapp.di.AppComponent;
import com.android.example.bakingapp.di.DaggerAppComponent;
import com.android.example.bakingapp.di.module.AppModule;
import com.android.example.bakingapp.ui.BakingApplication;

/**
 * Custom implementation of {@link BakingApplication} where we are able to
 * inject the various "testing" modules (i.e. modules tweaked specifically for
 * testing purposes.
 */
public class TestBakingApp extends BakingApplication {
    @Override
    public AppComponent getAppComponent() {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new TestNetModule())
                .daoModule(new TestDaoModule())
                .build();
    }

}
