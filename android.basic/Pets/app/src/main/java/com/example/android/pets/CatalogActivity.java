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
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.pets.data.PetContract.PetEntry;
import com.example.android.pets.models.Pet;
import com.example.android.pets.models.Pets;

import java.util.List;


/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity
        extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String LOG_TAG = CatalogActivity.class.getSimpleName();

    private final static int PET_CURSOR_LOADER_ID = 1;

    private PetCursorAdapter mPetCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup list view to show the pet inventory
        ListView petsListView = findViewById(R.id.pets_list_view);
        petsListView.setEmptyView(findViewById(R.id.empty_view));

        // setup the adapter for our list view
        mPetCursorAdapter = new PetCursorAdapter(CatalogActivity.this, null);
        petsListView.setAdapter(mPetCursorAdapter);
        petsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(LOG_TAG, "Pressed on pet with id: " + id);

                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                intent.setData(ContentUris.withAppendedId(PetEntry.CONTENT_URI_PETS, id));

                startActivity(intent);
            }
        });

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        // setup the loader
        getSupportLoaderManager()
                .initLoader(PET_CURSOR_LOADER_ID,null, this)
                .forceLoad();
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
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllPets();
                return true;
        }
        return super.onOptionsItemSelected(item);
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

    /**
     * Loader manager callbacks
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String [] projection = {
                PetEntry.COLUMN_PET_ID,
                PetEntry.COLUMN_PET_NAME,
                PetEntry.COLUMN_PET_BREED
        };

        return new CursorLoader(CatalogActivity.this, PetEntry.CONTENT_URI_PETS,
                projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mPetCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mPetCursorAdapter.swapCursor(null);
    }
}
