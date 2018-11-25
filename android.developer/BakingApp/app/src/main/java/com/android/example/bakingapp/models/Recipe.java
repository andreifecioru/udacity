package com.android.example.bakingapp.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


/**
 * Implements the "recipe" abstraction.
 *
 * Has a name, servings and possibly a thumbnail.
 */
@Entity(tableName = "recipes")
public class Recipe implements Parcelable  {

    @SerializedName("id")
    @Expose
    @PrimaryKey
    @ColumnInfo(name = "id")
    private long mId;

    @SerializedName("name")
    @Expose
    @ColumnInfo(name = "name")
    private String mName;

    @SerializedName("servings")
    @Expose
    @ColumnInfo(name = "servings")
    private int mServings;

    @SerializedName("image")
    @Expose
    @ColumnInfo(name = "image_url")
    private String mImageUrl;

    @SerializedName("ingredients")
    @Expose
    @Ignore
    private List<Ingredient> mIngredients;

    @SerializedName("steps")
    @Expose
    @Ignore
    private List<Step> mSteps;

    public Recipe() { }

    /**
     * Getters/setters and other accessors.
     */
    public long getId() { return mId; }
    public void setId(long id) { mId = id; }

    public String getName() { return mName; }
    public void setName(String name) { mName = name; }

    public int getServings() { return mServings; }
    public void setServings(int servings) { mServings = servings; }

    public String getImageUrl() { return mImageUrl; }
    public void setImageUrl(String imageUrl) { mImageUrl = imageUrl; }

    public List<Ingredient> getIngredients() { return mIngredients; }
    public void setIngredients(List<Ingredient> ingredients) { mIngredients = ingredients; }

    public List<Step> getSteps() { return mSteps; }
    public void setSteps(List<Step> steps) { mSteps = steps; }

    /**
     * Provides a string representation of a recipe (JSON format).
     *
     * Used for debugging/logging purposes.
     *
     * @return A string representation of the recipe.
     */
    @Override
    public String toString() {
        return new StringBuilder("recipe: {\n")
                .append("  id: ").append(getId()).append(", \n")
                .append("  name: ").append(getName()).append(", \n")
                .append("  servings: ").append(getServings()).append(", \n")
                .append("  image URL: ").append(getImageUrl()).append(", \n")
                .append("  ingredient count: ").append(
                        getIngredients() == null ? 0 : getIngredients().size())
                .append(", \n")
                .append("  step count: ").append(
                        getSteps() == null ? 0 : getSteps().size())
                .append(" }")
                .toString();
    }

    /**
     * Parcelable protocol implementation.
     */
    public Recipe(Parcel in) {
        mId = in.readLong();
        mName = in.readString();
        mServings = in.readInt();
        mImageUrl = in.readString();
        mIngredients = in.createTypedArrayList(Ingredient.CREATOR);
        mSteps = in.createTypedArrayList(Step.CREATOR);
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeString(mName);
        dest.writeInt(mServings);
        dest.writeString(mImageUrl);
        dest.writeTypedList(mIngredients);
        dest.writeTypedList(mSteps);
    }

    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}
