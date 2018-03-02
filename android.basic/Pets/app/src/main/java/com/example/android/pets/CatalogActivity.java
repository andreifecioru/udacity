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
import android.widget.ListView;

import com.example.android.pets.data.PetContract.PetEntry;
import com.example.android.pets.models.Pet;
import com.example.android.pets.models.Pets;

import java.util.List;


/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity {
    private static final String LOG_TAG = CatalogActivity.class.getSimpleName();

    private ListView mPetsListView;

    private PetCursorAdapter mPetCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup list view to show the pet inventory
        mPetsListView = findViewById(R.id.pets_list_view);
        mPetsListView.setEmptyView(findViewById(R.id.empty_view));

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
    protected void onStart() {
        super.onStart();
        displayPetList();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPetCursorAdapter.getCursor().close();
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
                refreshPetList();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllPets();
                refreshPetList();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void displayPetList() {
        mPetCursorAdapter = new PetCursorAdapter(CatalogActivity.this, setupCursor());
        mPetsListView.setAdapter(mPetCursorAdapter);
    }

    private void refreshPetList() {
        mPetCursorAdapter.getCursor().close();
        mPetCursorAdapter.changeCursor(setupCursor());
    }

    private Cursor setupCursor() {
        ContentResolver resolver = getContentResolver();

        String [] projection = {
                PetEntry.COLUMN_PET_ID,
                PetEntry.COLUMN_PET_NAME,
                PetEntry.COLUMN_PET_BREED,
                PetEntry.COLUMN_PET_GENDER,
                PetEntry.COLUMN_PET_WEIGHT
        };

        return resolver.query(PetEntry.CONTENT_URI_PETS, projection, null, null, null);
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
