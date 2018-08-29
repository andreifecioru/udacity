package com.example.android.popularmovies.models;

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

import java.io.Serializable;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Models the "movie-review" abstraction.
 *
 * Has an author, some review text (i.e content) and is linked via a one-to-many
 * relationship with a {@link Movie} instance. At the DB level, this relationship is
 * enforced via a foreign-key constraint.
 */
@Entity(tableName = "reviews",
        indices = {@Index("movie_id")},
        foreignKeys = @ForeignKey(
                entity = Movie.class,
                parentColumns = "id",
                childColumns = "movie_id",
                onDelete = CASCADE))
public class Review implements Parcelable {
    @SerializedName("id")
    @Expose
    @PrimaryKey
    @ColumnInfo(name = "id")
    @NonNull
    private String mId = "";

    @SerializedName("author")
    @Expose
    @ColumnInfo(name = "author")
    private String mAuthor;

    @SerializedName("content")
    @Expose
    @ColumnInfo(name = "content")
    private String mContent;

    @ColumnInfo(name = "movie_id")
    private long mMovieId;

    /**
     * Getters/setters and other accessors.
     */
    public String getId() { return mId; }
    public void setId(String id) { mId = id; }

    public String getAuthor() { return mAuthor; }
    public void setAuthor(String author) { mAuthor = author; }

    public String getContent() { return mContent; }
    public void setContent(String content) { mContent = content; }

    public long getMovieId() { return mMovieId; }
    public void setMovieId(long movieId) { mMovieId = movieId; }

    public Review() {}

    /**
     * Provides a string representation of a review (JSON format).
     *
     * Used for debugging/logging purposes.
     *
     * @return A string representation of the review.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("review: {\n");
        sb.append("  id: ").append(getId()).append(", \n");
        sb.append("  author: ").append(getAuthor()).append(", \n");
        sb.append("  content: ").append(getContent()).append(", \n");
        sb.append("  movie id: ").append(getMovieId()).append(", \n");

        return sb.toString();
    }

    /**
     * Parcelable protocol implementation.
     */
    public Review(Parcel in) {
        mId = in.readString();
        mAuthor = in.readString();
        mContent = in.readString();
        mMovieId = in.readLong();
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mAuthor);
        dest.writeString(mContent);
        dest.writeLong(mMovieId);
    }

    public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };
}
