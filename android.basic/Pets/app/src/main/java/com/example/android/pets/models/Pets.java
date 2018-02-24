package com.example.android.pets.models;

import java.util.ArrayList;
import java.util.List;

import com.example.android.pets.data.PetContract.PetEntry;


final public class Pets {
    public static List<Pet> generateDummyData() {
        List<Pet> result = new ArrayList<>();

        result.add(new Pet("Garfield", "Tabby", PetEntry.GENDER_MALE, 7));
        result.add(new Pet("Lady", "Tabby", PetEntry.GENDER_FEMALE, 4));
        result.add(new Pet("Spike", "Terrier", PetEntry.GENDER_MALE, 10));
        result.add(new Pet("Rex", "Bulldog", PetEntry.GENDER_MALE, 17));

        return result;
    }
    public static Pet fromPetWithId(Pet pet, long id) {
        return new Pet(
            id,
            pet.getName(),
            pet.getBreed(),
            pet.getGender(),
            pet.getWeight()
        );
    }
}
