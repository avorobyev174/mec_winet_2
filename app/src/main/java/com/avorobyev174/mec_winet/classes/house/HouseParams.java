package com.avorobyev174.mec_winet.classes.house;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HouseParams {
    @SerializedName("street")
    @Expose
    private String street;
    @SerializedName("house_number")
    @Expose
    private String houseNumber;

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }
}