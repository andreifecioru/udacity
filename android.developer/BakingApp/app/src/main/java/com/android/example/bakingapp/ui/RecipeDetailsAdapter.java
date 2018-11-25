package com.android.example.bakingapp.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.example.bakingapp.R;
import com.android.example.bakingapp.models.*;

import java.util.List;


/**
 * RecyclerView adapter which fuels the list view in the {@link RecipeDetailsActivity}
 * activity.
 *
 * The layout is a little more complicated. Not all items have the same layout (i.e. the layout
 * changes based on the item's position in the list). The order is as follows:
 *  - a section header for ingredients: a simple text view
 *  - a list of ingredients: simple layout with lines of text
 *  - a section header for steps: a simple text view
 *  - a list of steps: classic layout with number on the left, and the step description on the right
 */
public class RecipeDetailsAdapter
        extends RecyclerView.Adapter<RecipeDetailsAdapter.RecipeDetailsViewHolder>{

    // list of view types (4 in total)
    private final static int VIEW_TYPE_INGREDIENTS_HEADER = 100;
    private final static int VIEW_TYPE_INGREDIENT = 101;
    private final static int VIEW_TYPE_STEPS_HEADER = 200;
    private final static int VIEW_TYPE_STEP = 201;

    private final Context mContext;

    private final OnStepClickListener mOnStepClickListener;

    private List<Ingredient> mIngredients;
    private List<Step> mSteps;

    RecipeDetailsAdapter(Context context,
                         Recipe recipe,
                         OnStepClickListener onStepClickListener) {
        mContext = context;

        mIngredients = recipe.getIngredients();
        mSteps = recipe.getSteps();

        mOnStepClickListener = onStepClickListener;
    }

    @NonNull
    @Override
    public RecipeDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        int layoutId;

        // based on the view type we're dealing with different layouts
        switch (viewType) {
            case VIEW_TYPE_INGREDIENTS_HEADER:
            case VIEW_TYPE_STEPS_HEADER:
                layoutId = R.layout.section_header_item;
                break;

            case VIEW_TYPE_INGREDIENT:
                layoutId = R.layout.ingredient_item;
                break;

            case VIEW_TYPE_STEP:
                layoutId = R.layout.step_item;
                break;

            default:
                throw new IllegalArgumentException("Invalid view type: " + viewType);
        }

        View view = LayoutInflater.from(mContext)
                .inflate(layoutId, viewGroup, false);

        return new RecipeDetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeDetailsViewHolder viewHolder, int position) {
        int viewType = getItemViewType(position);

        // based on the view type we're dealing with different layouts,
        // so we need to deal with different UI controls
        switch (viewType) {
            case VIEW_TYPE_INGREDIENTS_HEADER:
                viewHolder.mSectionHeaderTextView.setText(mContext.getString(R.string.section_header_ingredients));
                break;

            case VIEW_TYPE_INGREDIENT:
                renderIngredientView(viewHolder, position);
                break;

            case VIEW_TYPE_STEPS_HEADER:
                viewHolder.mSectionHeaderTextView.setText(mContext.getString(R.string.section_header_steps));
                break;

            case VIEW_TYPE_STEP:
                renderStepView(viewHolder, position);
                break;

            default:
                throw new IllegalArgumentException("Invalid view type: " + viewType);
        }
    }


    @Override
    public int getItemCount() {
        return  getIngredientsViewCount() + getStepsViewCount() +
                2; // 2 comes from: ingredients header + steps header
    }

    @Override
    public int getItemViewType(int position) {
        int ingredientsViewCount = getIngredientsViewCount();

        // the view type is dictated by the items position

        // 1st comes a section header for ingredients
        if (position == 0) {
            return VIEW_TYPE_INGREDIENTS_HEADER;
        }

        // ... then the ingredients list
        if (position < ingredientsViewCount + 1) {
            return VIEW_TYPE_INGREDIENT;
        }

        // .. then the section header for steps
        if (position == ingredientsViewCount + 1) {
            return VIEW_TYPE_STEPS_HEADER;
        }

        // ... then the step list
        return VIEW_TYPE_STEP;
    }

    /**
     * Helper method that allows us to alternate the background colors of the
     * items in the recycler view. This creates the "zebra-like" pattern
     * making things easier to follow by adding a little bit of visual contrast.
     */
    private void setItemBgColor(ViewGroup itemContainer, int position) {
        int color = ContextCompat.getColor(
                mContext,
                position % 2 == 0 ? R.color.grey : R.color.greyLight);

        itemContainer.setBackgroundColor(color);
    }

    private int getIngredientsViewCount() {
        // when there are no ingredients to render, the view count is still 1
        // (to display the loading indicator or the "no ingredients" message)
        if (mIngredients == null || mIngredients.isEmpty()) return 1;

        return mIngredients.size();
    }

    private int getStepsViewCount() {
        // when there are no steps to render, the view count is still 1
        // (to display the loading indicator or the "no steps" message)
        if (mSteps == null || mSteps.isEmpty()) return 1;

        return mSteps.size();
    }

    /**
     * Helper method for rendering an item in the list of ingredients.
     */
    private void renderIngredientView(@NonNull RecipeDetailsViewHolder viewHolder, int position) {
        if (mIngredients == null || mIngredients.isEmpty()) {
            // there are no ingredients to show; just display a message
            viewHolder.mIngredientNameTextView.setText(R.string.no_ingredients_available);
        } else {
            // now we have something to display
            int ingredientsOffset = 1; // 1 comes from: ingredients section header
            Ingredient ingredient = mIngredients.get(position - ingredientsOffset);

            setItemBgColor(viewHolder.mIngredientContainerLayout, position);

            viewHolder.mIngredientNameTextView.setText(ingredient.getName().toLowerCase());
            viewHolder.mIngredientQuantityTextView.setText(mContext.getString(R.string.ingredient_quantity,
                    ingredient.getQuantity(), ingredient.getMeasure()));
        }
    }

    /**
     * Helper method for rendering an item in the list of steps.
     */
    private void renderStepView(@NonNull RecipeDetailsViewHolder viewHolder, int position) {
        if (mSteps == null || mSteps.isEmpty()) {
            // there are no ingredients to show; just display a message
            viewHolder.mStepNoTxtView.setVisibility(View.GONE);
            viewHolder.mStepDescriptionTextView.setText(R.string.no_steps_available);
        } else {
            // now we have something to display
            // 2 comes from: ingredients section header + steps section header
            int stepsOffset = 2 + getIngredientsViewCount();
            final Step step = mSteps.get(position - stepsOffset);

            viewHolder.mStepNoTxtView.setVisibility(View.VISIBLE);
            viewHolder.mStepNoTxtView.setText(String.valueOf(step.getStepNo()));
            viewHolder.mStepDescriptionTextView.setText(step.getDescription());

            setItemBgColor(viewHolder.mStepContainerLayout, position);

            // make the review items "clickable"
            viewHolder.mStepContainerLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnStepClickListener.onStepClick(step);
                }
            });
        }
    }

    // implements the view-holder pattern
    class RecipeDetailsViewHolder extends RecyclerView.ViewHolder {
        private final TextView mSectionHeaderTextView;

        private final ViewGroup mIngredientContainerLayout;
        private final TextView mIngredientNameTextView;
        private final TextView mIngredientQuantityTextView;

        private final ViewGroup mStepContainerLayout;
        private final TextView mStepNoTxtView;
        private final TextView mStepDescriptionTextView;

        RecipeDetailsViewHolder(@NonNull View view) {
            super(view);

            // here we initialize all the UI controls in all layouts
            // knowing full well that some of them will be null

            // we are relying on the proper computation of view types
            // to avoid triggering a NPE at runtime

            mSectionHeaderTextView = view.findViewById(R.id.tv_section_header);

            mIngredientContainerLayout = view.findViewById(R.id.layout_ingredient_container);
            mIngredientNameTextView = view.findViewById(R.id.tv_ingredient_name);
            mIngredientQuantityTextView = view.findViewById(R.id.tv_ingredient_quantity);

            mStepContainerLayout = view.findViewById(R.id.layout_step_container);
            mStepNoTxtView = view.findViewById(R.id.tv_step_no);
            mStepDescriptionTextView = view.findViewById(R.id.tv_step_description);
        }
    }

    /**
     * Protocol for providing custom logic for handling user taps on step items.
     */
    interface OnStepClickListener {
        void onStepClick(Step step);
    }
}
