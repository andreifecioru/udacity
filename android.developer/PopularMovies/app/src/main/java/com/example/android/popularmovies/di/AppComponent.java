package com.example.android.popularmovies.di;

import com.example.android.popularmovies.MovieDetailsViewModel;
import com.example.android.popularmovies.MovieListViewModel;
import com.example.android.popularmovies.di.module.ApiModule;
import com.example.android.popularmovies.di.module.AppModule;
import com.example.android.popularmovies.di.module.DaoModule;
import com.example.android.popularmovies.di.module.NetModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Entry-point for our DI setup.
 *
 * The "application component" is instantiated as a singleton at the application level
 * (see {@link com.example.android.popularmovies.PopularMoviesApplication}.
 *
 * Our DI setup defines 4 modules:
 *  - app module: provides a reference to our application (and its context)
 *  - api module: provides a client (via Retrofit) to connect to the Movie DB API
 *  - net module: provides the low-level networking entities (like HTTP client, Retrofit, Gson)
 *  - DAO module: provides access to the DB via various DAO instances.
 */
@Singleton
@Component(
        modules = {AppModule.class, ApiModule.class, NetModule.class, DaoModule.class}
)
public interface AppComponent {
    void inject(MovieListViewModel viewModel);
    void inject(MovieDetailsViewModel viewModel);
}
