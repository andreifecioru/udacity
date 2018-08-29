package com.example.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.android.popularmovies.models.Review;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Implements the user screen that displays a movie review.
 *
 * This activity was created because some of the reviews are quite lengthy and
 * cannot be displayed easily inside a list item w/o terribly distorting the layout.
 *
 * In this activity the review text is displayed wrapped inside a scroll view allowing
 * the reader to read it with ease (no matter the verbosity of the review).
 */
public class ReviewDetailsActivity extends BaseActivity {
    private static final String LOG_TAG = ReviewDetailsActivity.class.getSimpleName();

    public static final String INTENT_EXTRA_REVIEW_KEY = "review";

    private Review mReview;

    @BindView(R.id.tv_review_content) TextView mReviewContentTextView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_details);

        Intent intent = getIntent();
        if (intent.hasExtra(INTENT_EXTRA_REVIEW_KEY)) {
            // Extract the review data from the intent.
            mReview = intent.getParcelableExtra(INTENT_EXTRA_REVIEW_KEY);

            Log.d(LOG_TAG, mReview.toString());
        }

        if (mReview == null) {
            // If no review data can be extracted, we're done with this activity.
            finish();
            return;
        }

        // We want the author of the review to be displayed in the action-bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.review_author_says, mReview.getAuthor()));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        ButterKnife.bind(this);

        mReviewContentTextView.setText(mReview.getContent());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
         switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
         }
        return super.onOptionsItemSelected(item);
    }
}
