package com.example.android.popularmovies;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.repo.MovieRepository.SortBy;
import com.example.android.popularmovies.utils.PreferencesUtils;
import com.example.android.popularmovies.utils.Resource;
import com.example.android.popularmovies.utils.ui.RecyclerViewEmptyViewSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.android.popularmovies.MovieDetailsActivity.INTENT_EXTRA_MOVIE_KEY;

/**
 * Implements the user screen that displays the list of movies
 * based on the sort criterion stored in the app's shared preference
 * store.
 *
 * Movies are displayed in a grid layout. Each element in the grid displays:
 *  - the movie poster
 *  - the movie title
 *  - a rating widget (on a 1-5 scale)
 *  - a small button in the top-right corner allowing the user
 *    to quickly mark a movie as "favourite"
 */
public class ListMoviesActivity
        extends BaseActivity
        implements MovieAdapter.OnMoviePosterClick,
                   MovieAdapter.OnMovieFavouriteClick {

    private static final String LOG_TAG = ListMoviesActivity.class.getSimpleName();

    private static final String BUNDLE_RECYCLER_LAYOUT_STATE = "recycler_layout_state";

    // various UI controls
    @BindView(R.id.layout_coordinator) CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.navigation_view) NavigationView mNavigationView;
    @BindView(R.id.rv_movie_list) RecyclerViewEmptyViewSupport mMoviePostersRecyclerView;
    @BindView(R.id.empty_view) LinearLayout mEmptyView;
    @BindView(R.id.empty_view_no_movies) LinearLayout mEmptyViewNoMovies;
    @BindView(R.id.pb_empty_view) ProgressBar mEmptyViewProgressBar;
    @BindView(R.id.iv_empty_movie_list) ImageView mNoMoviesImageView;

    private ActionBarDrawerToggle mDrawerToggle;

    private MovieAdapter mMovieAdapter;

    private Parcelable mRecyclerViewState;

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
        mMovieAdapter = new MovieAdapter(new ArrayList<Movie>(), this, this);
        mMoviePostersRecyclerView.setAdapter(mMovieAdapter);

        // Set the empty view for our recycler view
        mMoviePostersRecyclerView.setEmptyView(mEmptyView);

        // Set the navigation view events/handlers
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

        // Setup the action-bar and the home button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        // Initial setup for the UI controls:
        // we assume that the data is loading and
        // we show the loading indicator (progress bar)
        // and hide everything else.
        mEmptyView.setVisibility(View.VISIBLE);
        mEmptyViewNoMovies.setVisibility(View.GONE);
        mEmptyViewProgressBar.setVisibility(View.VISIBLE);

        // Kick-start data loading (initializes the view model)
        refreshMovieList();
    }

    private void refreshMovieList() {
        Log.d(LOG_TAG, "Refreshing the movie list...");

        // Get a handle on the view model
        MovieListViewModel viewModel = MovieListViewModel.create(this);
        // ... and inject it into our DI setup
        PopularMoviesApplication.getAppComponent().inject(viewModel);

        // Retrieve the sort criterion from the app's shared preference store
        String sortBy = PreferencesUtils.getMoviesSortByPreferenceSetting(this);

        // Adjust the title based on the sort-by preference value
        setActivityTitle(sortBy);

        // Watch the view model for any changes in the underlying live data
        viewModel.getMovies(SortBy.fromString(sortBy)).observe(this, new Observer<Resource<List<Movie>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<Movie>> resource) {
                if (resource == null) return;

                switch (resource.status) {
                    case LOADING:
                        if (resource.data == null || resource.data.isEmpty()) {
                            // Show the loading progress indicator
                            mEmptyViewProgressBar.setVisibility(View.VISIBLE);
                            mEmptyViewNoMovies.setVisibility(View.GONE);
                        } else {
                            // Data loading is still in progress
                            // but we have some cache data to display
                            // until data is ready.

                            // Hide the loading progress indicator
                            mEmptyViewProgressBar.setVisibility(View.GONE);
                            mEmptyViewNoMovies.setVisibility(View.VISIBLE);

                            mMovieAdapter.setMovies(resource.data);
                        }
                        break;

                    case SUCCESS:
                        // Hide the loading progress indicator
                        mEmptyViewProgressBar.setVisibility(View.GONE);
                        mEmptyViewNoMovies.setVisibility(View.VISIBLE);

                        mMovieAdapter.setMovies(resource.data);
                    break;

                    case ERROR:
                        // Hide the loading progress indicator
                        mEmptyViewProgressBar.setVisibility(View.GONE);
                        mEmptyViewNoMovies.setVisibility(View.VISIBLE);

                        // Set the movie list to an empty list to
                        // force the adapter to display the "empty-list" view
                        mMovieAdapter.setMovies(new ArrayList<Movie>());
                        Snackbar.make(mCoordinatorLayout,
                                getResources().getText(R.string.error_loading_movie_list),
                                Snackbar.LENGTH_SHORT).show();
                        Log.e(LOG_TAG, "Error loading movie list: " + resource.message);
                    break;
                }

                restoreRecyclerViewState();
            }
        });
    }

    /**
     *  Helper method for changing the activity window title (in the action-bar)
     *  based on the user's current selection of the movie sorting criterion.
     *
     * @param sortByPrefValue the sort-by criterion as stored in the app's shared
     *                        preference store
     */
    private void setActivityTitle(String sortByPrefValue) {
        SortBy sortBy = SortBy.fromString(sortByPrefValue);
        if (sortBy == null) return; // sanity-check

        int titleResId;

        switch (sortBy) {
            case TOP_RATED:
                titleResId = R.string.settings_order_by_top_rated_label;
                break;

            case MOST_POPULAR:
                titleResId = R.string.settings_order_by_most_popular_label;
                break;

            case FAVOURITES:
                titleResId = R.string.settings_order_by_favourites_label;
                break;

            default:
                // default to app's name
                titleResId = R.string.app_name;

        }

        setTitle(titleResId);
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        RecyclerView.LayoutManager layoutManager = mMoviePostersRecyclerView.getLayoutManager();

        if (layoutManager != null) {
            outState.putParcelable(BUNDLE_RECYCLER_LAYOUT_STATE,
                    layoutManager.onSaveInstanceState());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            mRecyclerViewState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT_STATE);
            restoreRecyclerViewState();
        }
    }

    private void restoreRecyclerViewState() {
        if (mRecyclerViewState != null) {
            RecyclerView.LayoutManager layoutManager = mMoviePostersRecyclerView.getLayoutManager();

            if (layoutManager != null) {
                layoutManager.onRestoreInstanceState(mRecyclerViewState);
            }
        }
    }

    @OnClick(R.id.bt_try_again)
    public void onTryAgainButtonClick(View view) {
        refreshMovieList();
    }

    /**
     * Invoked when the user tapped one of the movie posters (i.e. one item
     * in the grid view).
     *
     * The user is taken to the {@link MovieDetailsActivity} where the info for the movie
     * that was tapped is displayed.
     *
     * @param movie the info for the movie item that ws tapped
     */
    @Override
    public void onMovieClick(Movie movie) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        // Attach the "clicked" movie as "extra-data" to the intent.
        intent.putExtra(INTENT_EXTRA_MOVIE_KEY, movie);

        // Launch the "movie-details" activity.
        startActivity(intent);
    }

    /**
     * Invoked when the user taps the "favourite" button in the top-right corner
     * of a movie poster.
     *
     * The "favourite" status of the corresponding {@link Movie} instance is updated
     * (i.e. toggled) and the updated instance is persisted in the local DB.
     *
     * @param movie the {@link Movie} instance corresponding to the item that was tapped.
     */
    @Override
    public void onFavouriteClick(Movie movie) {
        MovieListViewModel viewModel = MovieListViewModel.create(this);
        PopularMoviesApplication.getAppComponent().inject(viewModel);

        // update the movie data in the local DB
        viewModel.updateMovie(movie);
    }

    /**
     * Callback invoked when connectivity is restored.
     *
     * We try to refresh the data (in case it was stale).
     */
    @Override
    protected void onConnectivityRestored() {
        refreshMovieList();
    }
}
