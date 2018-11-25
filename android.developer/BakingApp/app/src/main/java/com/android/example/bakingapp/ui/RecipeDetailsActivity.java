package com.android.example.bakingapp.ui;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.android.example.bakingapp.R;
import com.android.example.bakingapp.models.Ingredient;
import com.android.example.bakingapp.models.Recipe;
import com.android.example.bakingapp.models.Step;
import com.android.example.bakingapp.service.BakingTimeWidgetService;

import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.android.example.bakingapp.ui.BakingTimeWidget.INGREDIENT_SET_KEY;
import static com.android.example.bakingapp.ui.BakingTimeWidget.RECIPE_ID_KEY;
import static com.android.example.bakingapp.ui.BakingTimeWidget.RECIPE_NAME_KEY;
import static com.android.example.bakingapp.ui.RecipeDetailsFragment.RECIPE_ARGS_KEY;
import static com.android.example.bakingapp.ui.StepDetailsActivity.INTENT_EXTRA_STEP_INDEX_KEY;
import static com.android.example.bakingapp.ui.StepDetailsFragment.STEP_ARGS_KEY;
import static com.android.example.bakingapp.ui.StepDetailsFragment.STEP_ARGS_TABLET_MODE_KEY;


/**
 * Implements the "recipe-details" screen displaying the information about the selected recipe.
 *
 * It is a "dual-mode" activity, behaving differently when in mobile vs. tablet mode (implements
 * the master-detail navigation pattern).
 *
 * Mobile mode:
 *  - a fragment containing a recycler view displaying the list of ingredients and the list of
 *    preparation steps
 *  - when tapping a step item, another activity is launched displaying the info about the selected
 *    preparation step (see {@link StepDetailsActivity}).
 *
 * Tablet mode:
 *  - a fragment containing a recycler view displaying the list of ingredients and the list of
 *    preparation steps
 *  - fragment containing the information about the currently selected step (the video and
 *    instructions below)
 *  - tapping on a specific preparation step (on the left side) will update the step-details section
 *    (on the right)
 * */
public class RecipeDetailsActivity
        extends BaseActivity
        implements RecipeDetailsFragment.Protocol {
    private static final String LOG_TAG = RecipeDetailsActivity.class.getSimpleName();

    public static final String INTENT_EXTRA_RECIPE_KEY = "recipe.key";
    public static final String INTENT_EXTRA_RECIPE_ID_KEY = "recipe.id.key";

    private Recipe mRecipe;

    // Boolean flag keeping track of whether we are in "tablet vs. mobile" mode.
    private boolean mTabletModeOn;

    @Nullable
    @BindView(R.id.step_details_fragment_container) FrameLayout mStepDetailsContainerLayout;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        ButterKnife.bind(this);

        // Determine if we are in tablet mode or not: we are in tablet mode
        // we the container for the step details is present in our layout.
        mTabletModeOn = mStepDetailsContainerLayout != null;
        Log.d(LOG_TAG, "Tablet mode: " + mTabletModeOn);

        Intent intent = getIntent();
        if (intent!= null && intent.hasExtra(INTENT_EXTRA_RECIPE_KEY)) {
            mRecipe = intent.getParcelableExtra(INTENT_EXTRA_RECIPE_KEY);

            Log.d(LOG_TAG, mRecipe.toString());
        }

        if (mRecipe == null) {
            // If no recipe data can be extracted from the intent,
            // we look in the shared preferences to restore the last accessed recipe

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            long recipeId = sharedPreferences.getLong(RECIPE_ID_KEY, 0);

            if (recipeId == 0) { // we don't have a recipe ID to work with; just exit
                finish();
                return;
            }

            // Get a handle on the view model
            RecipeDetailsViewModel viewModel = RecipeDetailsViewModel.create(this);
            // ... and inject it into our DI setup
            BakingApplication.getInstance().getAppComponent().inject(viewModel);

            viewModel.getRecipe(recipeId).observe(this, new Observer<Recipe>() {
                @Override
                public void onChanged(@Nullable Recipe recipe) {
                    if (recipe == null) {
                        // the DB did not provide us with a workable recipe
                        // just exit
                        finish();
                        return;
                    }

                    // we finally have a recipe to work with
                    mRecipe = recipe;

                    // wait until the whole query in complete;
                    // (we have both the ingredients and steps set on the recipe)
                    if (mRecipe.getIngredients() != null && mRecipe.getSteps() != null) {
                        initUI(savedInstanceState);
                    }
                }
            });

        } else {
            initUI(savedInstanceState);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                if (isTaskRoot()) {
                    Intent intent = new Intent(this, RecipeListActivity.class);
                    startActivity(intent);
                } else {
                    NavUtils.navigateUpFromSameTask(this);
                }

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStepClick(Step step) {
        if (mTabletModeOn) {
            setStepDetailsFragment(step.getStepNo());
        } else {
            Intent intent = new Intent(this, StepDetailsActivity.class);

            intent.putExtra(INTENT_EXTRA_RECIPE_KEY, mRecipe);
            intent.putExtra(INTENT_EXTRA_STEP_INDEX_KEY, step.getStepNo());

            startActivity(intent);
        }
    }

    private void setStepDetailsFragment(int stepIndex) {
        StepDetailsFragment stepDetailsFragment = new StepDetailsFragment();

        Bundle args = new Bundle();
        args.putParcelable(STEP_ARGS_KEY, mRecipe.getSteps().get(stepIndex));
        args.putBoolean(STEP_ARGS_TABLET_MODE_KEY, mTabletModeOn);
        stepDetailsFragment.setArguments(args);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.step_details_fragment_container, stepDetailsFragment)
                .commit();
    }

    private void initUI(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "Initializing UI");

        // Setup the action-bar and the home button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle(mRecipe.getName());
        }

        // Only create new fragments when there is no previously saved state
        if (savedInstanceState == null) {
            RecipeDetailsFragment recipeDetailsFragment = new RecipeDetailsFragment();

            Bundle args = new Bundle();
            args.putParcelable(RECIPE_ARGS_KEY, mRecipe);
            recipeDetailsFragment.setArguments(args);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.recipe_details_fragment_container, recipeDetailsFragment)
                    .commit();

            if (mTabletModeOn) {
                setStepDetailsFragment(0);
            }
        }

        // Persist information about the current recipe in the app's preference store.
        saveRecipeInfoInPreferenceStore();

        // Update the widget
        BakingTimeWidget.refresh(this);
    }

    /**
     * Helper method in which we store information about the currently displayed recipe
     * in the app's preference store.
     *
     * This information will be later extracted by the app's widget to display it on the widget itself.
     */
    private void saveRecipeInfoInPreferenceStore() {
        // Save the info about the last accessed recipe in app's pref. store
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(RECIPE_NAME_KEY, mRecipe.getName());

        editor.putLong(RECIPE_ID_KEY, mRecipe.getId());

        Set<String> ingredientNames = new HashSet<>();
        for (Ingredient ingredient: mRecipe.getIngredients()) {
            ingredientNames.add(ingredient.getName());
        }
        editor.putStringSet(INGREDIENT_SET_KEY, ingredientNames);

        editor.apply();
    }
}
