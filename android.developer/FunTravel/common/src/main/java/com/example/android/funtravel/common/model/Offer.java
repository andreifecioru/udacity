package com.example.android.funtravel.common.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Offer {
    public static final int MAX_RATING = 5;

    @SerializedName("id")
    @PrimaryKey
    @Expose
    @ColumnInfo(name = "id")
    long mId;

    @SerializedName("title")
    @Expose
    @ColumnInfo(name = "title")
    private String mTitle;

    @SerializedName("description")
    @Expose
    @ColumnInfo(name = "description")
    private String mDescription;

    @SerializedName("type")
    @Expose
    @ColumnInfo(name = "type")
    private String mType;

    @SerializedName("price")
    @Expose
    @ColumnInfo(name = "price")
    private float mPrice;

    @SerializedName("avgRating")
    @Expose
    @ColumnInfo(name = "avg_rating")
    private float mAvgRating;

    @SerializedName("photoUrl")
    @Expose
    @ColumnInfo(name = "photo_url")
    private String mPhotoUrl;

    @SerializedName("aspectRatio")
    @Expose
    @ColumnInfo(name = "aspect_ratio")
    private float mAspectRatio;

    @SerializedName("videoUrl")
    @Expose
    @ColumnInfo(name = "video_url")
    private String mVideoUrl;


    public Offer() { }

    /**
     * Getters/setters and other accessors.
     */
    public long getId() { return mId; }
    public void setId(long id) { mId = id; }

    public String getTitle() { return mTitle; }
    public void setTitle(String title) { mTitle = title; }

    public String getDescription() { return mDescription; }
    public void setDescription(String description) { mDescription = description; }

    public String getType() { return mType; }
    public OfferType getTypeAsEnum() { return OfferType.fromString(mType); }
    public void setType(String type) { mType = type; }

    public float getPrice() { return mPrice; }
    public void setPrice(float price) { mPrice = price; }

    public float getAvgRating() { return mAvgRating; }
    public void setAvgRating(float avgRating) { mAvgRating = avgRating; }

    public String getPhotoUrl() { return mPhotoUrl; }
    public void setPhotoUrl(String photoUrl) { mPhotoUrl = photoUrl; }

    public float getAspectRatio() { return mAspectRatio; }
    public void setAspectRatio(float aspectRatio) { mAspectRatio = aspectRatio; }

    public String getVideoUrl() { return mVideoUrl; }
    public void setVideoUrl(String videoUrl) { mVideoUrl = videoUrl; }


    /**
     * Provides a string representation of an offer (JSON-like format).
     *
     * Used for debugging/logging purposes.
     *
     * @return A string representation of the offer.
     */
    @Override
    public String toString() {
        return new StringBuilder("offer: {\n")
                .append("  id: ").append(getId()).append(", \n")
                .append("  title: ").append(getTitle()).append(", \n")
                .append("  description: ").append(getDescription()).append(", \n")
                .append("  type: ").append(getType()).append(", \n")
                .append("  price: ").append(getPrice()).append(", \n")
                .append("  avg. rating: ").append(getAvgRating()).append(", \n")
                .append("  photo url: ").append(getPhotoUrl()).append(", \n")
                .append("  aspect ratio:").append(getAspectRatio()).append(", \n")
                .append("  video url: ").append(getVideoUrl())
                .append(" }")
                .toString();
    }

}
