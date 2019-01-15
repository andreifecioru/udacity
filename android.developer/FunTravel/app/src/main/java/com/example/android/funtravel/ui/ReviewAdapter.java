package com.example.android.funtravel.ui;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.funtravel.R;
import com.example.android.funtravel.common.model.Review;
import com.example.android.funtravel.model.ParcelableReview;
import com.example.android.funtravel.repo.FunTravelRepository;
import com.example.android.funtravel.utils.OfferUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * RecyclerView adapter for the {@link Review} class. Fuels the grid-view
 * which displays the list of offer reviews.
 */
public class ReviewAdapter
        extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private static final String LOG_TAG = ReviewAdapter.class.getSimpleName();

    private final ListReviewsViewModel mViewModel;
    private final LifecycleOwner mOwner;
    private final long mOfferId;

    private long mReviewCount = 0;

    /** Produces an instance of the {@link ReviewAdapter} class (constructor)
     *
     *
     * @param context An instance of {@link Context} (allows us to access app's resources)
     * @param owner An instance of {@link LifecycleOwner} (allows us to observe {@link LiveData} objects.
     * @param viewModel An instance of {@link ListOffersViewModel} (provides access to {@link FunTravelRepository}).
     * @param offerId The ID of the offer associated with the reviews displayed by this adapter.
     */
    ReviewAdapter(LifecycleOwner owner,
                  ListReviewsViewModel viewModel,
                  long offerId) {
        mOwner = owner;
        mViewModel = viewModel;
        mOfferId = offerId;
    }

    /**
     * Forces an update of the adapter view.
     *
     * @param reviewCount The number of reviews to be displayed by this adapter.
     */
    public void notifyChanged(long reviewCount) {
        mReviewCount = reviewCount;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for the article item.
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View root = inflater.inflate(R.layout.review_item, parent, false);

        // Return the view holder
        return new ReviewViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull final ReviewViewHolder holder, int position) {
        final int pos = position;
        // Get the article at position
        mViewModel.getReviewAtPositionForOffer(position, mOfferId).observe(mOwner, new Observer<ParcelableReview>() {
            @Override
            public void onChanged(@Nullable ParcelableReview review) {
                // Bind data to the view-holder
                holder.bindOffer(review, pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (int) mReviewCount;
    }

    // Implements the view-holder pattern.
    class ReviewViewHolder
            extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_author_name) TextView mAuthorNameTextView;
        @BindView(R.id.ll_rating_container) LinearLayout mRatingContainerLinearLayout;
        @BindView(R.id.tv_review_content) TextView mReviewContentTextView;

        private Review mReview;
        private int mPosition;

        ReviewViewHolder(@NonNull View root) {
            super(root);

            ButterKnife.bind(this, root);
        }

        // Binding method.
        void bindOffer(Review review, int position) {
            // Sanity check: fast exit
            if (review == null) return;

            mReview = review;
            mPosition = position;

            // Display the author name
            mAuthorNameTextView.setText(mReview.getAuthor());

            // Display the rating
            OfferUtils.showRatingStars(mRatingContainerLinearLayout, mReview.getRating());

            // Display the review content
            mReviewContentTextView.setText(mReview.getContent());
        }
    }
}

