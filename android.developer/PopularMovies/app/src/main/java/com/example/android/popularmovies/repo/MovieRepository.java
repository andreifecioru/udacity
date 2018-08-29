package com.example.android.popularmovies.repo;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.example.android.popularmovies.dao.MovieDao;
import com.example.android.popularmovies.dao.ReviewDao;
import com.example.android.popularmovies.dao.TrailerDao;
import com.example.android.popularmovies.di.module.NetModule;
import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.models.MovieList;
import com.example.android.popularmovies.models.Review;
import com.example.android.popularmovies.models.ReviewList;
import com.example.android.popularmovies.models.Trailer;
import com.example.android.popularmovies.models.TrailerList;
import com.example.android.popularmovies.services.moviedb.MovieDbApi;
import com.example.android.popularmovies.utils.ApiResponse;
import com.example.android.popularmovies.utils.NetworkBoundResource;
import com.example.android.popularmovies.utils.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * This class implements the "repository" abstraction for movies (and
 * related entities) as described in the AAC official docs. It is the
 * "single source of truth" for all movie-related info.
 *
 * See: https://developer.android.com/jetpack/docs/guide
 */
@Singleton
public class MovieRepository {
    private static final String LOG_TAG = MovieRepository.class.getSimpleName();

    /**
     * Enum for representing our three movie sorting criteria.
     */
    public enum SortBy {
        TOP_RATED("top_rated"),
        MOST_POPULAR("popular"),
        FAVOURITES("favourites");

        private final String mQueryString;

        SortBy(String queryString) {
            mQueryString = queryString;
        }

        String getQueryString() { return mQueryString; }

        public static SortBy fromString(String text) {
            for (SortBy sortBy : SortBy.values()) {
                if (sortBy.mQueryString.equals(text)) {
                    return sortBy;
                }
            }

            return null;
        }
    }

    // this is where we store the various query params (like the API key)
    private Map<String, String> mOptions = new HashMap<>();

    // access DB via DAOs
    private final MovieDbApi mMovieDbApi;
    private final MovieDao mMovieDao;
    private final TrailerDao mTrailerDao;
    private final ReviewDao mReviewDao;

    // access the DB on a background thread
    private final Executor mExecutor;

    /* We keep track whether we should download the popular and top-rated movie list.

       Since we cache the movie data in our local DB, we want to avoid to re-download the
       raw movie data.

       NOTE: we are downloading data only once, but we could improve this implementation
       by attaching a TTL (i.e. expiration data on the data we store in the DB). */
    private boolean mShouldFetchPopularMovies = true;
    private boolean mShouldFetchTopRatedMovies = true;

    @Inject
    MovieRepository(MovieDbApi movieDbApi, Executor executor,
                    MovieDao movieDao, TrailerDao trailerDao, ReviewDao reviewDao) {
        mMovieDbApi = movieDbApi;
        mMovieDao = movieDao;
        mExecutor = executor;
        mTrailerDao = trailerDao;
        mReviewDao = reviewDao;
        mOptions.put("api_key", NetModule.MOVIE_DB_API_KEY);
    }

