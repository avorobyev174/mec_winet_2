package com.avorobyev174.mec_winet.classes.vestibule;

import com.avorobyev174.mec_winet.classes.floor.Floor;
import com.avorobyev174.mec_winet.classes.house.House;
import com.avorobyev174.mec_winet.classes.section.Section;

import java.io.Serializable;

public class Vestibule implements Serializable {
    private int number;
    private Floor floor;
    private int id;

    public Floor getFloor() {
        return floor;
    }

    public int getNumber() {
        return number;
    }

    public String getFullNumber() {
        return "Тамбур " + getNumber();
    }

    public String getShortNumber() {
        return "Т" + getNumber();
    }

    public int getId() {
        return id;
    }

    public Vestibule(int id, int number, Floor floor) {
        this.number = number;
        this.floor = floor;
        this.id = id;
    }

}
