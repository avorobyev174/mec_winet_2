package com.avorobyev174.mec_winet.classes.apartment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApartmentParams {
    @SerializedName("winet_id")
    @Expose
    private String winetId;

    @SerializedName("apartment_type")
    @Expose
    private String apartmentType;

    @SerializedName("apartment_description")
    @Expose
    private String apartmentDesc;

    public String getWinetId() {
        return winetId;
    }

    public void setWinetId(String winetId) {
        this.winetId = winetId;
    }

    public String getApartmentType() {
        return apartmentType;
    }

    public void setApartmentType(String apartmentType) {
        this.apartmentType = apartmentType;
    }

    public String getApartmentDesc() {
        return apartmentDesc;
    }

    public void setApartmentDesc(String apartmentDesc) {
        this.apartmentDesc = apartmentDesc;
    }
}