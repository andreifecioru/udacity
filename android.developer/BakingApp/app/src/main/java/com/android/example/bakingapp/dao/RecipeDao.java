package com.android.example.bakingapp.dao;

import java.util.List;

import android.arch.lifecycle.LiveData;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

import com.android.example.bakingapp.models.Recipe;


/**
 * DAO definition for the basic recipe information.
 * Services provided:
 *  - retrieve all the recipes from DB
 *  - retrieve a particular recipe by ID
 *  - insert a list of recipes into DB
 *  - clear all the recipes from DB (used during testing and
 *      will also clear the ingredients and steps due to the way
 *      foreign keys are set up)
 */
@Dao
public interface RecipeDao {
    @Query("SELECT * FROM recipes")
    LiveData<List<Recipe>> loadRecipes();

    @Query("SELECT * FROM recipes WHERE id = :recipeId")
    LiveData<List<Recipe>> getRecipe(long recipeId);

    @Insert(onConflict = REPLACE)
    void insertRecipes(Recipe... recipes);

    @Query("DELETE FROM recipes")
    void deleteAllRecipes();
}
