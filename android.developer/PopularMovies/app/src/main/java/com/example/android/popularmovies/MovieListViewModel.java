package com.example.android.popularmovies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.FragmentActivity;

import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.repo.MovieRepository;
import com.example.android.popularmovies.utils.Resource;

import java.util.List;

import javax.inject.Inject;


/**
 * ViewModel class for the {@link ListMoviesActivity} activity.
 *
 * Uses the repository pattern as single source of truth for movie data.
 *
 * Exposes various movie info wrapped inside {@link LiveData} structures.
 */
public class MovieListViewModel extends ViewModel {
    private MovieRepository mMovieRepository;

    public static MovieListViewModel create (FragmentActivity activity) {
        return ViewModelProviders.of(activity).get(MovieListViewModel.class);
    }

    @Inject
    public void setMovieRepository(MovieRepository repository) {
        mMovieRepository = repository;
    }

    public LiveData<Resource<List<Movie>>> getMovies(MovieRepository.SortBy sortBy) {
        return mMovieRepository.getMovieList(sortBy);
    }

    public void updateMovie(Movie movie) {
        mMovieRepository.updateMovie(movie);
    }
}
