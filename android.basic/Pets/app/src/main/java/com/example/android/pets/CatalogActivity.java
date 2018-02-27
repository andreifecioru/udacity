/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.pets;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.pets.data.PetContract.PetEntry;
import com.example.android.pets.data.PetDbHelper;
import com.example.android.pets.models.Pet;
import com.example.android.pets.models.Pets;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity {
    private static final String LOG_TAG = CatalogActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayDatabaseInfo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertPets(Pets.generateDummyData());
                displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllPets();
                displayDatabaseInfo();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the pets database.
     */
    private void displayDatabaseInfo() {
        List<Pet> pets = getAllPets();

        TextView displayView = findViewById(R.id.text_view_pet);
        displayView.setText("Number of rows in pets database table: " + pets.size());

        displayView.append("\n");
        displayView.append(String.format(Locale.ENGLISH, "\n %s - %s - %s - %s - %s",
                PetEntry.COLUMN_PET_ID, PetEntry.COLUMN_PET_NAME, PetEntry.COLUMN_PET_BREED, PetEntry.COLUMN_PET_GENDER, PetEntry.COLUMN_PET_WEIGHT));

        for (Pet pet : pets) {
            displayView.append("\n" + pet);
        }
    }

    private List<Pet> getAllPets() {
        ContentResolver resolver = getContentResolver();

        String [] projection = {
                PetEntry.COLUMN_PET_ID,
                PetEntry.COLUMN_PET_NAME,
                PetEntry.COLUMN_PET_BREED,
                PetEntry.COLUMN_PET_GENDER,
                PetEntry.COLUMN_PET_WEIGHT
        };

        try(Cursor cursor =
                    resolver.query(PetEntry.CONTENT_URI_PETS, projection, null, null, null)) {
            List<Pet> result = new ArrayList<>();

            if (cursor == null) return result;

            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndex(PetEntry.COLUMN_PET_ID));
                String name = cursor.getString(cursor.getColumnIndex(PetEntry.COLUMN_PET_NAME));
                String breed = cursor.getString(cursor.getColumnIndex(PetEntry.COLUMN_PET_BREED));
                int gender = cursor.getInt(cursor.getColumnIndex(PetEntry.COLUMN_PET_GENDER));
                int weight = cursor.getInt(cursor.getColumnIndex(PetEntry.COLUMN_PET_WEIGHT));

                result.add(new Pet(id, name, breed, gender, weight));
            }

            return result;
        }
    }

    private void deleteAllPets() {
        ContentResolver resolver = getContentResolver();
        resolver.delete(PetEntry.CONTENT_URI_PETS, null, null);
    }

    private void insertPets(List<Pet> pets) {
        for (Pet pet : pets) {
            insertPet(pet);
        }
    }

    private Uri insertPet(Pet pet) {
        ContentResolver resolver = getContentResolver();

        ContentValues values = new ContentValues();
        values.put(PetEntry.COLUMN_PET_NAME, pet.getName());
        values.put(PetEntry.COLUMN_PET_BREED, pet.getBreed());
        values.put(PetEntry.COLUMN_PET_GENDER, pet.getGender());
        values.put(PetEntry.COLUMN_PET_WEIGHT, pet.getWeight());

        Uri newPetUri = resolver.insert(PetEntry.CONTENT_URI_PETS, values);
        if (newPetUri != null) {
            Log.d(LOG_TAG, "URI for new pet: " + newPetUri.toString());
        }

        return newPetUri;
    }
}
