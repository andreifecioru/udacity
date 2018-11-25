package com.android.example.bakingapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.example.bakingapp.R;
import com.android.example.bakingapp.models.Recipe;
import com.android.example.bakingapp.models.Step;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A custom {@link Fragment} implementation displaying the UI for the detailed info about a recipe.
 *
 * Displays the ingredient list together with a list of preparation steps in the same recycler-view
 * instance.
 */
public class RecipeDetailsFragment
        extends Fragment
        implements RecipeDetailsAdapter.OnStepClickListener {

    private static final String LOG_TAG = RecipeDetailsFragment.class.getSimpleName();

    public static final String RECIPE_ARGS_KEY = "recipe.args.key";

    public RecipeDetailsFragment() { }

    @BindView(R.id.rv_recipe_details) RecyclerView mRecipeDetailsRecyclerView;

    private Recipe mRecipe;

    private Protocol mProtocol;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null) {
            mRecipe = args.getParcelable(RECIPE_ARGS_KEY);
        }

        if (mRecipe == null) {
            throw new IllegalStateException("Recipe cannot be null");
        }

        View rootView = inflater.inflate(R.layout.fragment_recipe_details, container, false);

        ButterKnife.bind(this, rootView);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecipeDetailsRecyclerView.setLayoutManager(layoutManager);
        // Our item layout is not static (changes depending on the info to be rendered).
        mRecipeDetailsRecyclerView.setHasFixedSize(false);

        RecipeDetailsAdapter mRecipeDetailsAdapter = new RecipeDetailsAdapter(getContext(), mRecipe, this);
        mRecipeDetailsRecyclerView.setAdapter(mRecipeDetailsAdapter);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Make sure that the context we attach to conforms to the protocol.
        try {
            mProtocol = (Protocol) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + "must implement RecipeDetailsFragment.Protocol");
        }
    }

    @Override
    public void onStepClick(Step step) {
        Log.d(LOG_TAG, "Clicked on step " + step.getStepNo());
        mProtocol.onStepClick(step);
    }

    /**
     * Defines the communication protocol between the fragment and the parent activity.
     */
    public interface Protocol {
        // called when the user taps on a preparation-step item.
        void onStepClick(Step step);
    }
}
