package com.example.android.popularmovies.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.android.popularmovies.di.module.NetModule;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;



/**
 * Implements the "movie" abstraction.
 * For our purposes, a movie is defined by a title, synopsis,
 * user rating, etc.
 */
@Entity(tableName = "movies")
public class Movie implements Parcelable {
    static final public int MAX_RATING = 5;

    @SerializedName("id")
    @Expose
    @PrimaryKey
    @ColumnInfo(name = "id")
    private long mId;

    @SerializedName("title")
    @Expose
    @ColumnInfo(name = "title")
    private String mTitle;

    @SerializedName("poster_path")
    @Expose
    @ColumnInfo(name = "poster_path")
    private String mPosterPath;

    @SerializedName("backdrop_path")
    @Expose
    @ColumnInfo(name = "backdrop_path")
    private String mBackdropPath;

    @SerializedName("overview")
    @Expose
    @ColumnInfo(name = "synopsis")
    private String mSynopsis;

    @SerializedName("vote_average")
    @Expose
    @ColumnInfo(name = "user_rating")
    private double mUserRating;

    @SerializedName("popularity")
    @Expose
    @ColumnInfo(name = "popularity")
    private double mPopularity;

    @SerializedName("release_date")
    @Expose
    @ColumnInfo(name = "release_date")
    private String mReleaseDate;

    @ColumnInfo(name = "is_favourite")
    private boolean mIsFavourite;

    @ColumnInfo(name = "sort_by")
    private String mSortBy;

    public Movie() {}

    /**
     * Getters/setters and other accessors.
     */
    public long getId() { return mId; }
    public void setId(long id) { mId = id; }

    public String getTitle() { return mTitle; }
    public void setTitle(String title) { mTitle = title; }

    public String getPosterPath() { return mPosterPath; }
    public void setPosterPath(String posterPath) { mPosterPath = posterPath; }

    public String getPosterUrl() {
        if (mPosterPath == null) return "";

        return NetModule.to(NetModule.MOVIE_DB_URI_BUILDER_POSTER)
                .appendPath(mPosterPath.replace("/", ""))
                .build()
                .toString();
    }

    public String getBackdropPath() { return mBackdropPath; }
    public void setBackdropPath(String backdropPath) { mBackdropPath = backdropPath; }

    public String getBackdropUrl() {
        if (mBackdropPath == null) return "";

        return NetModule.to(NetModule.MOVIE_DB_URI_BUILDER_BACKDROP)
                .appendPath(mBackdropPath.replace("/", ""))
                .build()
                .toString();
    }

    public String getSynopsis() { return mSynopsis; }
    public void setSynopsis(String synopsis) { mSynopsis = synopsis; }

    public double getUserRating() { return mUserRating; }
    public void setUserRating(double userRating) { mUserRating = userRating; }

    public double getPopularity() { return mPopularity; }
    public void setPopularity(double popularity) { mPopularity = popularity; }

    public String getReleaseDate() { return mReleaseDate; }
    public void setReleaseDate(String releaseDate) { mReleaseDate = releaseDate; }

    public boolean getIsFavourite() { return mIsFavourite; }
    public void setIsFavourite(boolean isFavourite) { mIsFavourite = isFavourite; }

    public void toggleIsFavourite() { mIsFavourite = !mIsFavourite; }

    public String getSortBy() { return mSortBy; }
    public void setSortBy(String sortBy) { mSortBy = sortBy; }

    /**
     * Provides a string representation of a movie (JSON format).
     *
     * Used for debugging/logging purposes.
     *
     * @return A string representation of the movie.
     */
    @Override
    public String toString() {
        return new StringBuilder("movie: {\n")
            .append("  id: ").append(getId()).append(", \n")
            .append("  title: ").append(getTitle()).append(", \n")
            .append("  poster: ").append(getPosterUrl()).append(", \n")
            .append("  backdrop: ").append(getBackdropUrl()).append(", \n")
            .append("  rating: ").append(getUserRating()).append(", \n")
            .append("  popularity: ").append(getPopularity()).append(", \n")
            .append("  is favourite: ").append(getIsFavourite()).append(", \n")
            .append("  sort by: ").append(getSortBy()).append(", \n")
            .append("  release date: ").append(getReleaseDate()).append("}")
        .toString();
    }

    /**
     * Parcelable protocol implementation.
     */
    public Movie(Parcel in) {
        mId = in.readLong();
        mTitle = in.readString();
        mPosterPath = in.readString();
        mBackdropPath = in.readString();
        mSynopsis = in.readString();
        mUserRating = in.readDouble();
        mPopularity = in.readDouble();
        mReleaseDate = in.readString();
        mIsFavourite = in.readByte() == 1;
        mSortBy = in.readString();
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeString(mTitle);
        dest.writeString(mPosterPath);
        dest.writeString(mBackdropPath);
        dest.writeString(mSynopsis);
        dest.writeDouble(mUserRating);
        dest.writeDouble(mPopularity);
        dest.writeString(mReleaseDate);
        dest.writeByte(mIsFavourite ? (byte) 1 : (byte) 0);
        dest.writeString(mSortBy);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
