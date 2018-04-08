package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;
import com.udacity.sandwichclub.utils.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    @BindView(R.id.image_iv) ImageView mIngredientsIv;
    @BindView(R.id.place_of_origin_label) TextView mOriginLabel;
    @BindView(R.id.origin_tv) TextView mOriginTv;
    @BindView(R.id.also_known_as_label) TextView mAlsoKnownAsLabel;
    @BindView(R.id.also_known_tv) TextView mAlsoKnownAsTv;
    @BindView(R.id.ingredients_label) TextView mIngredientsLabel;
    @BindView(R.id.ingredients_tv) TextView mIngredientsTv;
    @BindView(R.id.description_label) TextView mDescriptionLabel;
    @BindView(R.id.description_tv) TextView mDescriptionTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
            return;
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(mIngredientsIv);

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {
        showOriginInfo(sandwich);
        showAlsoKnownAsInfo(sandwich);
        showIngredientsInfo(sandwich);
        showDescriptionInfo(sandwich);
    }

    private void showOriginInfo(Sandwich sandwich) {
        if (TextUtils.isEmpty(sandwich.getPlaceOfOrigin())) {
            // Don't show the place of origin if info is not available
            mOriginLabel.setVisibility(View.GONE);
            mOriginTv.setVisibility(View.GONE);
        } else {
            mOriginLabel.setVisibility(View.VISIBLE);
            mOriginTv.setVisibility(View.VISIBLE);
            mOriginTv.setText(sandwich.getPlaceOfOrigin());
        }
    }

    private void showAlsoKnownAsInfo(Sandwich sandwich) {
        if (sandwich.getAlsoKnownAs().isEmpty()) {
            // Don't show the aka if info is not available
            mAlsoKnownAsLabel.setVisibility(View.GONE);
            mAlsoKnownAsTv.setVisibility(View.GONE);
        } else {
            mAlsoKnownAsLabel.setVisibility(View.VISIBLE);
            mAlsoKnownAsTv.setVisibility(View.VISIBLE);
            mAlsoKnownAsTv.setText(StringUtils.joinStrings(", ", sandwich.getAlsoKnownAs()));
        }
    }

    private void showIngredientsInfo(Sandwich sandwich) {
        if (sandwich.getIngredients().isEmpty()) {
            // Don't show the ingredients if info is not available
            mIngredientsLabel.setVisibility(View.GONE);
            mIngredientsIv.setVisibility(View.GONE);
        } else {
            mIngredientsLabel.setVisibility(View.VISIBLE);
            mIngredientsTv.setVisibility(View.VISIBLE);
            mIngredientsTv.setText(StringUtils.joinStrings(", ", sandwich.getIngredients()));
        }
    }

    private void showDescriptionInfo(Sandwich sandwich) {
        if (TextUtils.isEmpty(sandwich.getDescription())) {
            // Don't show the description if info is not available
            mDescriptionLabel.setVisibility(View.GONE);
            mDescriptionTv.setVisibility(View.GONE);
        } else {
            mDescriptionLabel.setVisibility(View.VISIBLE);
            mDescriptionTv.setVisibility(View.VISIBLE);
            mDescriptionTv.setText(sandwich.getDescription());
        }
    }
}
