package com.example.android.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.models.Movie;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.popularmovies.ListMoviesActivity.INTENT_EXTRA_MOVIE_KEY;

public class MovieDetailsActivity extends AppCompatActivity {
    private static final String LOG_TAG = MovieDetailsActivity.class.getSimpleName();

    private Movie mMovie;

    @BindView(R.id.iv_backdrop) ImageView mBackdropImageView;
    @BindView(R.id.tv_title) TextView mTitleTextView;
    @BindView(R.id.tv_user_rating) TextView mUserRatingTextView;
    @BindView(R.id.tv_release_date) TextView mReleaseDateTextView;
    @BindView(R.id.tv_synopsis) TextView mSynopsisTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Intent intent = getIntent();
        if (intent.hasExtra(INTENT_EXTRA_MOVIE_KEY)) {
            // Extract the movie data from the intent.
            mMovie = (Movie) intent.getSerializableExtra(INTENT_EXTRA_MOVIE_KEY);

            Log.d(LOG_TAG, mMovie.toString());
        }

        if (mMovie == null) {
            // If no movie data can be extracted, we're done with this activity.
            finish();
            return;
        }

        ButterKnife.bind(this);

        // Render the backdrop image for the movie
        Picasso.with(this)
                .load(mMovie.getBackdropUrl())
                .placeholder(R.drawable.poster_placeholder)
                .centerCrop().fit()
                .into(mBackdropImageView);

        // Fill in the rest of the movie data
        mTitleTextView.setText(mMovie.getTitle());
        mUserRatingTextView.setText(getString(R.string.user_rating, String.valueOf(mMovie.getUserRating())));
        mReleaseDateTextView.setText(getString(R.string.release_date, String.valueOf(mMovie.getReleaseDate())));
        mSynopsisTextView.setText(mMovie.getSynopsis());
    }
}
