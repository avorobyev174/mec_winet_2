package com.avorobyev174.mec_winet.classes.winet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WinetParams {
    @SerializedName("ser_number")
    @Expose
    private String serNumber;

    @SerializedName("vestibule_id")
    @Expose
    private String vestibuleId;

    @SerializedName("winet_type")
    @Expose
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVestibuleId() {
        return vestibuleId;
    }

    public void setVestibuleId(String vestibuleId) {
        this.vestibuleId = vestibuleId;
    }

    public String getSerNumber() {
        return serNumber;
    }

    public void setSerNumber(String serNumber) {
        this.serNumber = serNumber;
    }
}