package com.avorobyev174.mec_winet.classes.vestibule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VestibuleInfo {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("floor_id")
    @Expose
    private int floorId;

    @SerializedName("vestibule_number")
    @Expose
    private String vestibuleNumber;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVestibuleNumber() {
        return vestibuleNumber;
    }

    public void setVestibuleNumber(String vestibuleNumber) {
        this.vestibuleNumber = vestibuleNumber;
    }

    public int getFloorId() {
        return floorId;
    }

    public void setFloorId(int floorId) {
        this.floorId = floorId;
    }
}