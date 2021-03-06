package com.avorobyev174.mec_winet.classes.apartment.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApartmentInfoResponse {

    @SerializedName("success")
    @Expose
    private Boolean success;

    @SerializedName("result")
    @Expose
    private List<ApartmentInfo> apartmentInfo = null;

    @SerializedName("sql")
    @Expose
    private String sql;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<ApartmentInfo> getResult() {
        return apartmentInfo;
    }

    public void setWinetDataInfo(List<ApartmentInfo> winetDataInfo) {
        this.apartmentInfo = winetDataInfo;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

}
