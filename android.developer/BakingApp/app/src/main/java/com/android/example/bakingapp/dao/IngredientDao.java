package com.android.example.bakingapp.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.android.example.bakingapp.models.Ingredient;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;


/**
 * DAO definition for the ingredient information.
 * Services provided:
 *  - retrieve all the ingredients from DB
 *  - insert a list of ingredients into DB
 */
@Dao
public interface IngredientDao {
    @Query("SELECT * FROM ingredients WHERE recipe_id = :recipeId")
    LiveData<List<Ingredient>> loadIngredientsForRecipe(long recipeId);

    @Insert(onConflict = REPLACE)
    void insertIngredients(Ingredient... ingredients);
}
