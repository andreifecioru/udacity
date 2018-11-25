package com.android.example.bakingapp.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static android.arch.persistence.room.ForeignKey.CASCADE;


/**
 * Implements the "ingredient" abstraction.
 *
 * Has a name, a quantity and a measure.
 */
@Entity(tableName = "ingredients",
        indices = {@Index("recipe_id")},
        foreignKeys = @ForeignKey(
                entity = Recipe.class,
                parentColumns = "id",
                childColumns = "recipe_id",
                onDelete = CASCADE))
public class Ingredient implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long mId;

    @SerializedName("ingredient")
    @Expose
    @ColumnInfo(name = "name")
    private String mName;

    @SerializedName("quantity")
    @Expose
    @ColumnInfo(name = "quantity")
    private float mQuantity;

    @SerializedName("measure")
    @Expose
    @ColumnInfo(name = "measure")
    private String mMeasure;

    @ColumnInfo(name = "recipe_id")
    private long mRecipeId;

    public Ingredient() {}

    /**
     * Getters/setters and other accessors.
     */
    public long getId() { return mId; }
    public void setId(long id) { mId = id; }

    public String getName() { return mName; }
    public void setName(String name) { mName = name; }

    public float getQuantity() { return mQuantity; }
    public void setQuantity(float quantity) { mQuantity = quantity; }

    public String getMeasure() { return mMeasure; }
    public void setMeasure(String measure) { mMeasure = measure; }

    public long getRecipeId() { return mRecipeId; }
    public void setRecipeId(long recipeId) { mRecipeId = recipeId; }

    /**
     * Provides a string representation of an ingredient.
     *
     * Used for debugging/logging purposes.
     *
     * @return A string representation of the ingredient.
     */
    @Override
    public String toString() {
        return new StringBuilder("ingredient: {")
                .append("  id: ").append(getId()).append(", ")
                .append("  recipe id: ").append(getRecipeId()).append(", ")
                .append("  name: ").append(getName())
                .append(" }")
                .toString();
    }

    /**
     * Parcelable protocol implementation.
     */
    public Ingredient(Parcel in) {
        mId = in.readLong();
        mName = in.readString();
        mQuantity = in.readFloat();
        mMeasure = in.readString();
        mRecipeId = in.readLong();
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeString(mName);
        dest.writeFloat(mQuantity);
        dest.writeString(mMeasure);
        dest.writeLong(mRecipeId);
    }

    public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
}
