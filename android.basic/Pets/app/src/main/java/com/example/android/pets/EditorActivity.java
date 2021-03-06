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
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.pets.data.PetContract.PetEntry;
import com.example.android.pets.models.Pet;

/**
 * Allows user to create a new pet or edit an existing one.
 */
public class EditorActivity
        extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String LOG_TAG = EditorActivity.class.getSimpleName();

    private final static int PET_CURSOR_LOADER_ID = 1;

    /** EditText field to enter the pet's name */
    private EditText mNameEditText;

    /** EditText field to enter the pet's breed */
    private EditText mBreedEditText;

    /** EditText field to enter the pet's weight */
    private EditText mWeightEditText;

    /** EditText field to enter the pet's gender */
    private Spinner mGenderSpinner;

    /**
     * Gender of the pet. The possible values are:
     * 0 for unknown gender, 1 for male, 2 for female.
     */
    private int mGender = PetEntry.GENDER_UNKNOWN;

    private Uri mPetUri;

    private boolean mPetHasChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();
        if (intent != null) {
            mPetUri = intent.getData();
        }

        if (mPetUri != null) {
            setTitle(R.string.editor_activity_title_edit_pet);

            invalidateOptionsMenu();

            // setup the loader
            getSupportLoaderManager()
                    .initLoader(PET_CURSOR_LOADER_ID,null, this)
                    .forceLoad();
        } else {
            setTitle(R.string.editor_activity_title_new_pet);
        }

        Log.d(LOG_TAG, "Pet URI: " + mPetUri);

        // Find all relevant views that we will need to read user input from
        mNameEditText = findViewById(R.id.edit_pet_name);
        mBreedEditText = findViewById(R.id.edit_pet_breed);
        mWeightEditText = findViewById(R.id.edit_pet_weight);
        mGenderSpinner = findViewById(R.id.spinner_gender);

        mNameEditText.setOnTouchListener(mOnTouchListener);
        mBreedEditText.setOnTouchListener(mOnTouchListener);
        mWeightEditText.setOnTouchListener(mOnTouchListener);
        mGenderSpinner.setOnTouchListener(mOnTouchListener);


        setupSpinner();
    }

    /**
     * Setup the dropdown spinner that allows the user to select the gender of the pet.
     */
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mGenderSpinner.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.gender_male))) {
                        mGender = PetEntry.GENDER_MALE;
                    } else if (selection.equals(getString(R.string.gender_female))) {
                        mGender = PetEntry.GENDER_FEMALE;
                    } else {
                        mGender = PetEntry.GENDER_UNKNOWN;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGender = PetEntry.GENDER_UNKNOWN;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if (mPetUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                savePet(createPetFromUserInput());
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                showDeleteConfirmationDialog(
                        // user confirmed pet deletion
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int deletedPets = deletePet();
                                if (deletedPets > 0) {
                                    Toast.makeText(EditorActivity.this,
                                            getString(R.string.msg_delete_pet_success),
                                            Toast.LENGTH_SHORT)
                                    .show();
                                    finish();
                                } else {
                                    Toast.makeText(EditorActivity.this,
                                            getString(R.string.msg_delete_pet_failed),
                                            Toast.LENGTH_SHORT)
                                            .show();
                                }
                            }
                        },

                        // user cancelled pet deletion
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(EditorActivity.this,
                                        getString(R.string.msg_delete_pet_cancelled),
                                        Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }
                );
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                if (mPetHasChanged) {
                    showUnsavedChangesDialog(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Navigate back to parent activity (CatalogActivity)
                            NavUtils.navigateUpFromSameTask(EditorActivity.this);
                        }
                    });
                } else {
                    // Navigate back to parent activity (CatalogActivity)
                    NavUtils.navigateUpFromSameTask(this);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mPetHasChanged) {
            showUnsavedChangesDialog(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
        } else {
            super.onBackPressed();
        }
    }

    private Pet createPetFromUserInput() {

        try {
            String name = mNameEditText.getText().toString().trim();
            String breed = mBreedEditText.getText().toString().trim();
            int weight = Integer.parseInt(mWeightEditText.getText().toString().trim());
            return new Pet(name, breed, mGender, weight);
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
            return null;
        }
    }

    private void savePet(Pet pet) {
        if (pet == null) return;

        ContentResolver resolver = getContentResolver();

        ContentValues values = new ContentValues();
        values.put(PetEntry.COLUMN_PET_NAME, pet.getName());
        values.put(PetEntry.COLUMN_PET_BREED, pet.getBreed());
        values.put(PetEntry.COLUMN_PET_GENDER, pet.getGender());
        values.put(PetEntry.COLUMN_PET_WEIGHT, pet.getWeight());

        if (mPetUri == null) { // insert mode
            Uri newPetUri = resolver.insert(PetEntry.CONTENT_URI_PETS, values);
            if (newPetUri != null) {
                Log.d(LOG_TAG, "URI for new pet: " + newPetUri.toString());
            }
        } else { // update mode
            long petId = ContentUris.parseId(mPetUri);
            String selection = "_id = ?";
            String[] selectionArgs = new String[] { String.valueOf(petId) };
            int rowsUpdated = resolver.update(mPetUri, values, selection, selectionArgs);

            if (rowsUpdated == 0) {
                Toast.makeText(EditorActivity.this,
                        getString(R.string.msg_update_pet_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(EditorActivity.this,
                        getString(R.string.msg_update_pet_success),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private int deletePet() {
        ContentResolver resolver = getContentResolver();
        return resolver.delete(mPetUri, null, null);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(EditorActivity.this, mPetUri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.getCount() == 1) {
            cursor.moveToPosition(0);
            long id = cursor.getLong(cursor.getColumnIndex(PetEntry.COLUMN_PET_ID));
            String name = cursor.getString(cursor.getColumnIndex(PetEntry.COLUMN_PET_NAME));
            String breed = cursor.getString(cursor.getColumnIndex(PetEntry.COLUMN_PET_BREED));
            int gender = cursor.getInt(cursor.getColumnIndex(PetEntry.COLUMN_PET_GENDER));
            int weight = cursor.getInt(cursor.getColumnIndex(PetEntry.COLUMN_PET_WEIGHT));

            fillUIFromPet(new Pet(id, name, breed, gender, weight));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNameEditText.setText("");
        mBreedEditText.setText("");
        mWeightEditText.setText("");
        mGenderSpinner.setSelection(PetEntry.GENDER_UNKNOWN);
    }

    private void fillUIFromPet(Pet pet) {
        mNameEditText.setText(pet.getName());
        mBreedEditText.setText(pet.getBreed());
        mWeightEditText.setText(String.valueOf(pet.getWeight()));
        mGenderSpinner.setSelection(pet.getGender());
    }

    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mPetHasChanged = true;
            return false;
        }
    };

    private void showUnsavedChangesDialog(DialogInterface.OnClickListener onDiscardClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditorActivity.this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, onDiscardClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        builder.create().show();
    }

    private void showDeleteConfirmationDialog(
            DialogInterface.OnClickListener onOkClickListener,
            DialogInterface.OnClickListener onCancelListener ) {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditorActivity.this);
        builder.setMessage(R.string.msg_delete_pet_entry);
        builder.setPositiveButton(R.string.delete_yes, onOkClickListener);
        builder.setNegativeButton(R.string.delete_no, onCancelListener);

        builder.create().show();
    }
}