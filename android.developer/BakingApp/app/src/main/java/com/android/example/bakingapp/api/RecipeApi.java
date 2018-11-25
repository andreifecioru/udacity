package com.android.example.bakingapp.api;

import java.util.List;

import android.arch.lifecycle.LiveData;

import retrofit2.http.GET;

import com.android.example.bakingapp.models.Recipe;
import com.android.example.bakingapp.utils.ApiResponse;


/**
 * Definition of the recipes web API as required by the Retrofit lib.
 */
public interface RecipeApi {
    @GET("baking.json")
    LiveData<ApiResponse<List<Recipe>>> getRecipeList();
}
