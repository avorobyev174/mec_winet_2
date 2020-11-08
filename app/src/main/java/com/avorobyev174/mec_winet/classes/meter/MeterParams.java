package com.avorobyev174.mec_winet.classes.meter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MeterParams {
    @SerializedName("apartment_id")
    @Expose
    private String apartmentId;

    @SerializedName("meter_type")
    @Expose
    private String meterType;

    @SerializedName("serial_number")
    @Expose
    private String serNumber;

    @SerializedName("meter_password")
    @Expose
    private String password;

    public String getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(String apartmentId) {
        this.apartmentId = apartmentId;
    }

    public String getMeterType() {
        return meterType;
    }

    public void setMeterType(String meterType) {
        this.meterType = meterType;
    }

    public String getSerNumber() {
        return serNumber;
    }

    public void setSerNumber(String serNumber) {
        this.serNumber = serNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}