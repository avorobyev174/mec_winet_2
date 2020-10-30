package com.avorobyev174.mec_winet.classes.apartment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApartmentParams {
    @SerializedName("apartment_number")
    @Expose
    private String apartmentNumber;

    @SerializedName("winet_id")
    @Expose
    private String winetId;

    public String getWinetId() {
        return winetId;
    }

    public void setWinetId(String winetId) {
        this.winetId = winetId;
    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }
}