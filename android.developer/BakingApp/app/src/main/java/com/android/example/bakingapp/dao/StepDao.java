package com.android.example.bakingapp.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.android.example.bakingapp.models.Step;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;


/**
 * DAO definition for the recipe-step information.
 * Services provided:
 *  - retrieve all the recipe-steps from DB
 *  - insert a list of recipe-steps into DB
 */
@Dao
public interface StepDao {
    @Query("SELECT * FROM steps WHERE recipe_id = :recipeId")
    LiveData<List<Step>> loadStepsForRecipe(long recipeId);

    @Insert(onConflict = REPLACE)
    void insertSteps(Step... step);
}
