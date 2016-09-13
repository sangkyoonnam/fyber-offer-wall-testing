package com.fyber.ads.ofw.testing.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OfferData {

    @SerializedName("code")
    public String code;

    @SerializedName("message")
    public String message;

    @SerializedName("offers")
    private ArrayList<Offer> offers = new ArrayList();

    public OfferData(ArrayList<Offer> offers) {
        super();
        this.offers = offers;
    }

    public ArrayList<Offer> getOffers() {
        return offers;
    }
}
