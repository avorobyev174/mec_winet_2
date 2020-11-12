package com.avorobyev174.mec_winet.classes.apartment;

import com.avorobyev174.mec_winet.classes.winet.Winet;
import com.avorobyev174.mec_winet.classes.winetData.WinetData;

import java.io.Serializable;

public class Apartment implements Serializable {
    private int id;
    private Winet winet;
    private String apartmentDesc;
    private String apartmentType;
    private String apartmentComment;

    public void setApartmentDesc(String apartmentDesc) {
        this.apartmentDesc = apartmentDesc;
    }

    public Winet getWinet() {
        return winet;
    }

    public WinetData getWinetData() {
        return winet.getWinetData();
    }

    public String getDescription() {
        return apartmentDesc;
    }

    public int getId() {
        return id;
    }

    public  String getFullApartmentDesc() {
        if (apartmentType.equals("1")) {
            return getApartmentDesc() + " кв.";
        } else {
            return getApartmentDesc();
        }

    }

    public String getApartmentDesc() {
        return apartmentDesc;
    }

    public String getApartmentType() {
        return apartmentType;
    }

    public void setApartmentType(String apartmentType) {
        this.apartmentType = apartmentType;
    }

    public String getApartmentComment() {
        return apartmentComment;
    }

    public void setApartmentComment(String apartmentComment) {
        this.apartmentComment = apartmentComment;
    }

    public Apartment(int id, String apartmentType, String apartmentDesc, String apartmentComment, Winet winet) {
        this.id = id;
        this.apartmentType = apartmentType;
        this.apartmentComment = apartmentComment;
        this.apartmentDesc = apartmentDesc;
        this.winet = winet;
    }

}
