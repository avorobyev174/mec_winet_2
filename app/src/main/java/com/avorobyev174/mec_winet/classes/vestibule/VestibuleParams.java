package com.avorobyev174.mec_winet.classes.vestibule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VestibuleParams {
    @SerializedName("vestibule_number")
    @Expose
    private String vestibuleNumber;

    @SerializedName("floor_id")
    @Expose
    private String floorId;

    public String getFloorId() {
        return floorId;
    }

    public void setFloorId(String floorId) {
        this.floorId = floorId;
    }

    public String getVestibuleNumber() {
        return vestibuleNumber;
    }

    public void setVestibuleNumber(String vestibuleNumber) {
        this.vestibuleNumber = vestibuleNumber;
    }
}