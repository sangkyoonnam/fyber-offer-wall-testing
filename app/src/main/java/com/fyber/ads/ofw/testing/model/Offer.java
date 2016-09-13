package com.fyber.ads.ofw.testing.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Offer implements Parcelable {

    @SerializedName("title")
    public String title;

    @SerializedName("teaser")
    public String teaser;

    @SerializedName("thumbnail")
    public Thumbnail thumbnail;

    @SerializedName("payout")
    public int payout;

    public Offer() {}

    public Offer(Parcel in) {
        readFromParcel(in);
    }

    public Offer(String _title, String _teaser, Thumbnail _thumbnail, int _payout) {
        this.title = _title;
        this.teaser = _teaser;
        this.thumbnail = _thumbnail;
        this.payout = _payout;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(teaser);
        dest.writeParcelable(thumbnail, 0);
        dest.writeInt(payout);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private void readFromParcel(Parcel in){
        title = in.readString();
        teaser = in.readString();
        thumbnail = in.readParcelable(Thumbnail.class.getClassLoader());
        payout = in.readInt();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Offer createFromParcel(Parcel in) {
            return new Offer(in);
        }

        public Offer[] newArray(int size) {
            return new Offer[size];
        }
    };
}
