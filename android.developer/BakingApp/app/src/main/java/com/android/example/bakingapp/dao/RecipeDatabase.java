package com.android.example.bakingapp.dao;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.android.example.bakingapp.models.*;


/**
 * The "recipes" DB contains three tables:
 *  - the "recipes" table contains basic recipe info (like name, thumbnail image, etc).
 *  - the "ingredients" table (with FK pointing to the "recipes" table)
 *  - the "steps" table (with FK pointing to the "recipes" table)
 */
@Database(
        entities = {Recipe.class, Ingredient.class, Step.class},
        version = 1,
        exportSchema = false)
public abstract class RecipeDatabase extends RoomDatabase {
    public abstract RecipeDao getRecipeDao();
    public abstract IngredientDao getIngredientDao();
    public abstract StepDao getStepDao();
}
