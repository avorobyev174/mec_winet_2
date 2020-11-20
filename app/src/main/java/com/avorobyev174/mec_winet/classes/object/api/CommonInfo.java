package com.avorobyev174.mec_winet.classes.object.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommonInfo {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("object_name")
    @Expose
    private String name;

    @SerializedName("parent_id")
    @Expose
    private int parentId;


    public int getId() {
        return id;
    }

    public void setId(int id) {
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
}
