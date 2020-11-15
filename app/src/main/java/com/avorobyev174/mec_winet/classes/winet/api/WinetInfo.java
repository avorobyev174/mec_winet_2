package com.avorobyev174.mec_winet.classes.winet.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WinetInfo {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("vestibule_id")
    @Expose
    private int vestibuleId;

    @SerializedName("ser_number")
    @Expose
    private String serNumber;

    @SerializedName("winet_type")
    @Expose
    private String type;

    @SerializedName("winet_comment")
    @Expose
    private String comment;

    @SerializedName("winet_x")
    @Expose
    private String winetX;

    @SerializedName("winet_y")
    @Expose
    private String winetY;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSerNumber() {
        return serNumber;
    }

    public void setSerNumber(String serNumber) {
        this.serNumber = serNumber;
    }

    public int getVestibuleId() {
        return vestibuleId;
    }

    public void setVestibuleId(int vestibuleId) {
        this.vestibuleId = vestibuleId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getWinetX() {
        return winetX;
    }

    public void setWinetX(String winetX) {
        this.winetX = winetX;
    }

    public String getWinetY() {
        return winetY;
    }

    public void setWinetY(String winetY) {
        this.winetY = winetY;
    }
}