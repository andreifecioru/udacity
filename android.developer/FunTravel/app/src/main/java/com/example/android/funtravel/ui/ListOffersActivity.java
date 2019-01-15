package com.example.android.funtravel.ui;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.content.Intent;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.Fragment;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.lifecycle.ViewModel;

import com.example.android.funtravel.utils.SimpleIdlingResource;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import com.example.android.funtravel.FunTravelApp;
import com.example.android.funtravel.R;
import com.example.android.funtravel.common.model.Offer;
import com.example.android.funtravel.model.ParcelableOffer;
import com.example.android.funtravel.ui.widget.FunTravelWidget;
import com.example.android.funtravel.utils.PreferenceUtils;
import com.example.android.funtravel.utils.ui.RecyclerViewEmptyViewSupport;

import androidx.test.espresso.IdlingResource;
import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;


import static android.content.Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS;
import static com.example.android.funtravel.ui.OfferOverviewFragment.OFFER_ARGS_KEY;

/**
 * Implements the functionality for the main screen of the app.
 *
 * Content basically consists of a grid-layout presenting a listing of available offers.
 * The code in this class handles the following:
 *   - orchestrates the navigation using a navigation-drawer pattern
 *   - triggers the retrieval of offers from the backend service (with local DB caching)
 *     via a {@link ViewModel} implementation.
 *   - handles the interaction with the {@link SwipeRefreshLayout} UI control to let the user
 *     trigger a DB refresh.
 *   - launches the {@link OfferDetailsActivity} when the user taps one of the items
 *     in the grid-view.
 */
public class ListOffersActivity
        extends BaseActivity
        implements OfferAdapter.OnOfferClick ,
                   SwipeRefreshLayout.OnRefreshListener,
                   SharedPreferences.OnSharedPreferenceChangeListener{

    private static final String LOG_TAG = ListOffersActivity.class.getSimpleName();

    private static final String BUNDLE_RECYCLER_LAYOUT_STATE = "recycler.layout.state";

    @Nullable @BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.navigation_view) NavigationView mNavigationView;

    @BindView(R.id.layout_coordinator) CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.rv_offer_list) RecyclerViewEmptyViewSupport mOffersRecyclerView;
    @BindView(R.id.empty_view) LinearLayout mEmptyView;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;

    @BindInt(R.integer.offer_list_column_count) int mGridColumnCount;

    private ActionBarDrawerToggle mDrawerToggle;

    private OfferAdapter mOfferAdapter;
    private Parcelable mRecyclerViewState;
    private ListOffersViewModel mViewModel;

    private Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_offers);

        ButterKnife.bind(this);

        // Get a handle on the view model
        mViewModel = ListOffersViewModel.create(this);
        // ... and inject it into our DI setup
        FunTravelApp.getInstance().getAppComponent().inject(mViewModel);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, mGridColumnCount);
        mOffersRecyclerView.setLayoutManager(layoutManager);

        // Setup the adapter for our recycler view
        mFragment = getSupportFragmentManager().findFragmentById(R.id.fragment);
        mOfferAdapter = new OfferAdapter(this, mFragment, mViewModel, this);
        mOffersRecyclerView.setAdapter(mOfferAdapter);

        // Set the empty view for our recycler view
        mOffersRecyclerView.setEmptyView(mEmptyView);

        // Set the listener for our swipe-to-refresh layout
        mSwipeRefreshLayout.setOnRefreshListener(this);

        // Integration between the activity and the navigation drawer.
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close);
        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        // Set the navigation view events/handlers
        mNavigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            switch (id) {
                case R.id.nav_menu_settings:
                    Intent intent = new Intent(ListOffersActivity.this, SettingsActivity.class);
                    intent.addFlags(FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    startActivity(intent);
                default:
                    Log.w(LOG_TAG, "Invalid menu selection: " + id);
            }
            return false;
        });

        // Setup the action-bar and the home button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        // Register the activity to listen for changes in the pref. store
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        // Kick-start data loading
        mSwipeRefreshLayout.setRefreshing(true);
        if (mIdlingResource == null) {
            fetchOffers();
        }

    }

    @VisibleForTesting
    public void fetchOffers() {
        Log.d(LOG_TAG, "Refreshing offer list");

        // Lock idling resource
        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(false);
        }

        int maxOfferCount = PreferenceUtils.getMaxOfferCountSetting(this);
        int flags = PreferenceUtils.getOfferCategoryFlags(this);

        // Watch the view model for any changes in the underlying live data
        mViewModel.fetchOffers(maxOfferCount, flags).observe(mFragment, resource -> {
            if (resource == null) return;

            switch (resource.status) {
                case LOADING:
                    if (resource.data == null) {
                        Log.d(LOG_TAG, "No data available in local cache. Loading...");
                    } else {
                        // Data loading is still in progress
                        // but we have some cache data to display
                        // until data is ready.
                        Log.d(LOG_TAG, "Data available in local cache. Loading...");

                        mOfferAdapter.notifyChanged(resource.data);
                        mSwipeRefreshLayout.setRefreshing(false);
                        updateAppWidget();
                    }
                    break;

                case SUCCESS:
                    Log.d(LOG_TAG, "Fetched " + resource.data + " offers.");
                    if (resource.data != null) {
                        mOfferAdapter.notifyChanged(resource.data);
                    }
                    mSwipeRefreshLayout.setRefreshing(false);
                    updateAppWidget();
                    break;

                case ERROR:
                    if (resource.data != null) {
                        mOfferAdapter.notifyChanged(resource.data);
                    }
                    mSwipeRefreshLayout.setRefreshing(false);

                    Snackbar.make(mCoordinatorLayout,
                            getResources().getText(R.string.error_loading_offer_list),
                            Snackbar.LENGTH_SHORT).show();

                    Log.e(LOG_TAG, "Error loading offer list. " + resource.message);
                    break;

                default:
                    throw new IllegalStateException("Unknown resource status: " + resource.status);
            }

            // Release idling resource
            if (mIdlingResource != null) {
                mIdlingResource.setIdleState(true);
            }
        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_list_offers_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.menu_refresh:
                Log.d(LOG_TAG, "Fetching articles on user request.");
                mSwipeRefreshLayout.setRefreshing(true);
                fetchOffers();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Save/restore the state of the recycler view to persist the scrolling position
    // while switching between apps.
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        RecyclerView.LayoutManager layoutManager = mOffersRecyclerView.getLayoutManager();

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
            RecyclerView.LayoutManager layoutManager = mOffersRecyclerView.getLayoutManager();

            if (layoutManager != null) {
                layoutManager.onRestoreInstanceState(mRecyclerViewState);
            }
        }
    }

    @Override
    public void onRefresh() {
        Log.d(LOG_TAG, "Fetching a new set of offers.");

        // In order to force the re-loading of the entire offer-list,
        // we first have to clear the local data cache (i.e. reset the DB contents).
        mViewModel.deleteAllOffers(() -> runOnUiThread(() -> {
            // Kick-start data loading
            mSwipeRefreshLayout.setRefreshing(true);
            fetchOffers();
        }));
    }

    @Override
    public void onOfferClick(Offer offer, int position) {
        Intent intent = new Intent(this, OfferDetailsActivity.class);
        intent.putExtra(OFFER_ARGS_KEY, (ParcelableOffer) offer);

        startActivity(intent);
    }

    private void updateAppWidget() {
        FunTravelWidget.updateFunTravelWidget(ListOffersActivity.this.getApplicationContext());
    }

    // Setup of idling resource used during instrumented testing.
    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }

        return mIdlingResource;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        onRefresh();
    }
}
