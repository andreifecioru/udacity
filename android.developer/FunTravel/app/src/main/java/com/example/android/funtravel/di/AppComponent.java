package com.example.android.funtravel.di;

import javax.inject.Singleton;

import dagger.Component;

import com.example.android.funtravel.di.module.*;
import com.example.android.funtravel.ui.ListOffersViewModel;
import com.example.android.funtravel.ui.ListReviewsViewModel;
import com.example.android.funtravel.ui.widget.FunTravelRemoteViewsFactory;

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
    modules = {AppModule.class, ApiModule.class, NetModule.class, DaoModule.class}
)
public interface AppComponent {
    void inject(ListOffersViewModel viewModel);
    void inject(ListReviewsViewModel viewModel);
    void inject(FunTravelRemoteViewsFactory remoteViewsFactory);
}
