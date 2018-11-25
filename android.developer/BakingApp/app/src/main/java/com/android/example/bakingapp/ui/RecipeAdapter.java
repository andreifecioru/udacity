package com.android.example.bakingapp.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.example.bakingapp.R;
import com.android.example.bakingapp.models.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * RecyclerView adapter for the {@link Recipe} class. Fuels the grid-view
 * which displays the list of recipes.
 */
public class RecipeAdapter
    extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private List<Recipe> mRecipes;

    private final OnRecipeClick mOnRecipeClick;

    /** Produces an instance of the {@link RecipeAdapter} class (constructor)
     *
     * @param recipes A list of {@link Recipe} objects (the raw data source)
     * @param onRecipeClick Implementation of the {@link OnRecipeClick} interface
     *                      where you can specify custom logic for clicking a recipe item.
     */
    RecipeAdapter(List<Recipe> recipes,
                 OnRecipeClick onRecipeClick) {
        mRecipes = recipes;
        mOnRecipeClick = onRecipeClick;
    }

    /** Updates the recipe list (the underlying data source).
     *
     * @param recipes A list of {@link Recipe} objects (the raw data source)
     */
    public void setRecipes(List<Recipe> recipes) {
        mRecipes = recipes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for the recipe item.
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View root = inflater.inflate(R.layout.recipe_grid_item, parent, false);

        // Return the view holder
        return new RecipeViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        // Get the recipe at position
        Recipe recipe = mRecipes.get(position);

        // Bind data to the view-holder
        holder.bindRecipe(recipe);
    }

    @Override
    public int getItemCount() {
        if (mRecipes == null) return 0;
        return mRecipes.size();
    }

    // Implements the view-holder pattern.
    class RecipeViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private final Context mContext;

        private ImageView mRecipeImageView;
        private TextView mNameTextView;
        private TextView mIngredientCountTextView;
        private TextView mStepCountTextView;

        private Recipe mRecipe;

        RecipeViewHolder(@NonNull View root) {
            super(root);

            mContext = root.getContext();
            // Install on-click handler
            root.setOnClickListener(this);

            // Cache UI controls
            mRecipeImageView = root.findViewById(R.id.iv_recipe_image);
            mNameTextView = root.findViewById(R.id.tv_recipe_name);
            mIngredientCountTextView = root.findViewById(R.id.tv_ingredient_count);
            mStepCountTextView = root.findViewById(R.id.tv_step_count);
        }

        @Override
        public void onClick(View view) {
            mOnRecipeClick.onRecipeClick(mRecipe);
        }

        // Binding method.
        void bindRecipe(Recipe recipe) {
            // Sanity check: fast exit
            if (recipe == null) return;

            mRecipe = recipe;

            // Display the recipe name
            mNameTextView.setText(mRecipe.getName());

            // Display the ingredient & step count
            int ingredientCount = mRecipe.getIngredients() == null ? 0 : mRecipe.getIngredients().size();
            mIngredientCountTextView.setText(mContext.getString(R.string.ingredients_count, String.valueOf(ingredientCount)));

            int stepCount = mRecipe.getSteps() == null ? 0 : mRecipe.getSteps().size();
            mStepCountTextView.setText(mContext.getString(R.string.steps_count, String.valueOf(stepCount)));

            // Render the recipe image
            String imageUrl = mRecipe.getImageUrl();
            if (TextUtils.isEmpty(imageUrl)) {
                mRecipeImageView.setImageResource(R.drawable.pie);
            } else {
                Picasso.with(mContext)
                        .load(imageUrl)
                        .placeholder(R.drawable.pie)
                        .error(R.drawable.pie)
                        .into(mRecipeImageView);
            }
        }
    }


    /**
     * Protocol for providing custom logic for handling user taps on recipe items.
     */
    interface OnRecipeClick {
        void onRecipeClick(Recipe recipe);
    }
}
