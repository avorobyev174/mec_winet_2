package com.avorobyev174.mec_winet.classes.apartment.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApartmentInfo {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("winet_id")
    @Expose
    private int winetId;

    @SerializedName("apartment_description")
    @Expose
    private String apartmentDescription;

    @SerializedName("apartment_type")
    @Expose
    private String apartmentType;

    @SerializedName("apartment_comment")
    @Expose
    private String apartmentComment;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWinetId() {
        return winetId;
    }

    public void setWinetId(int winetId) {
        this.winetId = winetId;
    }

    public String getApartmentComment() {
        return apartmentComment;
    }

    public void setApartmentComment(String apartmentComment) {
        this.apartmentComment = apartmentComment;
    }

    public String getApartmentDescription() {
        return apartmentDescription;
    }

    public void setApartmentDescription(String apartmentDescription) {
        this.apartmentDescription = apartmentDescription;
    }

    public String getApartmentType() {
        return apartmentType;
    }

    public void setApartmentType(String apartmentType) {
        this.apartmentType = apartmentType;
    }
}