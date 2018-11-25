package com.android.example.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.android.example.bakingapp.R;
import com.android.example.bakingapp.models.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.android.example.bakingapp.ui.RecipeDetailsActivity.INTENT_EXTRA_RECIPE_KEY;
import static com.android.example.bakingapp.ui.StepDetailsFragment.STEP_ARGS_KEY;
import static com.android.example.bakingapp.ui.StepDetailsFragment.STEP_ARGS_TABLET_MODE_KEY;


/**
 * Implements the "step-details" screen displaying the information about the selected
 * preparation step.
 *
 * NOTE: this activity is only displayed in "mobile" mode. It houses the {@link StepDetailsFragment}
 * and decorates it with navigation buttons at the bottom of the layout, allowing the user to
 * navigate between preparation steps of the same recipe.
 */
public class StepDetailsActivity extends BaseActivity {
    private static final String LOG_TAG = StepDetailsActivity.class.getSimpleName();

    public static final String INTENT_EXTRA_STEP_INDEX_KEY = "step.index.key";

    @BindView(R.id.fab_prev_step) FloatingActionButton mPrevStepFAB;
    @BindView(R.id.fab_next_step) FloatingActionButton mNextStepFAB;

    private Recipe mRecipe;
    private int mStepIndex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent!= null) {
            if (intent.hasExtra(INTENT_EXTRA_RECIPE_KEY)) {
                mRecipe = intent.getParcelableExtra(INTENT_EXTRA_RECIPE_KEY);
                Log.d(LOG_TAG, mRecipe.toString());
            }

            // If we are not provided with a "step index", default to displaying
            // the first preparation step of the recipe.
            if (intent.hasExtra(INTENT_EXTRA_STEP_INDEX_KEY)) {
                mStepIndex = intent.getIntExtra(INTENT_EXTRA_STEP_INDEX_KEY, 0);
            }
        }

        if (mRecipe == null) {
            // If no recipe data can be extracted, we're done with this activity.
            finish();
            return;
        }

        // Setup the action-bar and the home button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle(mRecipe.getName());
        }

        // Only create new fragments when there is no previously saved state
        if (savedInstanceState == null) {
            setStepDetailsFragment();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       int id = item.getItemId();

       switch (id) {
           case android.R.id.home:
               NavUtils.navigateUpFromSameTask(this);
               return true;
       }

       return super.onOptionsItemSelected(item);
    }

    /**
     * Helper methods that allows us to "navigate" between the steps of a given recipe.
     *
     * It functions by replacing the {@link StepDetailsFragment} with the new content.
     */
    private void setStepDetailsFragment() {
        StepDetailsFragment stepDetailsFragment = new StepDetailsFragment();

        Bundle args = new Bundle();
        args.putParcelable(STEP_ARGS_KEY, mRecipe.getSteps().get(mStepIndex));
        args.putBoolean(STEP_ARGS_TABLET_MODE_KEY, false);
        stepDetailsFragment.setArguments(args);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
            .replace(R.id.step_details_fragment_container, stepDetailsFragment)
            .commit();

        // If this is the 1st step, hide the "previous step" button
        mPrevStepFAB.setVisibility(mStepIndex == 0 ? View.GONE : View.VISIBLE);

        // If this is the last step, hide the "next step" button
        mNextStepFAB.setVisibility(mStepIndex >= mRecipe.getSteps().size() - 1 ? View.GONE : View.VISIBLE);
    }

    @OnClick(R.id.fab_prev_step)
    public void onPrevStepButtonClick(View view) {
        Log.d(LOG_TAG, "Previous step click!");
        if (mStepIndex > 0) mStepIndex--;
        setStepDetailsFragment();
    }

    @OnClick(R.id.fab_next_step)
    public void onNextStepButtonClick(View view) {
        Log.d(LOG_TAG, "Next step click!");
        if (mStepIndex < mRecipe.getSteps().size() - 1) mStepIndex ++;
        setStepDetailsFragment();
    }
}
