package com.example.android.pets.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.example.android.pets.data.PetContract.PetEntry;
import com.example.android.pets.models.Pet;
import com.example.android.pets.models.Pets;

import java.util.ArrayList;
import java.util.List;

public class PetDbHelper extends SQLiteOpenHelper {
    private static final String LOG_TAG = PetDbHelper.class.getSimpleName();

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Pets.db";

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + PetEntry.TABLE_NAME + "(" +
            PetEntry.COLUMN_PET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            PetEntry.COLUMN_PET_NAME + " TEXT NOT NULL, " +
            PetEntry.COLUMN_PET_BREED + " TEXT, " +
            PetEntry.COLUMN_PET_GENDER + " INTEGER NOT NULL, " +
            PetEntry.COLUMN_PET_WEIGHT + " INTEGER NOT NULL DEFAULT 0 " +
            ");";

    private static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + PetEntry.TABLE_NAME + ";";

    private static final String SQL_DELETE_ENTRIES = "DELETE FROM " + PetEntry.TABLE_NAME + ";";


    public PetDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_TABLE);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void insertPets(List<Pet> pets) {
        for (Pet pet : pets) {
            insertPet(pet);
        }
    }

    public Pet insertPet(Pet pet) {
        ContentValues values = new ContentValues();
        values.put(PetEntry.COLUMN_PET_NAME, pet.getName());
        values.put(PetEntry.COLUMN_PET_BREED, pet.getBreed());
        values.put(PetEntry.COLUMN_PET_GENDER, pet.getGender());
        values.put(PetEntry.COLUMN_PET_WEIGHT, pet.getWeight());

        long newId = getWritableDatabase().insert(PetEntry.TABLE_NAME, null, values);
        if (newId != -1) {
            Log.d(LOG_TAG, "Inserted new pet in DB: " + pet.getName() + "(id: " + newId + ").");
            return Pets.fromPetWithId(pet, newId);
        } else {
            Log.e(LOG_TAG, "Failed to add pet to DB.");
            return null;
        }
    }

    public int countAllPets() {
        SQLiteDatabase db = getReadableDatabase();

        String [] projection = {
            PetEntry.COLUMN_PET_ID // we just grab the ID, because all we want is to count the rows
        };

        try (Cursor cursor = db.query(PetEntry.TABLE_NAME,
                projection,
                null,
                null, null, null, null, null)) {
           return cursor.getCount();
        }
    }

    public List<Pet> getAllPets() {
        List<Pet> result = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        String [] projection = {
                PetEntry.COLUMN_PET_ID,
                PetEntry.COLUMN_PET_NAME,
                PetEntry.COLUMN_PET_BREED,
                PetEntry.COLUMN_PET_GENDER,
                PetEntry.COLUMN_PET_WEIGHT
        };

        try (Cursor cursor = db.query(PetEntry.TABLE_NAME,
                projection,
                null,
                null, null, null, null, null)) {

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

    public void clearAllPets() {
        getWritableDatabase().execSQL(SQL_DELETE_ENTRIES);
    }
}
