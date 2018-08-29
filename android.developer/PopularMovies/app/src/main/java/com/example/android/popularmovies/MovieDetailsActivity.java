package com.example.android.popularmovies;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.models.Review;
import com.example.android.popularmovies.models.Trailer;
import com.example.android.popularmovies.utils.Resource;
import com.example.android.popularmovies.utils.YouTubeUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Implements the user screen which displays a single movie
 * detailed info.
 *
 * The screen layout is as follows:
 *  - a movie backdrop picture (1/3 of the screen)
 *  - a button which allows the user to toggle the
 *    "favourite" status for the movie
 *  - movie info (2/3 of the screen) in a list layout with variable item layouts:
 *    - basic movie info (title, user rating, release date, etc.)
 *    - a list of related videos (i.e. trailers)
 *    - a list of reviews
 */
public class MovieDetailsActivity
        extends BaseActivity
        implements MovieDetailsAdapter.OnTrailerClickListener,
                   MovieDetailsAdapter.OnReviewClickListener {

    private static final String LOG_TAG = MovieDetailsActivity.class.getSimpleName();

    private static final String BUNDLE_RECYCLER_LAYOUT_STATE = "recycler_layout_state";

    public static final String INTENT_EXTRA_MOVIE_KEY = "movie";

    private Movie mMovie;

    // Various UI controls
    @BindView(R.id.layout_coordinator) CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.iv_backdrop) ImageView mBackdropImageView;
    @BindView(R.id.rv_movie_details) RecyclerView mMovieDetailsRecyclerView;
    @BindView(R.id.btn_favourite) Button mFavouriteButton;

    private MovieDetailsAdapter mMovieDetailsAdapter;

    private Parcelable mRecyclerViewState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Intent intent = getIntent();
        if (intent.hasExtra(INTENT_EXTRA_MOVIE_KEY)) {
            // Extract the movie data from the intent.
            mMovie = intent.getParcelableExtra(INTENT_EXTRA_MOVIE_KEY);

            Log.d(LOG_TAG, mMovie.toString());
        }

        if (mMovie == null) {
            // If no movie data can be extracted, we're done with this activity.
            finish();
            return;
        }

        ButterKnife.bind(this);

        // Render the backdrop image for the movie
        String backdropUrl = mMovie.getBackdropUrl().trim();
        if (TextUtils.isEmpty(backdropUrl)) {
            mBackdropImageView.setImageResource(R.drawable.poster_placeholder);
        } else {
            Picasso.with(this)
                    .load(mMovie.getBackdropUrl())
                    .placeholder(R.drawable.poster_placeholder)
                    .error(R.drawable.poster_placeholder)
                    .centerCrop().fit()
                    .into(mBackdropImageView);
        }

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mMovieDetailsRecyclerView.setLayoutManager(layoutManager);
        // Our item layout is not static (changes depending on the info to be rendered).
        mMovieDetailsRecyclerView.setHasFixedSize(false);

        mMovieDetailsAdapter = new MovieDetailsAdapter(this, mMovie, this, this);
        mMovieDetailsRecyclerView.setAdapter(mMovieDetailsAdapter);

        // We want the title in the action-bar to match the movie title.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(mMovie.getTitle());
        }

        // Do the initial refresh of our UI controls.
        refreshMovieInfo();
        refreshFavouriteStatus();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        RecyclerView.LayoutManager layoutManager = mMovieDetailsRecyclerView.getLayoutManager();

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
            RecyclerView.LayoutManager layoutManager = mMovieDetailsRecyclerView.getLayoutManager();

            if (layoutManager != null) {
                layoutManager.onRestoreInstanceState(mRecyclerViewState);
            }
        }
    }

    /**
     * Method invoked when the user taps one of the items in the trailers section.
     *
     * We want to open the YouTube video using either the app (if present) or
     * inside the web-browser.
     *
     * @param trailer the {@link Trailer} instance corresponding to the tapped trailer
     *                item.
     */
    @Override
    public void onTrailerClick(Trailer trailer) {
        YouTubeUtils.openYouTubeVideo(this, trailer.getKey());
    }

    /**
     * Method invoked when the user taps one of the items in the reviews section.
     *
     * The user is taken to the {@link ReviewDetailsActivity} where the whole review is
     * displayed.
     *
     * NOTE: in the list item view only the first line of the review is shown.
     *
     * @param review the {@link Review} instance corresponding to the tapped review item.
     */
    @Override
    public void onReviewClick(Review review) {
        Intent intent = new Intent(this, ReviewDetailsActivity.class);
        // Pass the Review instance as extra data on the intent.
        intent.putExtra(ReviewDetailsActivity.INTENT_EXTRA_REVIEW_KEY, review);

        // Start the ReviewDetailsActivity
        startActivity(intent);
    }

    /**
     * Helper method for refreshing the trailer list and the review list.
     *
     * NOTE: the basic movie info (i.e. title, user-rating, etc) comes as
     * extra info on the intent that starts this activity (no need to refresh that).
     */
    private void refreshMovieInfo() {
        // Setup the view-model
        MovieDetailsViewModel viewModel = MovieDetailsViewModel.create(this);
        // ... and inject it into our DI setup
        PopularMoviesApplication.getAppComponent().inject(viewModel);

        // Watch the view-model for changes in the underlying live data
        // ... for trailers
        viewModel.getTrailers(mMovie.getId()).observe(this, new Observer<Resource<List<Trailer>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<Trailer>> resource) {
                if (resource == null) return;
                switch (resource.status) {
                    case LOADING:
                        Log.d(LOG_TAG, "Trailers loading...");
                        mMovieDetailsAdapter.setTrailerList(null);
                        break;

                    case SUCCESS:
                        Log.d(LOG_TAG, "Trailers successfully loaded.");
                        mMovieDetailsAdapter.setTrailerList(resource.data);
                        break;

                    case ERROR:
                        Snackbar.make(mCoordinatorLayout,
                                getResources().getText(R.string.error_loading_trailer_list),
                                Snackbar.LENGTH_SHORT).show();
                        Log.e(LOG_TAG, "Error loading trailers: " + resource.message);
                    break;
                }
            }
        });

        // ... and for the reviews
        viewModel.getReviews(mMovie.getId()).observe(this, new Observer<Resource<List<Review>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<Review>> resource) {
                if (resource == null) return;

                switch (resource.status) {
                    case LOADING:
                        Log.d(LOG_TAG, "Reviews loading...");
                        mMovieDetailsAdapter.setReviewList(null);
                        break;

                    case SUCCESS:
                        Log.d(LOG_TAG, "Reviews successfully loaded.");
                        mMovieDetailsAdapter.setReviewList(resource.data);
                        break;

                    case ERROR:
                        Snackbar.make(mCoordinatorLayout,
                                getResources().getText(R.string.error_loading_review_list),
                                Snackbar.LENGTH_SHORT).show();
                        Log.e(LOG_TAG, "Error loading reviews: " + resource.message);
                        break;
                }

            }
        });

        restoreRecyclerViewState();
    }

    /**
     * Method invoked when connectivity is restored (we come back online).
     *
     * We try to refresh in case we're dealing with stale data.
     */
    @Override
    protected void onConnectivityRestored() {
        refreshMovieInfo();
    }

    /**
     * Event handler for dealing with user tapping on the favourite button.
     *
     * We toggle the "favourite" status on the corresponding {@link Movie}
     * instance and we persist it in the local DB.
     */
    @OnClick(R.id.btn_favourite)
    public void onFavouriteBtnClick(View view) {
        // toggle the movie fav status
        mMovie.toggleIsFavourite();

        // update the UI
        refreshFavouriteStatus();

        // update the view model
        MovieDetailsViewModel viewModel = MovieDetailsViewModel.create(this);
        PopularMoviesApplication.getAppComponent().inject(viewModel);

        // persist the changes in the local DB
        viewModel.updateMovie(mMovie);
    }

    /**
     * Helper method used to make sure that the UI reflects the movie's
     * "favourite" status.
     */
    private void refreshFavouriteStatus() {
        if (mMovie.getIsFavourite()) {
            mFavouriteButton.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    getResources().getDrawable(android.R.drawable.star_big_on), null);
        } else {
            mFavouriteButton.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    getResources().getDrawable(android.R.drawable.star_big_off), null);
        }
    }
}
