package com.avorobyev174.mec_winet.classes.house;

import com.avorobyev174.mec_winet.classes.common.Entity;

import java.io.Serializable;

public class House extends Entity implements Serializable {
    private String street;
    private String streetNumber;
    private String name;
    private String houseY;
    private String houseX;
    private int id;

    public String getStreet() {
        return street;
    }

    public String getFullStreetName() {
        return street + " " + streetNumber;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public int getId() {
        return id;
    }

    public House(int id, String street, String streetNumber) {
        super();
        this.street = street;
        this.streetNumber = streetNumber;
        this.id = id;
    }

    public House(int id, String name, String houseX, String houseY) {
        super();
        this.name = name;
        this.houseX = houseX;
        this.houseY = houseY;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getHouseY() {
        return houseY;
    }

    public String getHouseX() {
        return houseX;
    }

    public boolean isCommonObj() {
        return street == null;
    }
}
