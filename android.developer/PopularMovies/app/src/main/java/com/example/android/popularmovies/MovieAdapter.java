package com.example.android.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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

    /** Produces an instance of the {@link MovieAdapter} class (constructor)
     *
     * @param movies A list of {@link Movie} objects (the raw data source)
     * @param onMoviePosterClick Implementation of the {@link OnMoviePosterClick} interface
     *                           where you can specify custom logic for clicking a movie item.
     */
    MovieAdapter(List<Movie> movies,
                 OnMoviePosterClick onMoviePosterClick) {
        mMovies = movies;
        mOnMoviePosterClick = onMoviePosterClick;
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

        private Movie mMovie;

        MovieViewHolder(View root) {
            super(root);

            mContext = root.getContext();

            // Install on-click handler
            root.setOnClickListener(this);

            // Cache UI controls
            mPosterImageView = root.findViewById(R.id.iv_poster);
            mTitleTextView = root.findViewById(R.id.tv_title);
        }

        @Override
        public void onClick(View v) {
            mOnMoviePosterClick.onClick(mMovie);
        }

        // Binding method.
        void bindMovie(Movie movie) {
            // Sanity check: fast exit
            if (movie == null) return;

            mMovie = movie;

            // Display the movie title
            mTitleTextView.setText(mMovie.getTitle());

            // Render the movie poster
            Picasso.with(mContext)
                    .load(mMovie.getPosterUrl())
                    .placeholder(R.drawable.poster_placeholder)
                    .into(mPosterImageView);
        }
    }

    /**
     * Protocol for providing custom logic for handling user taps on movie items.
     */
    interface OnMoviePosterClick {
        void onClick(Movie movie);
    }
}
