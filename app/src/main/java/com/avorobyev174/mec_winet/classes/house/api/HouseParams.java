package com.avorobyev174.mec_winet.classes.house.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HouseParams {
    @SerializedName("street")
    @Expose
    private String street;
    @SerializedName("house_number")
    @Expose
    private String houseNumber;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("house_x")
    @Expose
    private String houseX;

    @SerializedName("house_Y")
    @Expose
    private String houseY;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHouseX() {
        return houseX;
    }

    public void setHouseX(String houseX) {
        this.houseX = houseX;
    }

    public String getHouseY() {
        return houseY;
    }

    public void setHouseY(String houseY) {
        this.houseY = houseY;
    }
}