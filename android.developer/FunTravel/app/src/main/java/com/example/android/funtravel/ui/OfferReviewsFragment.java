package com.example.android.funtravel.ui;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.example.android.funtravel.FunTravelApp;
import com.example.android.funtravel.R;
import com.example.android.funtravel.utils.Resource;
import com.example.android.funtravel.utils.ui.RecyclerViewEmptyViewSupport;


/**
 * Fragment displaying the "reviews" info of an offer which includes:
 *
 * It's pretty much a one-column grid layout which displays for reach review
 * entry the name of the author, the rating and the review content.
 */
public class OfferReviewsFragment
        extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener {
    private static final String LOG_TAG = OfferReviewsFragment.class.getSimpleName();

    private static final String BUNDLE_RECYCLER_LAYOUT_STATE = "recycler.layout.state";

    public static final String OFFER_ID_ARGS_KEY = "offer.id.key";

    private static final long INVALID_OFFER_ID = -1;

    @BindView(R.id.layout_coordinator) CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.rv_review_list) RecyclerViewEmptyViewSupport mReviewsRecyclerView;
    @BindView(R.id.tv_empty_view) TextView mEmptyView;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;

    private long mOfferId = INVALID_OFFER_ID;
    private ListReviewsViewModel mViewModel;
    private Parcelable mRecyclerViewState;
    private ReviewAdapter mReviewAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Bundle args = getArguments();
        if (args != null) {
            mOfferId = args.getLong(OFFER_ID_ARGS_KEY, INVALID_OFFER_ID);
            if (mOfferId != INVALID_OFFER_ID) {
                Log.d(LOG_TAG, "Rendering reviews for offer with id: " + mOfferId);
            }
        }

        if (mOfferId == INVALID_OFFER_ID) {
            throw new IllegalStateException("Invalid offer id.");
        }

        View rootView = inflater.inflate(R.layout.fragment_offer_reviews, container, false);

        ButterKnife.bind(this, rootView);

        // Get a handle on the view model
        mViewModel = ListReviewsViewModel.create(getActivity());
        // ... and inject it into our DI setup
        FunTravelApp.getInstance().getAppComponent().inject(mViewModel);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        mReviewsRecyclerView.setLayoutManager(layoutManager);

        // Setup the adapter for our recycler view
        mReviewAdapter = new ReviewAdapter(this, mViewModel, mOfferId);
        mReviewsRecyclerView.setAdapter(mReviewAdapter);

        // Set the empty view for our recycler view
        mReviewsRecyclerView.setEmptyView(mEmptyView);

        // Set the listener for our swipe-to-refresh layout
        mSwipeRefreshLayout.setOnRefreshListener(this);

        // Kick-start data loading
        mSwipeRefreshLayout.setRefreshing(true);
        fetchReviews();

        return rootView;
    }

    // Save/restore the state of the recycler view to persist the scrolling position
    // while switching between apps.
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        RecyclerView.LayoutManager layoutManager = mReviewsRecyclerView.getLayoutManager();

        if (layoutManager != null) {
            outState.putParcelable(BUNDLE_RECYCLER_LAYOUT_STATE,
                    layoutManager.onSaveInstanceState());
        }
    }

    private void fetchReviews() {
        Log.d(LOG_TAG, "Refreshing review list");

        mViewModel.fetchReviewsForOffer(mOfferId).observe(this, new Observer<Resource<Long>>() {
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

                            mReviewAdapter.notifyChanged(resource.data);
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        break;

                    case SUCCESS:
                        Log.d(LOG_TAG, "Fetched " + resource.data + " offers.");
                        if (resource.data != null) {
                            mReviewAdapter.notifyChanged(resource.data);
                        }
                        mSwipeRefreshLayout.setRefreshing(false);
                        break;

                    case ERROR:
                        if (resource.data != null) {
                            mReviewAdapter.notifyChanged(resource.data);
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
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            mRecyclerViewState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT_STATE);
            restoreRecyclerViewState();
        }
    }

    private void restoreRecyclerViewState() {
        if (mRecyclerViewState != null) {
            RecyclerView.LayoutManager layoutManager = mReviewsRecyclerView.getLayoutManager();

            if (layoutManager != null) {
                layoutManager.onRestoreInstanceState(mRecyclerViewState);
            }
        }
    }

    @Override
    public void onRefresh() {
        Log.d(LOG_TAG, "Fetching reviews on user request.");

        // In order to force the re-loading of the entire offer-list,
        // we first have to clear the local data cache (i.e. reset the DB contents).
        mViewModel.deleteAllReviewsForOffer(mOfferId, () -> {
            FragmentActivity activity = getActivity();
            if (activity != null) {
                activity.runOnUiThread(() -> {
                    // Kick-start data loading
                    mSwipeRefreshLayout.setRefreshing(true);
                    fetchReviews();
                });
            }
        });
    }
}
