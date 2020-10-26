package com.avorobyev174.mec_winet.classes.house;

import java.io.Serializable;

public class House implements Serializable {
    private String street;
    private String streetNumber;
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


}
