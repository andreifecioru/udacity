package com.android.example.bakingapp.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static android.arch.persistence.room.ForeignKey.CASCADE;


/**
 * Implements the "recipe-step" abstraction.
 *
 * Has a number, description and possibly a video URL.
 */
@Entity(tableName = "steps",
        indices = {@Index("recipe_id")},
        foreignKeys = @ForeignKey(
                entity = Recipe.class,
                parentColumns = "id",
                childColumns = "recipe_id",
                onDelete = CASCADE))
public class Step implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long mId;

    @SerializedName("id")
    @Expose
    @ColumnInfo(name = "step_no")
    private int mStepNo;

    @SerializedName("shortDescription")
    @Expose
    @ColumnInfo(name = "short_des")
    private String mShortDescription;

    @SerializedName("description")
    @Expose
    @ColumnInfo(name = "full_des")
    private String mDescription;

    @SerializedName("videoURL")
    @Expose
    @ColumnInfo(name = "video_url")
    private String mVideoUrl;

    @SerializedName("thumbnailURL")
    @Expose
    @ColumnInfo(name = "thumb_url")
    private String mThumbnailUrl;

    @ColumnInfo(name = "recipe_id")
    private long mRecipeId;

    public Step() { }

    /**
     * Getters/setters and other accessors.
     */
    public long getId() { return mId; }
    public void setId(long id) { mId = id; }

    public int getStepNo() { return mStepNo; }
    public void setStepNo(int stepNo) { mStepNo = stepNo; }

    public String getShortDescription() { return mShortDescription; }
    public void setShortDescription(String shortDescription) { mShortDescription = shortDescription; }

    public String getDescription() { return mDescription; }
    public void setDescription(String description) { mDescription = description; }

    public String getVideoUrl() { return mVideoUrl; }
    public void setVideoUrl(String videoUrl) { mVideoUrl = videoUrl; }

    public String getThumbnailUrl() { return mThumbnailUrl; }
    public void setThumbnailUrl(String thumbnailUrl) { mThumbnailUrl = thumbnailUrl; }

    public long getRecipeId() { return mRecipeId; }
    public void setRecipeId(long recipeId) { mRecipeId = recipeId; }

    /**
     * Provides a string representation of a recipe-step.
     *
     * Used for debugging/logging purposes.
     *
     * @return A string representation of the recipe-step.
     */
    @Override
    public String toString() {
        return new StringBuilder("step: {")
                .append("  id: ").append(getId()).append(", ")
                .append("  recipe id: ").append(getRecipeId()).append(", ")
                .append("  video url: ").append(getVideoUrl())
                .append(" }")
                .toString();
    }

    /**
     * Parcelable protocol implementation.
     */
    public Step(Parcel in) {
        mId = in.readLong();
        mStepNo = in.readInt();
        mDescription = in.readString();
        mVideoUrl = in.readString();
        mThumbnailUrl = in.readString();
        mRecipeId = in.readLong();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeInt(mStepNo);
        dest.writeString(mDescription);
        dest.writeString(mVideoUrl);
        dest.writeString(mThumbnailUrl);
        dest.writeLong(mRecipeId);
    }

    public static final Parcelable.Creator<Step> CREATOR = new Parcelable.Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };
}

