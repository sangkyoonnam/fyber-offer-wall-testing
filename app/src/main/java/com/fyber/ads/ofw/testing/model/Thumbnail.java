package com.fyber.ads.ofw.testing.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Thumbnail implements Parcelable {

//    @SerializedName("lowres")
//    public String lowres;

    @SerializedName("hires")
    public String hires;

    public Thumbnail() {}

    public Thumbnail(Parcel in) {
        readFromParcel(in);
    }

    public Thumbnail(String _hires) {
        this.hires = _hires;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(hires);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private void readFromParcel(Parcel in){
        hires = in.readString();
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
