package com.example.android.funtravel.model;

import androidx.room.Entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.android.funtravel.common.model.Offer;


/**
 * Parcelable protocol implementation over the {@link Offer} model abstraction.
 *
 * We don't want to have {@link Parcelable} implementation part of the base {@link Offer} in "common"
 * definition because this is a marshaling mechanism used to pass info between various parts of
 * the app (it's an app-related concern).
 */
@Entity(tableName = "offers")
public class ParcelableOffer
        extends Offer
        implements Parcelable {

    public ParcelableOffer() { }

    public ParcelableOffer(Parcel in) {
        setId(in.readLong());
        setTitle(in.readString());
        setDescription(in.readString());
        setType(in.readString());
        setPrice(in.readFloat());
        setAvgRating(in.readFloat());
        setPhotoUrl(in.readString());
        setAspectRatio(in.readFloat());
        setVideoUrl(in.readString());
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(getId());
        dest.writeString(getTitle());
        dest.writeString(getDescription());
        dest.writeString(getType());
        dest.writeFloat(getPrice());
        dest.writeFloat(getAvgRating());
        dest.writeString(getPhotoUrl());
        dest.writeFloat(getAspectRatio());
        dest.writeString(getVideoUrl());
    }

    public static final Parcelable.Creator<ParcelableOffer> CREATOR = new Parcelable.Creator<ParcelableOffer>() {
        @Override
        public ParcelableOffer createFromParcel(Parcel in) {
            return new ParcelableOffer(in);
        }

        @Override
        public ParcelableOffer[] newArray(int size) {
            return new ParcelableOffer[size];
        }
    };
}
