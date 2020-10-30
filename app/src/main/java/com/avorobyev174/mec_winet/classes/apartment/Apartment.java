package com.avorobyev174.mec_winet.classes.apartment;

import com.avorobyev174.mec_winet.classes.winet.Winet;

public class Apartment {
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

    public String getNumber() {
        return apartmentDesc;
    }

    public int getId() {
        return id;
    }

    public  String getFullNumber() {
        return getNumber() + " кв.";
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
