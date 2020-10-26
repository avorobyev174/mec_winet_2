package com.avorobyev174.mec_winet.classes.floor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FloorParams {
    @SerializedName("floor_number")
    @Expose
    private String floorNumber;

    @SerializedName("section_id")
    @Expose
    private String sectionId;

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(String floorNumber) {
        this.floorNumber = floorNumber;
    }
}