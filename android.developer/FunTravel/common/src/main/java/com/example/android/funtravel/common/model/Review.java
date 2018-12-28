package com.example.android.funtravel.common.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static android.arch.persistence.room.ForeignKey.CASCADE;

public class Review {

    @SerializedName("id")
    @PrimaryKey
    @Expose
    @ColumnInfo(name = "id")
    private long mId;

    @SerializedName("author")
    @Expose
    @ColumnInfo(name = "author")
    private String mAuthor;

    @SerializedName("content")
    @Expose
    @ColumnInfo(name = "content")
    private String mContent;

    @SerializedName("rating")
    @Expose
    @ColumnInfo(name = "rating")
    private float mRating;

    @ColumnInfo(name = "offer_id")
    private long mOfferId;


    public Review() { }

    /**
     * Getters/setters and other accessors.
     */
    public long getId() { return mId; }
    public void setId(long id) { mId = id; }

    public String getAuthor() { return mAuthor; }
    public void setAuthor(String author) { mAuthor = author; }

    public String getContent() { return mContent; }
    public void setContent(String content) { mContent = content; }

    public float getRating() { return mRating; }
    public void setRating(float rating) { mRating = rating; }

    public long getOfferId() { return mOfferId; }
    public void setOfferId(long offerId) { mOfferId = offerId; }


    /**
     * Provides a string representation of an offer (JSON-like format).
     *
     * Used for debugging/logging purposes.
     *
     * @return A string representation of the offer.
     */
    @Override
    public String toString() {
        return new StringBuilder("review: {\n")
                .append("  id: ").append(getId()).append(", \n")
                .append("  author: ").append(getAuthor()).append(", \n")
                .append("  rating: ").append(getRating()).append(", \n")
                .append("  offer id: ").append(getOfferId())
                .append(" }")
                .toString();
    }
}
