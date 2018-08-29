package com.example.android.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.models.Review;
import com.example.android.popularmovies.models.Trailer;

import java.util.List;

/**
 * RecyclerView adapter which fuels the list view in the {@link MovieDetailsActivity}
 * activity.
 *
 * The layout is a little more complicated. Not all items have the same layout (i.e. the layout
 * changes based on the item's position in the list). The order is as follows:
 *  - the movies basic info: a vertical linear layout with info like title, user-rating etc.
 *  - a section header for trailers: a simple text view
 *  - a list of trailers: classic layout with icon and text (the trailer title)
 *  - a section header for reviews: a simple text view
 *  - a list of reviews: classic layout with icon, a title and sub-title (the 1st line in
 *    the review text)
 */
public class MovieDetailsAdapter
    extends RecyclerView.Adapter<MovieDetailsAdapter.MovieDetailsViewHolder> {

    private final static String LOG_TAG = MovieDetailsAdapter.class.getSimpleName();

    // list of view types (5 in total)
    private final static int VIEW_TYPE_MOVIE_INFO = 100;
    private final static int VIEW_TYPE_TRAILER_HEADER = 200;
    private final static int VIEW_TYPE_TRAILER = 201;
    private final static int VIEW_TYPE_REVIEW_HEADER = 300;
    private final static int VIEW_TYPE_REVIEW = 301;

    private final Movie mMovie;
    private final Context mContext;

    private final OnTrailerClickListener mOnTrailerClick;
    private final OnReviewClickListener mOnReviewClick;

    private List<Trailer> mTrailers;
    private List<Review> mReviews;

    MovieDetailsAdapter(@NonNull Context context,
                        @NonNull Movie movie,
                        @NonNull OnTrailerClickListener onTrailerClick,
                        @NonNull OnReviewClickListener onReviewClick) {
        mContext = context;
        mMovie = movie;
        mOnTrailerClick = onTrailerClick;
        mOnReviewClick = onReviewClick;
    }

    @NonNull
    @Override
    public MovieDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        int layoutId;

        // based on the view type we're dealing with different layouts
        switch (viewType) {
            case VIEW_TYPE_MOVIE_INFO:
                layoutId = R.layout.movie_info_item;
            break;

            case VIEW_TYPE_TRAILER_HEADER:
            case VIEW_TYPE_REVIEW_HEADER:
                layoutId = R.layout.section_header_item;
                break;

            case VIEW_TYPE_TRAILER:
                layoutId = R.layout.trailer_item;
                break;

            case VIEW_TYPE_REVIEW:
                layoutId = R.layout.review_item;
                break;

            default:
                throw new IllegalArgumentException("Invalid view type: " + viewType);
        }

        View view = LayoutInflater.from(mContext)
                .inflate(layoutId, viewGroup, false);

        return new MovieDetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieDetailsViewHolder viewHolder, int position) {
        int viewType = getItemViewType(position);

        // based on the view type we're dealing with different layouts,
        // so we need to deal with different UI controls
        switch (viewType) {
            case VIEW_TYPE_MOVIE_INFO:
                viewHolder.mMovieTitleTextView.setText(mMovie.getTitle());
                viewHolder.mMovieUserRatingTextView.setText(
                        mContext.getString(R.string.user_rating, String.valueOf(mMovie.getUserRating())));
                viewHolder.mMovieReleaseDateTextView.setText(
                        mContext.getString(R.string.release_date, mMovie.getReleaseDate()));
                viewHolder.mMovieSynopsisTextView.setText(mMovie.getSynopsis());
                break;

            case VIEW_TYPE_TRAILER_HEADER:
                viewHolder.mSectionHeaderTextView.setText(mContext.getString(R.string.section_header_trailers));
                break;

            case VIEW_TYPE_TRAILER:
                renderTrailerView(viewHolder, position);
                break;

            case VIEW_TYPE_REVIEW_HEADER:
                viewHolder.mSectionHeaderTextView.setText(mContext.getString(R.string.section_header_reviews));
                break;

            case VIEW_TYPE_REVIEW:
                renderReviewView(viewHolder, position);
                break;

            default:
                throw new IllegalArgumentException("Invalid view type: " + viewType);
        }
    }

    @Override
    public int getItemCount() {
        return  getTrailersViewCount() + getReviewsViewCount() +
                3; // 3 comes from: 1 movie info + trailers header + reviews header
    }

    @Override
    public int getItemViewType(int position) {
        int trailersViewCount = getTrailersViewCount();

        // the view type is dictated by the items position

        // 1st comes the basic movie info
        if (position == 0) {
            return VIEW_TYPE_MOVIE_INFO;
        }

        // ... then a section header for trailers
        if (position == 1) {
            return VIEW_TYPE_TRAILER_HEADER;
        }

        // ... then the trailer list
        if (position < trailersViewCount + 2) {
            return VIEW_TYPE_TRAILER;
        }

        // .. then the section header for reviews
        if (position == trailersViewCount + 2) {
            return VIEW_TYPE_REVIEW_HEADER;
        }

        // ... then the review list
        return VIEW_TYPE_REVIEW;
    }

    /**
     * Updates the list of trailers and forces a UI refresh.
     *
     * @param trailers the updated list of trailers
     */
    void setTrailerList(List<Trailer> trailers) {
        mTrailers = trailers;
        notifyDataSetChanged();
    }

    /**
     * Updates the list of reviews and forces a UI refresh.
     *
     * @param reviews the updated list of reviews
     */
    void setReviewList(List<Review> reviews) {
        mReviews = reviews;
        notifyDataSetChanged();
    }

    /**
     * Helper method for rendering an item in the list of trailers.
     */
    private void renderTrailerView(@NonNull MovieDetailsViewHolder viewHolder, int position) {
        if (mTrailers == null) {
            // when the list of trailers is null, that means we're still loading data
            // (either from local DB or network)
            viewHolder.mTrailersLoadingProgressBar.setVisibility(View.VISIBLE);
            viewHolder.mTrailerMovieIconImageView.setVisibility(View.GONE);
        } else if (mTrailers.isEmpty()) {
            // there are no trailers to show; just display a message
            viewHolder.mTrailersLoadingProgressBar.setVisibility(View.GONE);
            viewHolder.mTrailerMovieIconImageView.setVisibility(View.INVISIBLE);

            viewHolder.mTrailerTitleTextView.setText(R.string.no_trailers_available);
        } else {
            // now we have something to display
            int trailersOffset = 2; // 2 comes from: movie info + trailer section header
            final Trailer trailer = mTrailers.get(position - trailersOffset);

            viewHolder.mTrailersLoadingProgressBar.setVisibility(View.GONE);
            viewHolder.mTrailerMovieIconImageView.setVisibility(View.VISIBLE);

            viewHolder.mTrailerTitleTextView.setText(trailer.getName());

            // make the trailer item "clickable"
            viewHolder.mTrailerContainerLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnTrailerClick.onTrailerClick(trailer);
                }
            });
        }
    }

    /**
     * Helper method for rendering an item in the list of reviews.
     */
    private void renderReviewView(@NonNull MovieDetailsViewHolder viewHolder, int position) {
        if (mReviews == null) {
            // when the list of reviews is null, that means we're still loading data
            // (either from local DB or network)
            viewHolder.mReviewsLoadingProgressBar.setVisibility(View.VISIBLE);
            viewHolder.mReviewAuthorIconImageView.setVisibility(View.GONE);
        } else if (mReviews.isEmpty()) {
            // there are no reviews to show; just display a message
            viewHolder.mReviewsLoadingProgressBar.setVisibility(View.GONE);
            viewHolder.mReviewAuthorIconImageView.setVisibility(View.INVISIBLE);

            viewHolder.mReviewAuthorTextView.setText(R.string.no_reviews_available);
        } else {
            // now we have something to display

            // 3 comes from: movie info + trailer section header + review section header
            int reviewsOffset = 3 + getTrailersViewCount();

            final Review review = mReviews.get(position - reviewsOffset);

            viewHolder.mReviewsLoadingProgressBar.setVisibility(View.GONE);
            viewHolder.mReviewAuthorIconImageView.setVisibility(View.VISIBLE);

            viewHolder.mReviewAuthorTextView.setText(review.getAuthor());
            viewHolder.mReviewContentTextView.setText(review.getContent());

            // make the review items "clickable"
            viewHolder.mReviewContainerLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnReviewClick.onReviewClick(review);
                }
            });
        }
    }

    private int getTrailersViewCount() {
        // when there are no trailers to render, the view count is still 1
        // (to display the loading indicator or the "no trailers" message)
        if (mTrailers == null || mTrailers.isEmpty()) return 1;

        return mTrailers.size();
    }

    private int getReviewsViewCount() {
        // when there are no reviews to render, the view count is still 1
        // (to display the loading indicator or the "no reviews" message)
        if (mReviews == null || mReviews.isEmpty()) return 1;

        return mReviews.size();
    }

    // implements the view-holder pattern
    class MovieDetailsViewHolder extends RecyclerView.ViewHolder {
        private final TextView mMovieTitleTextView;
        private final TextView mMovieUserRatingTextView;
        private final TextView mMovieReleaseDateTextView;
        private final TextView mMovieSynopsisTextView;

        private final TextView mSectionHeaderTextView;

        private final ViewGroup mTrailerContainerLayout;
        private final ImageView mTrailerMovieIconImageView;
        private final TextView mTrailerTitleTextView;
        private final ProgressBar mTrailersLoadingProgressBar;

        private final ViewGroup mReviewContainerLayout;
        private final ImageView mReviewAuthorIconImageView;
        private final TextView mReviewAuthorTextView;
        private final TextView mReviewContentTextView;
        private final ProgressBar mReviewsLoadingProgressBar;

        MovieDetailsViewHolder(@NonNull View view) {
            super(view);

            // here we initialize all the UI controls in all layouts
            // knowing full well that some of them will be null

            // we are relying on the proper computation of view types
            // to avoid triggering a NPE at runtime

            mMovieTitleTextView = view.findViewById(R.id.tv_title);
            mMovieUserRatingTextView = view.findViewById(R.id.tv_user_rating);
            mMovieReleaseDateTextView = view.findViewById(R.id.tv_release_date);
            mMovieSynopsisTextView = view.findViewById(R.id.tv_synopsis);

            mSectionHeaderTextView = view.findViewById(R.id.tv_section_header);

            mTrailerContainerLayout = view.findViewById(R.id.layout_trailer_container);
            mTrailerMovieIconImageView = view.findViewById(R.id.iv_movie_icon);
            mTrailerTitleTextView = view.findViewById(R.id.tv_trailer_title);
            mTrailersLoadingProgressBar = view.findViewById(R.id.pb_trailers_loading);

            mReviewContainerLayout = view.findViewById(R.id.layout_review_container);
            mReviewAuthorIconImageView = view.findViewById(R.id.iv_author_icon);
            mReviewAuthorTextView = view.findViewById(R.id.tv_author);
            mReviewContentTextView = view.findViewById(R.id.tv_content);
            mReviewsLoadingProgressBar = view.findViewById(R.id.pb_reviews_loading);
        }
    }

    /**
     * Protocol for providing custom logic for handling user taps on trailer items.
     */
    interface OnTrailerClickListener {
        void onTrailerClick(Trailer trailer);
    }

    /**
     * Protocol for providing custom logic for handling user taps on review items.
     */
    interface OnReviewClickListener {
        void onReviewClick(Review review);
    }
}
