package com.udacity.gradle.builditbigger.ui;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.FragmentActivity;

import com.udacity.gradle.builditbigger.api.JokesApi;
import com.android.example.jokedisplay.models.Joke;
import com.udacity.gradle.builditbigger.utils.ApiResponse;
import com.udacity.gradle.builditbigger.utils.Resource;

import javax.inject.Inject;

/**
 * A {@link ViewModel} implementation used by the {@link MainActivity}.
 *
 * Provides access to the {@link JokesApi} services.
 */
public class MainActivityViewModel extends ViewModel {
    private JokesApi mJokesApi;

    public static MainActivityViewModel create (FragmentActivity activity) {
        return ViewModelProviders.of(activity).get(MainActivityViewModel.class);
    }

    @Inject
    void setJokesApi(JokesApi jokesApi) {
        mJokesApi = jokesApi;
    }

    LiveData<Resource<Joke>> getJoke() {
        return Transformations.map(mJokesApi.getJoke(), new Function<ApiResponse<Joke>, Resource<Joke>>() {
            @Override
            public Resource<Joke> apply(ApiResponse<Joke> response) {
                return response.isSuccessful()
                        ? Resource.success(response.getBody())
                        : Resource.<Joke>error(response.getErrorMessage(), null);
            }
        });
    }
}
