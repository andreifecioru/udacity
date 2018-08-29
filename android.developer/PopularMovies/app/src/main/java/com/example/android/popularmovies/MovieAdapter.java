package com.example.android.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * RecyclerView adapter for the {@link Movie} class. Fuels the grid-view
 * which displays the list of movie posters.
 */
public class MovieAdapter
        extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> mMovies;

    private final OnMoviePosterClick mOnMoviePosterClick;

    private final OnMovieFavouriteClick mOnMovieFavouriteClick;

    /** Produces an instance of the {@link MovieAdapter} class (constructor)
     *
     * @param movies A list of {@link Movie} objects (the raw data source)
     * @param onMoviePosterClick Implementation of the {@link OnMoviePosterClick} interface
     *                           where you can specify custom logic for clicking a movie item.
     */
    MovieAdapter(List<Movie> movies,
                 OnMoviePosterClick onMoviePosterClick,
                 OnMovieFavouriteClick onMovieFavouriteClick) {
        mMovies = movies;
        mOnMoviePosterClick = onMoviePosterClick;
        mOnMovieFavouriteClick = onMovieFavouriteClick;
    }

    /** Updates the movie list (the underlying data source).
     *
     * @param movies A list of {@link Movie} objects (the raw data source)
     */
    public void setMovies(List<Movie> movies) {
        mMovies = movies;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for the movie-poster item.
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View root = inflater.inflate(R.layout.poster_item, parent, false);

        // Return the view holder
        return new MovieViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        // Get the movie at position
        Movie movie = mMovies.get(position);

        // Bind data to the view-holder
        holder.bindMovie(movie);
    }

    @Override
    public int getItemCount() {
        if (mMovies == null) return 0;

        return mMovies.size();
    }

    // Implements the view-holder pattern.
    class MovieViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private final Context mContext;

        private final ImageView mPosterImageView;
        private final TextView mTitleTextView;
        private final ViewGroup mViewGroupRatingContainer;
        private final ImageView mFavouriteImageView;
        private final TextView mPopularityTextView;

        private Movie mMovie;

        MovieViewHolder(View root) {
            super(root);

            mContext = root.getContext();

            // Install on-click handler
            root.setOnClickListener(this);

            // Cache UI controls
            mPosterImageView = root.findViewById(R.id.iv_poster);
            mTitleTextView = root.findViewById(R.id.tv_title);
            mViewGroupRatingContainer = root.findViewById(R.id.rating_container);
            mFavouriteImageView = root.findViewById(R.id.iv_favourite);
            mPopularityTextView = root.findViewById(R.id.tv_popularity);
        }

        @Override
        public void onClick(View v) {
            mOnMoviePosterClick.onMovieClick(mMovie);
        }

        // Binding method.
        void bindMovie(Movie movie) {
            // Sanity check: fast exit
            if (movie == null) return;

            mMovie = movie;

            // Display the movie title
            mTitleTextView.setText(mMovie.getTitle());

            // Display the user rating
            showRatingStars();

            // Display the popularity score
            mPopularityTextView.setText(String.valueOf(mMovie.getPopularity()));

            // Render the movie poster
            String posterUrl = mMovie.getPosterUrl();
            if (TextUtils.isEmpty(posterUrl)) {
                mPosterImageView.setImageResource(R.drawable.poster_placeholder);
            } else {
                Picasso.with(mContext)
                        .load(mMovie.getPosterUrl())
                        .placeholder(R.drawable.poster_placeholder)
                        .error(R.drawable.poster_placeholder)
                        .into(mPosterImageView);
            }

            // Display the "favourite" status
            displayFavouriteStatus();

            mFavouriteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMovie.toggleIsFavourite();
                    displayFavouriteStatus();
                    mOnMovieFavouriteClick.onFavouriteClick(mMovie);
                }
            });
        }

        private void displayFavouriteStatus() {
            if (mMovie.getIsFavourite()) {
                mFavouriteImageView.setImageResource(android.R.drawable.star_big_on);
            } else {
                mFavouriteImageView.setImageResource(android.R.drawable.star_big_off);
            }
        }

        // Shows rating stars on a 1-5 scale (the original scale of 0-10 is cut in half).
        private void showRatingStars() {
            int i;

            long starCount = Math.round(mMovie.getUserRating() / 2.0);

            // show full stars up to the rating value
            for (i = 0; i < starCount; i++) {
                ImageView imgView = (ImageView) mViewGroupRatingContainer.getChildAt(i);
                imgView.setImageResource(android.R.drawable.star_on);
            }

            // all the remaining stars are empty
            for (; i< Movie.MAX_RATING; i++) {
                ImageView imgView = (ImageView) mViewGroupRatingContainer.getChildAt(i);
                imgView.setImageResource(android.R.drawable.star_off);
            }
        }
    }

    /**
     * Protocol for providing custom logic for handling user taps on movie items.
     */
    interface OnMoviePosterClick {
        void onMovieClick(Movie movie);
    }

    /**
     * Protocol for providing custom logic for handling the toggle of the movie's
     * favourite status.
     */
    interface OnMovieFavouriteClick {
        void onFavouriteClick(Movie movie);
    }
}
