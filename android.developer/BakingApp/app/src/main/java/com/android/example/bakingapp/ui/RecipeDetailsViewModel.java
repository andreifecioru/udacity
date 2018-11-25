package com.android.example.bakingapp.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.FragmentActivity;

import com.android.example.bakingapp.models.Recipe;
import com.android.example.bakingapp.repo.RecipeRepository;
import com.android.example.bakingapp.utils.Resource;

import java.util.List;

import javax.inject.Inject;

/**
 * A {@link ViewModel} implementation used by the {@link RecipeDetailsActivity}.
 *
 * Provides access to the {@link RecipeRepository} services.
 */
public class RecipeDetailsViewModel extends ViewModel {
    private RecipeRepository mRecipeRepository;

    public static RecipeDetailsViewModel create (FragmentActivity activity) {
        return ViewModelProviders.of(activity).get(RecipeDetailsViewModel.class);
    }

    @Inject
    void setRecipeRepository(RecipeRepository repository) {
        mRecipeRepository = repository;
    }

    LiveData<Recipe> getRecipe(long recipeId) {
        return mRecipeRepository.getRecipe(recipeId);
    }
}
