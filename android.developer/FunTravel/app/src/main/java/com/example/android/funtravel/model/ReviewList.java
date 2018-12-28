package com.example.android.funtravel.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ReviewList {
    @SerializedName("reviews")
    @Expose
    private List<? extends ParcelableReview> mReviews;

    public List<? extends ParcelableReview> getReviews() { return mReviews; }
}
