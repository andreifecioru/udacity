package com.example.android.funtravel.ui;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.example.android.funtravel.FunTravelApp;
import com.example.android.funtravel.R;
import com.example.android.funtravel.common.model.Offer;
import com.example.android.funtravel.model.ParcelableOffer;
import com.example.android.funtravel.ui.widget.FunTravelWidget;
import com.example.android.funtravel.utils.PreferenceUtils;
import com.example.android.funtravel.utils.Resource;
import com.example.android.funtravel.utils.ui.RecyclerViewEmptyViewSupport;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;


import static android.content.Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS;
import static com.example.android.funtravel.ui.OfferOverviewFragment.OFFER_ARGS_KEY;

public class ListOffersActivity
        extends BaseActivity
        implements OfferAdapter.OnOfferClick ,
                   SwipeRefreshLayout.OnRefreshListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_offers);

        ButterKnife.bind(this);

        // Get a handle on the view model
        mViewModel = ListOffersViewModel.create(this);
        // ... and inject it into our DI setup
        FunTravelApp.getInstance().getAppComponent().inject(mViewModel);

        // TODO: make column count dependant on screen width
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, mGridColumnCount);
        mOffersRecyclerView.setLayoutManager(layoutManager);

        // Setup the adapter for our recycler view
        mOfferAdapter = new OfferAdapter(this, this, mViewModel, this);
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
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
            }
        });

        // Setup the action-bar and the home button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        // Kick-start data loading
        mSwipeRefreshLayout.setRefreshing(true);
        fetchOffers();

    }

    private void fetchOffers() {
        Log.d(LOG_TAG, "Refreshing offer list");

        int maxOfferCount = PreferenceUtils.getMaxOfferCountSetting(this);

        // Watch the view model for any changes in the underlying live data
        mViewModel.fetchOffers(maxOfferCount).observe(this, new Observer<Resource<Long>>() {
            @Override
            public void onChanged(@Nullable Resource<Long> resource) {
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
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
        Log.d(LOG_TAG, "Fetching offers on user request.");

        // In order to force the re-loading of the entire offer-list,
        // we first have to clear the local data cache (i.e. reset the DB contents).
        mViewModel.deleteAllOffers(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Kick-start data loading
                        mSwipeRefreshLayout.setRefreshing(true);
                        fetchOffers();
                    }
                });
            }
        });
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
}
