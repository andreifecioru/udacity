package com.example.android.pets.data;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.example.android.pets.data.PetContract.PetEntry;


public class PetProvider extends ContentProvider {
    static final private String LOG_TAG = PetProvider.class.getSimpleName();

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int URI_PATTERN_ID_PETS = 1000;
    private static final int URI_PATTERN_ID_PET_ID = 10001;


    private PetDbHelper mDbHelper;

    // setup the URI matcher object
    static {
        final UriMatcher matcher = sUriMatcher;

        matcher.addURI(PetContract.CONTENT_AUTHORITY, PetContract.PATH_PETS, URI_PATTERN_ID_PETS);
        matcher.addURI(PetContract.CONTENT_AUTHORITY, PetContract.PATH_PET_ID, URI_PATTERN_ID_PET_ID);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new PetDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri,
                        @Nullable String[] projection,
                        @Nullable String selection,
                        @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case URI_PATTERN_ID_PETS:
                cursor = db.query(PetEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case URI_PATTERN_ID_PET_ID:
                selection = PetEntry.COLUMN_PET_ID + "= ?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                cursor = db.query(PetEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Query is not supported for URI: " + uri);
        }

        Context context = getContext();
        if (context != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case URI_PATTERN_ID_PETS:
                return PetEntry.MIME_TYPE_PET_LIST;
            case URI_PATTERN_ID_PET_ID:
                return PetEntry.MIME_TYPE_PET_ITEM;
            default:
                throw new IllegalArgumentException("Type is not supported for URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri,
                      @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case URI_PATTERN_ID_PETS:
                return insertPet(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for URI: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri,
                      @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case URI_PATTERN_ID_PETS:
                return deletePet(uri, selection, selectionArgs);
            case URI_PATTERN_ID_PET_ID:
                selection = PetEntry.COLUMN_PET_ID + " = ?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return deletePet(uri, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Delete is not supported for URI: " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri,
                      @Nullable ContentValues contentValues,
                      @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case URI_PATTERN_ID_PETS:
                return updatePet(uri, contentValues, selection, selectionArgs);
            case URI_PATTERN_ID_PET_ID:
                selection = PetEntry.COLUMN_PET_ID + " = ?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updatePet(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for URI: " + uri);
        }
    }

    private int updatePet(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.size() == 0) return 0;

        validatePetDataOnUpdate(values);

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rowsUpdated = db.update(PetEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            Context context = getContext();
            if (context != null) {
                context.getContentResolver().notifyChange(uri, null);
            }
        }

        return rowsUpdated;
    }

    private Uri insertPet(Uri uri, ContentValues values) {
        validatePetDataOnInsert(values);

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long newId = db.insert(PetEntry.TABLE_NAME, null, values);
        if (newId != -1) {
            Log.d(LOG_TAG, "Inserted new pet in DB (id: " + newId + ").");

            Context context = getContext();
            if (context != null) {
                context.getContentResolver().notifyChange(uri, null);
            }

            return ContentUris.withAppendedId(uri, newId);
        } else {
            Log.e(LOG_TAG, "Failed to add pet to DB.");
            return null;
        }
    }

    private int deletePet(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rowsDeleted = db.delete(PetEntry.TABLE_NAME, selection, selectionArgs);

        if (rowsDeleted != 0) {
            Context context = getContext();
            if (context != null) {
                context.getContentResolver().notifyChange(uri, null);
            }
        }

        return rowsDeleted;
    }

    private void validatePetDataOnInsert(ContentValues values) {
        String name = values.getAsString(PetEntry.COLUMN_PET_NAME);
        if (TextUtils.isEmpty(name)) {
            throw new IllegalArgumentException("Pet must have a name.");
        }

        Integer gender = values.getAsInteger(PetEntry.COLUMN_PET_GENDER);
        if (gender == null || !PetEntry.isValidGender(gender)) {
            throw new IllegalArgumentException("Pet gender must be one of 0, 1 or 2. Got: " + gender);
        }

        Integer weight = values.getAsInteger(PetEntry.COLUMN_PET_WEIGHT);
        if (weight == null || weight < 0) {
            throw new IllegalArgumentException("Pet weight must be a positive integer value. Got: " + weight);
        }

        Log.d(LOG_TAG, "Pet data is valid for insert.");
    }

    private void validatePetDataOnUpdate(ContentValues values) {
        if (values.containsKey(PetEntry.COLUMN_PET_NAME)) {
            String name = values.getAsString(PetEntry.COLUMN_PET_NAME);
            if (TextUtils.isEmpty(name)) {
                throw new IllegalArgumentException("Pet must have a name.");
            }
        }

        if (values.containsKey(PetEntry.COLUMN_PET_GENDER)) {
            Integer gender = values.getAsInteger(PetEntry.COLUMN_PET_GENDER);
            if (gender == null || !PetEntry.isValidGender(gender)) {
                throw new IllegalArgumentException("Pet gender must be one of 0, 1 or 2. Got: " + gender);
            }
        }

        if (values.containsKey(PetEntry.COLUMN_PET_WEIGHT)) {
            Integer weight = values.getAsInteger(PetEntry.COLUMN_PET_WEIGHT);
            if (weight == null || weight < 0) {
                throw new IllegalArgumentException("Pet weight must be a positive integer value. Got: " + weight);
            }
        }

        Log.d(LOG_TAG, "Pet data is valid for update.");
    }
}
