package com.avorobyev174.mec_winet.classes.meter.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MetertInfo {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("apartment_id")
    @Expose
    private int apartmentId;

    @SerializedName("meter_serial_number")
    @Expose
    private String serNumber;

    @SerializedName("meter_type")
    @Expose
    private int meterType;

    @SerializedName("meter_password")
    @Expose
    private String password;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(int apartmentId) {
        this.apartmentId = apartmentId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSerNumber() {
        return serNumber;
    }

    public void setSerNumber(String serNumber) {
        this.serNumber = serNumber;
    }

    public int getMeterType() {
        return meterType;
    }

    public void setMeterType(int meterType) {
        this.meterType = meterType;
    }
}