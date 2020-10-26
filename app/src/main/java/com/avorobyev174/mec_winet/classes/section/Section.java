package com.avorobyev174.mec_winet.classes.section;

import com.avorobyev174.mec_winet.classes.house.House;

import java.io.Serializable;

public class Section implements Serializable {
    private int id;
    private int number;
    private House house;

    public int getNumber() {
        return number;
    }

    public String getFullNumber() {
        return "Подьезд " + getNumber();
    }

    public String getShortNumber() {
        return "П" + getNumber();
    }

    public House getHouse() {
        return house;
    }

    public int getId() {
        return id;
    }

    public Section(int id, int number, House house) {
        this.id = id;
        this.number = number;
        this.house = house;
    }

}
