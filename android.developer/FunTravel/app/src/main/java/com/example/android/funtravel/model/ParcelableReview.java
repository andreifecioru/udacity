package com.example.android.funtravel.model;

import android.os.Parcel;
import android.os.Parcelable;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;

import com.example.android.funtravel.common.model.Offer;
import com.example.android.funtravel.common.model.Review;

import static android.arch.persistence.room.ForeignKey.CASCADE;


/**
 * Parcelable protocol implementation over the {@link Review} model abstraction.
 */
@Entity(tableName = "reviews",
        indices = {@Index("offer_id")},
        foreignKeys = @ForeignKey(
                entity = ParcelableOffer.class,
                parentColumns = "id",
                childColumns = "offer_id",
                onDelete = CASCADE ))
public class ParcelableReview
        extends Review
        implements Parcelable {

    public ParcelableReview() { }

    public ParcelableReview(Parcel in) {
        setId(in.readLong());
        setAuthor(in.readString());
        setContent(in.readString());
        setRating(in.readFloat());
        setOfferId(in.readLong());
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(getId());
        dest.writeString(getAuthor());
        dest.writeString(getContent());
        dest.writeFloat(getRating());
        dest.writeLong(getOfferId());
    }

    public static final Parcelable.Creator<ParcelableReview> CREATOR = new Parcelable.Creator<ParcelableReview>() {
        @Override
        public ParcelableReview createFromParcel(Parcel in) {
            return new ParcelableReview(in);
        }

        @Override
        public ParcelableReview[] newArray(int size) {
            return new ParcelableReview[size];
        }
    };
}
