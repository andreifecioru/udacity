package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.models.Movies;
import com.example.android.popularmovies.utils.ui.RecyclerViewEmptyViewSupport;

import static com.example.android.popularmovies.models.Movies.MOVIE_DB_URI_BUILDER_API;
import static com.example.android.popularmovies.models.Movies.EMPTY;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ListMoviesActivity extends AppCompatActivity
    implements LoaderManager.LoaderCallbacks<List<Movie>>,
               MovieAdapter.OnMoviePosterClick {

    private static final String LOG_TAG = ListMoviesActivity.class.getSimpleName();

    public static final String INTENT_EXTRA_MOVIE_KEY = "movie";

    private static final int POSTERS_LOADER_ID = 1;

    @BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.navigation_view) NavigationView mNavigationView;
    @BindView(R.id.rv_movie_list) RecyclerViewEmptyViewSupport mMoviePostersRecyclerView;
    @BindView(R.id.empty_view) LinearLayout mEmptyView;
    @BindView(R.id.empty_view_no_movies) LinearLayout mEmptyViewNoMovies;
    @BindView(R.id.pb_empty_view) ProgressBar mEmptyViewProgressBar;
    @BindView(R.id.iv_empty_movie_list) ImageView mNoMoviesImageView;
    @BindView(R.id.iv_no_internet) ImageView mNoInternetImageView;

    private ActionBarDrawerToggle mDrawerToggle;
    private MovieAdapter mMovieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_movies);

        ButterKnife.bind(this);
        // Integration between the activity and the navigation drawer.
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close);
        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        // Item size does not change
        mMoviePostersRecyclerView.setHasFixedSize(true);

        // We use a grid layout manager (2 columns)
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        mMoviePostersRecyclerView.setLayoutManager(layoutManager);

        // Setup the adapter for our recycler view (start with an empty list)
        mMovieAdapter = new MovieAdapter(EMPTY, this);
        mMoviePostersRecyclerView.setAdapter(mMovieAdapter);

        // Set the empty view for our recycler view
        mMoviePostersRecyclerView.setEmptyView(mEmptyView);

        // Set the navigation view events/handlers
        mNavigationView = findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                switch (id) {
                    case R.id.nav_menu_settings:
                        Intent intent = new Intent(ListMoviesActivity.this, SettingsActivity.class);
                        startActivity(intent);
                    default:
                        Log.w(LOG_TAG, "Invalid menu selection: " + id);
                }
                return false;
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        // Setup the async-loader
        getSupportLoaderManager().initLoader(POSTERS_LOADER_ID, null, this);

        // Initiate download
        downloadPosters();
    }

    @Override
    protected void onStop() {
        super.onStop();

        mDrawerLayout.closeDrawers();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**********************************************
     * Helper methods
     *********************************************/
    private void downloadPosters() {
        if (isOnline()) {
            Loader<Movie> loader = getSupportLoaderManager().getLoader(POSTERS_LOADER_ID);
            if (loader != null) {
                mEmptyViewProgressBar.setVisibility(View.VISIBLE);
                mEmptyViewNoMovies.setVisibility(View.GONE);
                loader.forceLoad();
            }

            // When we are online, hide the "no-connection" image.
            mNoMoviesImageView.setVisibility(View.VISIBLE);
            mNoInternetImageView.setVisibility(View.GONE);

        } else {
            // When we are offline, show the "no-connection" image.
            mNoMoviesImageView.setVisibility(View.GONE);
            mEmptyViewProgressBar.setVisibility(View.GONE);
            mNoInternetImageView.setVisibility(View.VISIBLE);

            Toast.makeText(this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }
    }

    // Checks if we have connectivity or not.
    private boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connMgr != null) {
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }

        return false;
    }

    /**********************************************
     * LoaderManager callbacks
     *********************************************/
    @NonNull
    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Build the URI based on the values of our preferences.
        String orderBy = prefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));

        if (orderBy.equals(getString(R.string.settings_order_by_most_popular_value))) {
            setTitle(getString(R.string.settings_order_by_most_popular_label));
        } else if (orderBy.equals(getString(R.string.settings_order_by_top_rated_value))) {
            setTitle(getString(R.string.settings_order_by_top_rated_label));
        }

        return new MovieAsyncTaskLoader(this,
                Movies.to(MOVIE_DB_URI_BUILDER_API).appendPath(orderBy).build());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Movie>> loader,
                               List<Movie> movies) {
        // Hide the loading progress indicator
        mEmptyViewProgressBar.setVisibility(View.GONE);
        mEmptyViewNoMovies.setVisibility(View.VISIBLE);

        mMovieAdapter.setMovies(movies);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Movie>> loader) {
        mMovieAdapter.setMovies(EMPTY);
    }

    @OnClick(R.id.bt_try_again)
    public void onTryAgainButtonClick(View view) {
        downloadPosters();
    }

    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        // Attach the "clicked" movie as "extra-data" to the intent.
        intent.putExtra(INTENT_EXTRA_MOVIE_KEY, movie);

        // Launch the "movie-details" activity.
        startActivity(intent);
    }
}
