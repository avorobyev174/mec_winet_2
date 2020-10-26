package com.avorobyev174.mec_winet.classes.house;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HouseInfo {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("street")
    @Expose
    private String street;
    @SerializedName("house_number")
    @Expose
    private String houseNumber;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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