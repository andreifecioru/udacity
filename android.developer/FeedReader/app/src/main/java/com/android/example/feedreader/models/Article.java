package com.android.example.feedreader.models;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Implements the "article" abstraction.
 *
 * Has a title, author, body and possibly a thumbnail and a photo.
 */
@Entity(tableName = "articles")
public class Article implements Parcelable {
    private static final String LOG_TAG = Article.class.getSimpleName();

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long mId;

    @SerializedName("title")
    @Expose
    @ColumnInfo(name = "title")
    private String mTitle;

    @SerializedName("author")
    @Expose
    @ColumnInfo(name = "author")
    private String mAuthor;

    @SerializedName("body")
    @Expose
    @ColumnInfo(name = "body")
    private String mBody;

    @SerializedName("thumb")
    @Expose
    @ColumnInfo(name = "thumb_url")
    private String mThumbUrl;

    @SerializedName("photo")
    @Expose
    @ColumnInfo(name = "photo_url")
    private String mPhotoUrl;

    @SerializedName("aspect_ratio")
    @Expose
    @ColumnInfo(name = "aspect_ratio")
    private float mAspectRatio;

    @SerializedName("published_date")
    @Expose
    @ColumnInfo(name = "published_at")
    private String mPublishedAt;

    public Article() { }


    @Ignore
    private final SimpleDateFormat IN_DATE_FMT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss", Locale.US);

    @Ignore
    private SimpleDateFormat OUT_DATE_FMT = new SimpleDateFormat();

    // Most time functions can only handle 1902 - 2037
    @Ignore
    private GregorianCalendar START_OF_EPOCH = new GregorianCalendar(2,1,1);

    /**
     * Getters/setters and other accessors.
     */

    public long getId() { return mId; }
    public String getTitle() { return mTitle; }
    public String getAuthor() { return mAuthor; }
    public String getBody() { return mBody; }
    public String getThumbUrl() { return mThumbUrl; }
    public String getPhotoUrl() { return mPhotoUrl; }
    public float getAspectRatio() { return mAspectRatio; }
    public String getPublishedAt() { return mPublishedAt; }


    public String getSubtitle(String lineSeparator) {
        // Set the sub-title
        Date publishedDate = parsePublishedDate();
        if (!publishedDate.before(START_OF_EPOCH.getTime())) {
            return Html.fromHtml(
                    DateUtils.getRelativeTimeSpanString(
                            publishedDate.getTime(),
                            System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
                            DateUtils.FORMAT_ABBREV_ALL).toString()
                            + lineSeparator + " by "
                            + getAuthor()).toString();
        } else {
            return Html.fromHtml(
                    OUT_DATE_FMT.format(publishedDate)
                            + lineSeparator + " by "
                            + getAuthor()).toString();
        }
    }

    public void setId(long id) { mId = id; }
    public void setTitle(String title) { mTitle = title; }
    public void setAuthor(String author) { mAuthor = author; }
    public void setBody(String body) { mBody = body; }
    public void setThumbUrl(String thumbUrl) { mThumbUrl = thumbUrl; }
    public void setPhotoUrl(String photoUrl) { mPhotoUrl = photoUrl; }
    public void setAspectRatio(float aspectRatio) { mAspectRatio = aspectRatio; }
    public void setPublishedAt(String publishedAt) { mPublishedAt = publishedAt; }

    /**
     * Provides a string representation of an article (JSON-like format).
     *
     * Used for debugging/logging purposes.
     *
     * @return A string representation of the article.
     */
    @Override
    public String toString() {
        return new StringBuilder("article: {\n")
                .append("  id: ").append(getId()).append(", \n")
                .append("  title: ").append(getTitle()).append(", \n")
                .append("  author: ").append(getAuthor()).append(", \n")
                .append("  body len: ").append(getBody().length()).append(", \n")
                .append("  thumb URL: ").append(getThumbUrl()).append(", \n")
                .append("  photo URL: ").append(getPhotoUrl()).append(", \n")
                .append("  aspect ratio: ").append(getAspectRatio()).append(", \n")
                .append("  published at:").append(getPublishedAt())
                .append(" }")
                .toString();
    }

    // Helper methods
    private Date parsePublishedDate() {
        try {
            return IN_DATE_FMT.parse(getPublishedAt());
        } catch (ParseException ex) {
            Log.e(LOG_TAG, ex.getMessage());

            Log.i(LOG_TAG, "passing today's date");
            return new Date();
        }
    }
    /**
     * Parcelable protocol implementation.
     */
    protected Article(Parcel in) {
        mId = in.readLong();
        mTitle = in.readString();
        mAuthor = in.readString();
        mBody = in.readString();
        mThumbUrl = in.readString();
        mPhotoUrl = in.readString();
        mAspectRatio = in.readFloat();
        mPublishedAt = in.readString();
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeString(mTitle);
        dest.writeString(mAuthor);
        dest.writeString(mBody);
        dest.writeString(mThumbUrl);
        dest.writeString(mPhotoUrl);
        dest.writeFloat(mAspectRatio);
        dest.writeString(mPublishedAt);
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

}
