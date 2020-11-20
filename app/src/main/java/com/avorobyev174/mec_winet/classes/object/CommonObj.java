package com.avorobyev174.mec_winet.classes.object;

import com.avorobyev174.mec_winet.classes.common.Entity;
import com.avorobyev174.mec_winet.classes.house.House;

import java.io.Serializable;

public class CommonObj extends Entity implements Serializable {

    private String name;
    private int parentId;
    private int id;
    private House house;

    public CommonObj( int id, int parentId, String name) {
        this.name = name;
        this.parentId = parentId;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    @Override
    public Entity getParent() {
        return null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }
}
