package com.example.android.pets.models;

import android.text.TextUtils;

import com.example.android.pets.data.PetContract.PetEntry;

import org.w3c.dom.Text;

import java.util.Locale;


final public class Pet {
    final private long mId;
    final private String mName;
    final private String mBreed;
    final private int mGender;
    final private int mWeight;


    public Pet(long id, String name, String breed, int gender, int weight) {
        if (TextUtils.isEmpty(name)) {
            throw new IllegalArgumentException("Pet name cannot be empty.");
        }

        if (gender != PetEntry.GENDER_MALE &&
            gender != PetEntry.GENDER_FEMALE &&
            gender != PetEntry.GENDER_UNKNOWN) {
            throw new IllegalArgumentException("Unsupported gender value: " + gender);
        }

        mId = id;
        mName = name;
        mBreed = breed;
        mGender = gender;
        mWeight = weight;
    }

    public Pet(String name, String breed, int gender, int weight) {
        this(0, name, breed, gender, weight);
    }

    public long getId() { return mId; }

    public String getName() { return mName; }

    public String getBreed() { return mBreed; }

    public int getGender() { return mGender; }

    public int getWeight() { return mWeight; }

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "%d - %s - %s - %d - %d", mId, mName, mBreed, mGender, mWeight);
    }
}
