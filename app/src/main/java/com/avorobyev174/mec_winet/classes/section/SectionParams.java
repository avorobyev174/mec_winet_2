package com.avorobyev174.mec_winet.classes.section;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SectionParams {
    @SerializedName("section_number")
    @Expose
    private String sectionNumber;

    @SerializedName("house_id")
    @Expose
    private String houseId;

    public String getHouseId() {
        return houseId;
    }

    public void setHouseId(String houseId) {
        this.houseId = houseId;
    }

    public String getSectionNumber() {
        return sectionNumber;
    }

    public void setSectionNumber(String sectionNumber) {
        this.sectionNumber = sectionNumber;
    }
}