    @WorkerThread
    public void updateMovie(final Movie movie) {
        // we update the movie info on a background thread.
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mMovieDao.updateMovie(movie);
            }
        });
    }

    /**
     * We fetch the trailer list using the {@link NetworkBoundResource} implementation.
     *
     * This allows us to download the trailers only once per movie and then hit the DB cache
     * multiple times when needed. This also provides the off-line support we want.
     */
    public LiveData<Resource<List<Trailer>>> getTrailerList(final long movieId) {
        return new NetworkBoundResource<List<Trailer>, TrailerList>() {

            @Override
            protected void saveCallResult(@NonNull TrailerList data) {
                // Set the movieId for incoming trailers
                // in order to satisfy the ForeignKey DB constraint
                for (Trailer trailer : data.getTrailers()) {
                    trailer.setMovieId(movieId);
                }

                Trailer[] arr = new Trailer[data.getTrailers().size()];
                data.getTrailers().toArray(arr);
                mTrailerDao.insertTrailers(arr);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Trailer> data) {
                // we fetch data when there is no data cached locally
                if ((data == null) || data.isEmpty()) {
                    Log.d(LOG_TAG, "No data stored locally. Fetch from network");
                    return true;
                }

                return false;
            }

            @NonNull
            @Override
            protected LiveData<List<Trailer>> loadFromDb() {
                return mTrailerDao.loadTrailersForMovie(movieId);
            }

            @Override
            protected LiveData<ApiResponse<TrailerList>> createCall() {
                return mMovieDbApi.getTrailerList(movieId, mOptions);
            }
        }.getAsLiveData();
    }

    /**
     * We fetch the review list using the {@link NetworkBoundResource} implementation.
     *
     * This allows us to download the reviews only once per movie and then hit the DB cache
     * multiple times when needed. This also provides the off-line support we want.
     */
    public LiveData<Resource<List<Review>>> getReviewList(final long movieId) {
        return new NetworkBoundResource<List<Review>, ReviewList>() {

            @Override
            protected void saveCallResult(@NonNull ReviewList data) {
                // Set the movieId for incoming reviews
                // in order to satisfy the ForeignKey DB constraint
                for (Review review: data.getReviews()) {
                    review.setMovieId(movieId);
                }

                Review [] arr = new Review[data.getReviews().size()];
                data.getReviews().toArray(arr);
                mReviewDao.insertReviews(arr);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Review> data) {
                // we fetch data when there is no data cached locally
                if ((data == null) || data.isEmpty()) {
                    Log.d(LOG_TAG, "No data stored locally. Fetch from network");
                    return true;
                }

                return false;
            }

            @NonNull
            @Override
            protected LiveData<List<Review>> loadFromDb() {
                return mReviewDao.loadReviewsForMovie(movieId);
            }

            @Override
            protected LiveData<ApiResponse<ReviewList>> createCall() {
                return mMovieDbApi.getReviewList(movieId, mOptions);
            }
        }.getAsLiveData();
    }

    /**
     * We fetch the movie list using the {@link NetworkBoundResource} implementation.
     *
     * This allows us to download the reviews only once and then hit the DB cache
     * multiple times when needed. This also provides the off-line support we want.
     *
     * NOTE: when in off-line mode, the movie info will be available but not the poster
     * images (we don't store those in the local DB).
     */
    public LiveData<Resource<List<Movie>>> getMovieList(final SortBy sortBy) {
        return new NetworkBoundResource<List<Movie>, MovieList>() {
            @Override
            protected void saveCallResult(@NonNull MovieList data) {
                // Set the sort_by field for the incoming movies
                for (Movie movie : data.getMovies()) {
                    movie.setSortBy(sortBy.getQueryString());
                }

                // Clear the DB content corresponding to the sortBy criterion
                mMovieDao.deleteAllMovies(sortBy.getQueryString());

                Movie[] arr = new Movie[data.getMovies().size()];
                data.getMovies().toArray(arr);
                mMovieDao.insertMovies(arr);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Movie> data) {
                // we fetch data when there is no data cached locally
                if ((data == null) || data.isEmpty()) {
                    Log.d(LOG_TAG, "No data stored locally. Fetch from network");
                    return true;
                }

                boolean shouldFetch;
                switch (sortBy) {
                    case TOP_RATED:
                        shouldFetch = mShouldFetchTopRatedMovies;
                        // we're only fetching the top rated movies once.
                        mShouldFetchTopRatedMovies = false;
                        break;

                    case MOST_POPULAR:
                        shouldFetch = mShouldFetchPopularMovies;
                        // we're only fetching the popular rated movies once.
                        mShouldFetchPopularMovies = false;
                        break;

                    case FAVOURITES:
                        shouldFetch = false;
                        break;

                    default:
                        throw new IllegalArgumentException("Invalid sortBy criterion: " + sortBy);
                }

                return shouldFetch;
            }

            @NonNull
            @Override
            protected LiveData<List<Movie>> loadFromDb() {
                Log.d(LOG_TAG, "Loading data from DB.");

                // load the appropriate movie list based on the sort criteria
                switch (sortBy) {
                    case TOP_RATED:
                        return mMovieDao.loadTopRatedMovies();
                    case MOST_POPULAR:
                        return mMovieDao.loadPopularMovies();
                    case FAVOURITES:
                        return mMovieDao.loadFavouriteMovies();
                    default:
                        throw new IllegalArgumentException("Invalid sortBy criterion: " + sortBy);
                }
            }

            @Override
            protected LiveData<ApiResponse<MovieList>> createCall() {
                Log.d(LOG_TAG, "Fetching data from network.");

                switch (sortBy) {
                    case TOP_RATED:
                    case MOST_POPULAR:
                       return mMovieDbApi.getMovieList(sortBy.getQueryString(), mOptions);
                    case FAVOURITES:
                        // for the FAVOURITES sort criterion we are working only with local data
                        // so there is no API call to be made.
                        return null;
                    default:
                        throw new IllegalArgumentException("Invalid sortBy criterion: " + sortBy);
                }
            }

            @Override
            protected void onFetchFailed(String errMsg) {
                Log.d(LOG_TAG, "Failed to fetch data from network: " + errMsg);
            }
        }.getAsLiveData();
    }
}


