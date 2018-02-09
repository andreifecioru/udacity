package com.android.example.guardiannews.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * Implements the "news item" abstraction which maps on
 * one article/post retrieved from the Guardian API.
 * It stores info such as title, author, category (i.e. pillar)
 * and publishing time.
 */
public class NewsItem implements Parcelable {
    private final String mTitle;
    private final String mAuthorName;
    private final String mPillar;
    private final String mPublishDate;
    private final String mPublishTime;
    private final String mUrl;

    /*
     * Mapping between the month number (as provided by the Guardian API)
     * and the abbreviated (i.e 3-letter) month name.
     */
    private static final Map<String, String> monthNames;
    static
    {
        monthNames = new HashMap<>();
        monthNames.put("01", "Jan");
        monthNames.put("02", "Feb");
        monthNames.put("03", "Mar");
        monthNames.put("04", "Apr");
        monthNames.put("05", "May");
        monthNames.put("06", "Jun");
        monthNames.put("07", "Jul");
        monthNames.put("08", "Aug");
        monthNames.put("09", "Sep");
        monthNames.put("10", "Oct");
        monthNames.put("11", "Nov");
        monthNames.put("12", "Dec");
    }

    /**
     * Creates a {@link NewsItem} instance (constructor).
     *
     * @param title The title of the post.
     * @param authorName The name of the post's author. If not available, it is set to 'Unknown'.
     * @param publishTimestamp The publishing time in the format provided by the Guardian API (i.e.  yyyy-mm-ddTHH:MM:SSZ)
     * @param pillar The pillar (i.e. category) to which the post belongs.
     */
    NewsItem(String title, String authorName, String publishTimestamp, String pillar, String url) {
        mTitle = title;
        mAuthorName = authorName;
        mPillar = pillar;
        mUrl = url;

        // parse the publish timestamp (input format: 2018-02-09T13:42:05Z)
        String[] tokens = publishTimestamp.split("T");

        String dateString = tokens[0];
        String timeString = tokens[1];

        // parse the date (we want the '01 Jan, 2000' format)
        tokens = dateString.split("-");
        String year = tokens[0];
        String month = tokens[1];
        String day = tokens[2];

        mPublishDate = String.format(Locale.ENGLISH, "%s %s, %s", day, monthNames.get(month), year);

        // parse the time (we want the AM/PM format)
        tokens = timeString.split(":");
        int hour = Integer.parseInt(tokens[0]);
        String amPm = (hour < 12) ? "AM": "PM";
        String mins = tokens[1];

        mPublishTime = String.format(Locale.ENGLISH, "%d %s %s", hour, mins, amPm);
    }

    /**
     * (Getter) Returns the post title.
     *
     * @return The post's title.
     */
    public String getTitle() { return mTitle; }

    /**
     * (Getter) Returns the post pillar (i.e. category).
     *
     * @return The post's pillar.
     */
    public String getPillar() { return mPillar; }

    /**
     * (Getter) Returns the post pillar (i.e. category).
     *
     * @return The post's pillar.
     */
    public String getAuthorName() { return mAuthorName; }

    /**
     * (Getter) Returns the post publishing date (format: 01 Jan, 2000)
     *
     * @return The post's publishing date.
     */
    public String getPublishDate() { return mPublishDate; }

    /**
     * (Getter) Returns the post publishing time (format: 3:45 PM).
     *
     * @return The post's publishing time.
     */
    public String getPublishTime() { return mPublishTime; }

    /**
     * (Getter) Returns the post web URL.
     *
     * @return The post's web URL.
     */
    public String getUrl() { return mUrl; }

    // Parcelling logic
    NewsItem(Parcel in) {
        String[] data = new String[6];

        in.readStringArray(data);
        mTitle= data[0];
        mAuthorName = data[1];
        mPillar = data[2];
        mPublishDate = data[3];
        mPublishTime = data[4];
        mUrl = data[5];

    }

    public int describeContents(){
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
                mTitle,
                mAuthorName,
                mPillar,
                mPublishDate,
                mPublishTime,
                mUrl});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public NewsItem createFromParcel(Parcel in) {
            return new NewsItem(in);
        }

        public NewsItem[] newArray(int size) {
            return new NewsItem[size];
        }
    };
}
