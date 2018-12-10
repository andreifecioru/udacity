package com.udacity.gradle.builditbigger.api;

import android.arch.lifecycle.LiveData;

import com.android.example.jokedisplay.models.Joke;
import com.udacity.gradle.builditbigger.utils.ApiResponse;

import retrofit2.http.POST;

public interface JokesApi {
    @POST("_ah/api/jokes/v1/tell")
    LiveData<ApiResponse<Joke>> getJoke();
}
