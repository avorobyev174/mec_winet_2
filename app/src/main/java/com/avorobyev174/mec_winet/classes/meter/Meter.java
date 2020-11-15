package com.avorobyev174.mec_winet.classes.meter;

import com.avorobyev174.mec_winet.classes.apartment.Apartment;
import com.avorobyev174.mec_winet.classes.common.Entity;

import java.io.Serializable;

public class Meter extends Entity implements Serializable {
    private int id;
    private Apartment apartment;
    private String serNumber;
    private int type;
    private String password;

    public int getType() {
        return type;
    }

    public void setType(int type) {
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

    public Meter(int id, int type, String serNumber, String password, Apartment apartment) {
        this.id = id;
        this.type = type;
        this.password = password;
        this.serNumber = serNumber;
        this.apartment = apartment;
    }

    @Override
    public Entity getParent() {
        return apartment;
    }
}
