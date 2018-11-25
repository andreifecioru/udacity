package com.android.example.bakingapp.ui;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.example.bakingapp.R;
import com.android.example.bakingapp.models.Recipe;
import com.android.example.bakingapp.utils.Resource;
import com.android.example.bakingapp.utils.SimpleIdlingResource;
import com.android.example.bakingapp.utils.ui.RecyclerViewEmptyViewSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.android.example.bakingapp.ui.RecipeDetailsActivity.INTENT_EXTRA_RECIPE_KEY;


/**
 * Implements the "recipe-list" screen displaying the list of recipes downloaded from the
 * recipe "end-point" in a grid view.
 *
 * The look of the screen differs when in tablet vs. mobile mode:
 *  - in tablet mode, the grid-view has 3 columns.
 *  - im mobile mode, the grid-view has 1 column.
 */
public class RecipeListActivity
        extends BaseActivity
        implements RecipeAdapter.OnRecipeClick {
    private final String LOG_TAG = RecipeListActivity.class.getSimpleName();

    private static final String BUNDLE_RECYCLER_LAYOUT_STATE = "recycler.layout.state";

    private static final int GRID_COLUMN_COUNT_MOBILE = 1;
    private static final int GRID_COLUMN_COUNT_TABLET = 3;

    @Nullable
    @BindView(R.id.layout_tablet) FrameLayout mTabletLayout;
    @BindView(R.id.layout_coordinator) CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.rv_recipe_list) RecyclerViewEmptyViewSupport mRecipesRecyclerView;
    @BindView(R.id.empty_view) LinearLayout mEmptyView;
    @BindView(R.id.empty_view_no_recipes) LinearLayout mEmptyViewNoRecipes;
    @BindView(R.id.pb_empty_view) ProgressBar mEmptyViewProgressBar;

    private RecipeAdapter mRecipeAdapter;

    private Parcelable mRecyclerViewState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        ButterKnife.bind(this);

        // We determine if we are in "tablet" mode by looking for a specific
        // (empty) FrameLayout that is present only in sw600dp layout
        boolean mTabletModeOn = mTabletLayout != null;

        // Item size does not change
        mRecipesRecyclerView.setHasFixedSize(true);

        // We use a grid layout manager
        int columnCount = mTabletModeOn ? GRID_COLUMN_COUNT_TABLET : GRID_COLUMN_COUNT_MOBILE;
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, columnCount);
        mRecipesRecyclerView.setLayoutManager(layoutManager);

        // Setup the adapter for our recycler view (start with an empty list)
        mRecipeAdapter = new RecipeAdapter(new ArrayList<Recipe>(), this);
        mRecipesRecyclerView.setAdapter(mRecipeAdapter);

        // Set the empty view for our recycler view
        mRecipesRecyclerView.setEmptyView(mEmptyView);

        // Setup the action-bar and the home button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
        }

        // Initial setup for the UI controls:
        // we assume that the data is loading and
        // we show the loading indicator (progress bar)
        // and hide everything else.
        mEmptyView.setVisibility(View.VISIBLE);
        mEmptyViewNoRecipes.setVisibility(View.GONE);
        mEmptyViewProgressBar.setVisibility(View.VISIBLE);

        // Kick-start data loading (initializes the view model)
        if (mIdlingResource == null) {
            refreshRecipeList();
        }
    }

    @VisibleForTesting
    public void refreshRecipeList() {
        Log.d(LOG_TAG, "Refreshing the recipe list...");

        // Lock idling resource
        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(false);
        }

        // Get a handle on the view model
        RecipeListViewModel viewModel = RecipeListViewModel.create(this);
        // ... and inject it into our DI setup
        BakingApplication.getInstance().getAppComponent().inject(viewModel);

        // Watch the view model for any changes in the underlying live data
        viewModel.getRecipes().observe(this, new Observer<Resource<List<Recipe>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<Recipe>> resource) {
                if (resource == null) return;

                switch (resource.status) {
                    case LOADING:
                        if (resource.data == null || resource.data.isEmpty()) {
                            Log.d(LOG_TAG, "No data available in local cache. Loading...");

                            // Show the loading progress indicator
                            mEmptyViewProgressBar.setVisibility(View.VISIBLE);
                            mEmptyViewNoRecipes.setVisibility(View.GONE);
                        } else {
                            Log.d(LOG_TAG, "Data available in local cache. Loading...");
                            // Data loading is still in progress
                            // but we have some cache data to display
                            // until data is ready.

                            // Hide the loading progress indicator
                            mEmptyViewProgressBar.setVisibility(View.GONE);
                            mEmptyViewNoRecipes.setVisibility(View.VISIBLE);

                            mRecipeAdapter.setRecipes(resource.data);
                        }
                        break;

                    case SUCCESS:
                        Log.d(LOG_TAG, "Data is available.");
                        // Hide the loading progress indicator
                        mEmptyViewProgressBar.setVisibility(View.GONE);
                        mEmptyViewNoRecipes.setVisibility(View.VISIBLE);

                        mRecipeAdapter.setRecipes(resource.data);
                        break;

                    case ERROR:
                        // Hide the loading progress indicator
                        mEmptyViewProgressBar.setVisibility(View.GONE);
                        mEmptyViewNoRecipes.setVisibility(View.VISIBLE);

                        mRecipeAdapter.setRecipes(resource.data);

                        Snackbar.make(mCoordinatorLayout,
                                getResources().getText(R.string.error_loading_recipe_list),
                                Snackbar.LENGTH_SHORT).show();

                        Log.e(LOG_TAG, "Error loading recipe list. " + resource.message);
                        break;

                    default:
                        throw new IllegalStateException("Unknown resource status: " + resource.status);
                }

                // Release idling resource
                if (mIdlingResource != null) {
                    mIdlingResource.setIdleState(true);
                }
            }
        });
    }

    @VisibleForTesting
    public void deleteAllRecipes(Runnable onComplete) {
        Log.d(LOG_TAG, "Deleting all recipes...");

        // Get a handle on the view model
        RecipeListViewModel viewModel = RecipeListViewModel.create(this);
        // ... and inject it into our DI setup
        BakingApplication.getInstance().getAppComponent().inject(viewModel);

        viewModel.deleteAllRecipes(onComplete);
    }

    // Save/restore the state of the recycler view to persist the scrolling position
    // while switching between apps.
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        RecyclerView.LayoutManager layoutManager = mRecipesRecyclerView.getLayoutManager();

        if (layoutManager != null) {
            outState.putParcelable(BUNDLE_RECYCLER_LAYOUT_STATE,
                    layoutManager.onSaveInstanceState());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            mRecyclerViewState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT_STATE);
            restoreRecyclerViewState();
        }
    }

    private void restoreRecyclerViewState() {
        if (mRecyclerViewState != null) {
            RecyclerView.LayoutManager layoutManager = mRecipesRecyclerView .getLayoutManager();

            if (layoutManager != null) {
                layoutManager.onRestoreInstanceState(mRecyclerViewState);
            }
        }
    }

    @OnClick(R.id.bt_try_again)
    public void onTryAgainButtonClick(View view) {
        refreshRecipeList();
    }

    /**
     * Invoked when the user tapped one of the recipe (i.e. one item
     * in the grid view).
     *
     * The user is taken to the {@link RecipeDetailsActivity} where the info for the recipe
     * that was tapped is displayed.
     *
     * @param recipe the info for the recipe item that ws tapped
     */
    @Override
    public void onRecipeClick(Recipe recipe) {
        Log.i(LOG_TAG, "Selected recipe: " + recipe.getName());

        Intent intent = new Intent(this, RecipeDetailsActivity.class);
        intent.putExtra(INTENT_EXTRA_RECIPE_KEY, recipe);

        startActivity(intent);
    }

    // Setup of idling resource used during instrumented testing.
    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }

        return mIdlingResource;
    }
}
