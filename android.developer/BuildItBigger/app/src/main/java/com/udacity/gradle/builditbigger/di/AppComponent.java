package com.udacity.gradle.builditbigger.di;

import com.udacity.gradle.builditbigger.di.module.ApiModule;
import com.udacity.gradle.builditbigger.di.module.AppModule;
import com.udacity.gradle.builditbigger.di.module.NetModule;
import com.udacity.gradle.builditbigger.ui.MainActivityViewModel;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Entry-point for our DI setup.
 *
 * The "application component" is instantiated as a singleton at the application level.
 *
 * Our DI setup defines 4 modules:
 *  - app module: provides a reference to our application (and its context)
 *  - api module: provides a client (via Retrofit) to connect to the Movie DB API
 *  - net module: provides the low-level networking entities (like HTTP client, Retrofit, Gson)
 *  - DAO module: provides access to the DB via various DAO instances.
 */
@Singleton
@Component(
        modules = {AppModule.class, ApiModule.class, NetModule.class}
)
public interface AppComponent {
    void inject(MainActivityViewModel viewModel);
}

