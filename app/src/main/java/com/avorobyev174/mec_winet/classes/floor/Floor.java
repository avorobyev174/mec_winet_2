package com.avorobyev174.mec_winet.classes.floor;

import com.avorobyev174.mec_winet.classes.house.House;
import com.avorobyev174.mec_winet.classes.section.Section;

import java.io.Serializable;

public class Floor implements Serializable {
    private int id;
    private int number;

    public Section getSection() {
        return section;
    }

    private Section section;

    public int getNumber() {
        return number;
    }

    public String getFullNumber() {
        return "Этаж " + getNumber();
    }

    public String getShortNumber() {
        return "Э" + getNumber();
    }

    public int getId() {
        return id;
    }

    public Floor(int id, int number, Section section) {
        this.id = id;
        this.number = number;
        this.section = section;
    }

}
