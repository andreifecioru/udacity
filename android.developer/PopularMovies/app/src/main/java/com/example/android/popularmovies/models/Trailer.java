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
 * Models the "movie trailer" abstraction.
 *
 * Has a title, a key, a type, etc. It is linked via a one-to-many
 * relationship with a {@link Movie} instance. At the DB level, this relationship is
 * enforced via a foreign-key constraint.
 */
@Entity(tableName = "trailers",
        indices = {@Index("movie_id")},
        foreignKeys = @ForeignKey(
                entity = Movie.class,
                parentColumns = "id",
                childColumns = "movie_id",
                onDelete = CASCADE))
public class Trailer implements Parcelable {
    @SerializedName("id")
    @Expose
    @PrimaryKey
    @ColumnInfo(name = "id")
    @NonNull
    private String mId = "";

    @SerializedName("key")
    @Expose
    @ColumnInfo(name = "key")
    private String mKey;

    @SerializedName("name")
    @Expose
    @ColumnInfo(name = "name")
    private String mName;

    @SerializedName("site")
    @Expose
    @ColumnInfo(name = "site")
    private String mSite;

    @SerializedName("type")
    @Expose
    @ColumnInfo(name = "type")
    private String mType;

    @ColumnInfo(name = "movie_id")
    private long mMovieId;

    public Trailer() {}

    /**
     * Getters/setters and other accessors.
     */
    public String getId() { return mId; }
    public void setId(String id) { mId = id; }

    public String getKey() { return mKey; }
    public void setKey(String key) { mKey = key; }

    public String getName() { return mName; }
    public void setName(String name) { mName = name; }

    public String getSite() { return mSite; }
    public void setSite(String site) { mSite = site; }

    public String getType() { return mType; }
    public void setType(String type) { mType = type; }

    public long getMovieId() { return mMovieId; }
    public void setMovieId(long movieId) { mMovieId = movieId; }

    /**
     * Provides a string representation of a trailer (JSON format).
     *
     * Used for debugging/logging purposes.
     *
     * @return A string representation of the trailer.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("trailer: {\n");
        sb.append("  id: ").append(getId()).append(", \n");
        sb.append("  key: ").append(getKey()).append(", \n");
        sb.append("  name: ").append(getName()).append(", \n");
        sb.append("  site: ").append(getSite()).append(", \n");
        sb.append("  movie id: ").append(getMovieId()).append(", \n");

        return sb.toString();
    }

    /**
     * Parcelable protocol implementation.
     */
    public Trailer(Parcel in) {
        mId = in.readString();
        mKey = in.readString();
        mName = in.readString();
        mSite = in.readString();
        mType = in.readString();
        mMovieId = in.readLong();
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mKey);
        dest.writeString(mName);
        dest.writeString(mSite);
        dest.writeString(mType);
        dest.writeLong(mMovieId);
    }

    public static final Parcelable.Creator<Trailer> CREATOR = new Parcelable.Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };
}
