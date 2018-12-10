package com.android.example.jokedisplay.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Joke implements Parcelable {
    @SerializedName("data")
    @Expose
    private String mText;

    @Override
    public String toString() {
        return mText;
    }

    /**
     * Getters/setters and other accessors.
     */
    public String getText() { return mText; }
    public void setText(String text) { mText = text; }

    /**
     * Parcelable protocol implementation.
     */
    public Joke(Parcel in) {
        mText = in.readString();
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mText);
    }

    public static final Parcelable.Creator<Joke> CREATOR = new Parcelable.Creator<Joke>() {
        @Override
        public Joke createFromParcel(Parcel in) {
            return new Joke(in);
        }

        @Override
        public Joke[] newArray(int size) {
            return new Joke[size];
        }
    };
}
