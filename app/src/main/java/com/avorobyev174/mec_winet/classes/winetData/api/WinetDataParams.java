package com.avorobyev174.mec_winet.classes.winetData.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WinetDataParams {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("vestibule_id")
    @Expose
    private String vestibuleId;

    @SerializedName("ser_number")
    @Expose
    private String serNumber;

    @SerializedName("winet_comment")
    @Expose
    private String comment;

    @SerializedName("winet_type")
    @Expose
    private String type;

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

    public String getSerNumber() {
        return serNumber;
    }

    public void setSerNumber(String serNumber) {
        this.serNumber = serNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVestibuleId() {
        return vestibuleId;
    }

    public void setVestibuleId(String vestibuleId) {
        this.vestibuleId = vestibuleId;
    }
}