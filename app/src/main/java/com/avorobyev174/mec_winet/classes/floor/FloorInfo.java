package com.avorobyev174.mec_winet.classes.floor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FloorInfo {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("section_id")
    @Expose
    private int sectionId;

    @SerializedName("floor_number")
    @Expose
    private String floorNumber;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(String floorNumber) {
        this.floorNumber = floorNumber;
    }

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }
}