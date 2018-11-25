package com.android.example.bakingapp.repo;

import javax.inject.Inject;
import javax.inject.Singleton;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.android.example.bakingapp.utils.ApiResponse;
import com.android.example.bakingapp.utils.NetworkBoundResource;
import com.android.example.bakingapp.utils.Resource;
import com.android.example.bakingapp.api.RecipeApi;
import com.android.example.bakingapp.dao.*;
import com.android.example.bakingapp.models.*;
import com.android.example.bakingapp.ui.RecipeListActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;


/**
 * This class implements the "repository" abstraction for recipes (and
 * related entities) as described in the AAC official docs. It is the
 * "single source of truth" for all recipe-related info.
 *
 * See: https://developer.android.com/jetpack/docs/guide
 */
@Singleton
public class RecipeRepository {
    private static final String LOG_TAG = RecipeRepository.class.getSimpleName();

    private final RecipeApi mRecipeApi;

    // access DB via DAOs
    private final RecipeDao mRecipeDao;
    private final IngredientDao mIngredientDao;
    private final StepDao mStepDao;

    // access the DB on a background thread
    private final Executor mExecutor;

    @Inject
    RecipeRepository(RecipeApi recipeApi, Executor executor, Application application,
                     RecipeDao recipeDao, IngredientDao ingredientDao, StepDao stepDao) {
        mRecipeApi = recipeApi;

        mRecipeDao = recipeDao;
        mIngredientDao = ingredientDao;
        mStepDao = stepDao;

        mExecutor = executor;
    }

    /**
     * This is actually a helper method used only by the instrumented UI tests.
     *
     * It is called by the {@link RecipeListActivity} only when the idling resource
     * is setup (which happens only during the running of the tests).
     *
     * @param onComplete callback invoked when the operation is complete.
     * */
    @WorkerThread
    public void deleteAllRecipes(final Runnable onComplete) {
        // we delete all recipes on a background thread.
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mRecipeDao.deleteAllRecipes();
                onComplete.run();
            }
        });
    }

    /**
     * We fetch the recipe list using the {@link NetworkBoundResource} implementation.
     *
     * This allows us to download the recipes only once and then hit the DB cache
     * multiple times when needed. This also provides the off-line support we want.
     */
    public LiveData<Resource<List<Recipe>>> getRecipeList() {
        return new NetworkBoundResource<List<Recipe>, List<Recipe>>() {

            @Override
            protected void saveCallResult(@NonNull List<Recipe> recipes) {
                Log.d(LOG_TAG, "Saving data to the DB (" + recipes.size() +" entries).");
                // Set the recipeId for incoming ingredients & steps
                // in order to satisfy the ForeignKey DB constraints
                List<Ingredient> allIngredients = new ArrayList<>();
                List<Step> allSteps = new ArrayList<>();
                for (Recipe recipe: recipes) {
                    long recipeId = recipe.getId();

                    for (Ingredient ingredient: recipe.getIngredients()) {
                        ingredient.setRecipeId(recipeId);
                        allIngredients.add(ingredient);
                    }

                    for (Step step: recipe.getSteps()) {
                        step.setRecipeId(recipeId);
                        allSteps.add(step);
                    }
                }

                // Perform DB insertions
                Recipe[] arrRecipes = new Recipe[recipes.size()];
                recipes.toArray(arrRecipes);
                mRecipeDao.insertRecipes(arrRecipes);

                Ingredient[] arrIngredients = new Ingredient[allIngredients.size()];
                allIngredients.toArray(arrIngredients);
                Log.d(LOG_TAG, "Total number of ingredients: " + arrIngredients.length);
                mIngredientDao.insertIngredients(arrIngredients);

                Step[] arrSteps = new Step[allSteps.size()];
                allSteps.toArray(arrSteps);
                Log.d(LOG_TAG, "Total number of steps: " + arrSteps.length);
                mStepDao.insertSteps(arrSteps);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Recipe> recipes) {
                // we fetch data when there is no data cached locally
                if ((recipes == null) || recipes.isEmpty()) {
                    Log.d(LOG_TAG, "No data stored locally. Fetch from network");
                    return true;
                }

                return false;
            }

            @NonNull
            @Override
            protected LiveData<List<Recipe>> loadFromDb() {
                Log.d(LOG_TAG, "Loading data from DB.");

                final MediatorLiveData<List<Recipe>> result = new MediatorLiveData<>();
                result.addSource(mRecipeDao.loadRecipes(), new Observer<List<Recipe>>() {
                    @Override
                    public void onChanged(@Nullable final List<Recipe> recipes) {
                        if (recipes == null) {
                            result.setValue(null);
                            return;
                        }

                        for (final Recipe recipe : recipes) {
                            ingredientsAndStepsForRecipe(result, recipes, recipe);
                        }

                        result.setValue(recipes);
                    }
                });

                return  result;
            }

            @Override
            protected LiveData<ApiResponse<List<Recipe>>> createCall() {
                return mRecipeApi.getRecipeList();
            }
        }.getAsLiveData();
    }

    /**
     * Fetch the recipe with a specified ID and wrap the result in LiveData.
     * Also fetches the ingredients and recipe-steps for the said recipe.
     *
     * @param recipeId the ID of the recipe we are fetching.
     */
    public LiveData<Recipe> getRecipe(long recipeId) {
        final MediatorLiveData<Recipe> result = new MediatorLiveData<>();

        result.addSource(mRecipeDao.getRecipe(recipeId), new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                if (recipes == null || recipes.isEmpty()) {
                    result.setValue(null);
                    return;
                }

                Recipe recipe = recipes.get(0);
                ingredientsAndStepsForRecipe(result, recipe, recipe);
            }
        });

        return result;
    }

    /**
     * Private helper method that allows us to merge a query for recipes with 2 additional
     * "related-to" queries for retrieving the ingredients and recipe-step associated to a
     * particular recipe.
     *
     * @param result the LiveData result into which we are merging the 2 additional queries (can be either a
     *               list of recipes or a single recipe)
     * @param value what we are updating (either a list of recipes or a recipe)
     * @param recipe the recipe for which we are performing the 2 "related-to" SQL queries.
     * */
    private <T> void ingredientsAndStepsForRecipe(final MediatorLiveData<T> result,
                                                  final T value,
                                                  final Recipe recipe) {
        // We need to merge the results of 3 type of queries (wrapped inside LiveData structures):
        //  - the list of recipes
        //  - for each recipe:
        //    - the list of ingredients
        //    - the list of steps
        final long recipeId = recipe.getId();
        result.addSource(mIngredientDao.loadIngredientsForRecipe(recipeId), new Observer<List<Ingredient>>() {
            @Override
            public void onChanged(@Nullable List<Ingredient> ingredients) {
                Log.d(LOG_TAG, "Adding ingredients to recipe " + recipeId + ": " + ingredients.size());
                recipe.setIngredients(ingredients);
                result.setValue(value);
            }
        });

        result.addSource(mStepDao.loadStepsForRecipe(recipeId), new Observer<List<Step>>() {
            @Override
            public void onChanged(@Nullable List<Step> steps) {
                Log.d(LOG_TAG, "Adding steps to recipe " + recipeId + ": " + steps.size());
                recipe.setSteps(steps);
                result.setValue(value);
            }
        });
    }
}
