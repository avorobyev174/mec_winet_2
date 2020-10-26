package com.avorobyev174.mec_winet.classes.floor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FloorInfoResponse {

    @SerializedName("success")
    @Expose
    private Boolean success;

    @SerializedName("result")
    @Expose
    private List<FloorInfo> floorInfo = null;

    @SerializedName("sql")
    @Expose
    private String sql;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<FloorInfo> getResult() {
        return floorInfo;
    }

    public void setFloorInfo(List<FloorInfo> floorInfo) {
        this.floorInfo = floorInfo;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

}
