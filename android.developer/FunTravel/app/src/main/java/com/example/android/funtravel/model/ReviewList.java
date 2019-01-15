package com.example.android.funtravel.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.example.android.funtravel.common.model.Review;


/**
 * Wrapper container over the {@link Review} abstraction needed to help retrofit
 * parse the backend response which has the form:
 *
 * { reviews: [...] }
 */
public class ReviewList {
    @SerializedName("reviews")
    @Expose
    private List<? extends ParcelableReview> mReviews;

    public List<? extends ParcelableReview> getReviews() { return mReviews; }
}
