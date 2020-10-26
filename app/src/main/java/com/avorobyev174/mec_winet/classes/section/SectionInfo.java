package com.avorobyev174.mec_winet.classes.section;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SectionInfo {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("house_id")
    @Expose
    private int houseId;

    @SerializedName("section_number")
    @Expose
    private String sectionNumber;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSectionNumber() {
        return sectionNumber;
    }

    public void setSectionNumber(String sectionNumber) {
        this.sectionNumber = sectionNumber;
    }

    public int getHouseId() {
        return houseId;
    }

    public void setHouseId(int houseId) {
        this.houseId = houseId;
    }
}