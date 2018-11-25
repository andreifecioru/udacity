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
 * A {@link ViewModel} implementation used by the {@link RecipeListActivity}.
 *
 * Provides access to the {@link RecipeRepository} services.
 */
public class RecipeListViewModel extends ViewModel {
    private RecipeRepository mRecipeRepository;

    public static RecipeListViewModel create (FragmentActivity activity) {
        return ViewModelProviders.of(activity).get(RecipeListViewModel.class);
    }

    @Inject
    public void setRecipeRepository(RecipeRepository repository) {
        mRecipeRepository = repository;
    }

    public LiveData<Resource<List<Recipe>>> getRecipes() {
        return mRecipeRepository.getRecipeList();
    }

    public void deleteAllRecipes(Runnable onComplete) {
        mRecipeRepository.deleteAllRecipes(onComplete);
    }
}
