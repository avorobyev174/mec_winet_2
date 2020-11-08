package com.avorobyev174.mec_winet.classes.meter;

import com.avorobyev174.mec_winet.classes.apartment.Apartment;

import java.io.Serializable;

public class Meter implements Serializable {
    private int id;
    private Apartment apartment;
    private String serNumber;
    private String type;
    private String password;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSerNumber(String serNumber) {
        this.serNumber = serNumber;
    }

    public Apartment getApartment() {
        return apartment;
    }

    public String getNumber() {
        return serNumber;
    }

    public int getId() {
        return id;
    }

    public String getSerNumber() {
        return serNumber;
    }

    public Meter(int id, String type, String serNumber, String password, Apartment apartment) {
        this.id = id;
        this.type = type;
        this.password = password;
        this.serNumber = serNumber;
        this.apartment = apartment;
    }

}
