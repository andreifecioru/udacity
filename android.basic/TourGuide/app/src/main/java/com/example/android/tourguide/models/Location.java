package com.example.android.tourguide.models;

import java.io.Serializable;


/**
 * Implements the "location of interest" abstraction.
 * A location is defined by a name, a description,
 * a rating and various media resources (like images
 * and audio).
 */
public final class Location implements Serializable {
    /** Indicates that no audio resource is associated with the location */
    final static int NO_AUDIO_RES_ID = -1;

    private final String mName;
    private final String mDescription;
    private final int mRating;
    private final int mImageResId;
    private final int mAudioResId;

    /**
     * Creates a {@link Location} instance (constructor).
     *
     * @param name The name of the location.
     * @param description A short description for the location.
     * @param rating Integer (1-5) defining the rating for the location.
     * @param imageResId Integer pointing to the image resource for the location.
     * @param audioResId Integer pointing to the audio resource for the location.
     *                 If no audio is available, use {@link Location.NO_AUDIO_RES_ID}
     */
    Location(String name, String description, int rating, int imageResId, int audioResId) {
        // enforce invariants
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating value must be between 1 ang 5.");
        }

        if (imageResId < 0) {
            throw new IllegalArgumentException("Image resource ID must be a positive integer value.");
        }

        // set the state
        mName = name;
        mDescription = description;
        mRating = rating;
        mImageResId = imageResId;
        mAudioResId = audioResId;
    }

    /**
     * (Getter) Returns the name for the location.
     *
     * @return The location's name.
     */
    public String getName() { return mName; }

    /**
     * (Getter) Returns the description for the location.
     *
     * @return The location's description.
     */
    public String getDescription() { return mDescription; }

    /**
     * (Getter) Returns the description for the location.
     *
     * @return The location's description.
     */
    public int getRating() { return mRating; }

    /**
     * (Getter) Returns the res. ID for the image associated with the location.
     *
     * @return The resource ID for the location's image.
     */
    public int getImageResId() { return mImageResId; }

    /**
     * (Getter) Returns the res. ID for the audio associated with the location.
     *
     * @return The resource ID for the location's audio.
     */
    public int getAudioResId() { return mAudioResId; }

    /**
     * Check if the location has an associated audio resource.
     *
     * @return {@link true} if the location has audio {@link false} otherwise.
     */
    public boolean hasAudioResource() { return mAudioResId != -1; }
}

