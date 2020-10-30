package com.avorobyev174.mec_winet.classes.meter;

import com.avorobyev174.mec_winet.classes.winet.Winet;

public class Meter {
    private int id;
    private int winetId;
    private Winet winet;
    private String apartmentNumber;

    public void setWinetId(int winetId) {
        this.winetId = winetId;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    public int getWinetId() {
        return winetId;
    }

    public Winet getWinet() {
        return winet;
    }

    public String getNumber() {
        return apartmentNumber;
    }

    public int getId() {
        return id;
    }

    public  String getFullNumber() {
        return getNumber() + " кв.";
    }

    public Meter(int id, String apartmentNumber, int winetId, Winet winet) {
        this.id = id;
        this.winetId = winetId;
        this.apartmentNumber = apartmentNumber;
        this.winet = winet;
    }

}
