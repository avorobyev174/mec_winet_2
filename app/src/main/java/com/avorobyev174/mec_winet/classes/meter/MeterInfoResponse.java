package com.avorobyev174.mec_winet.classes.meter;

import com.avorobyev174.mec_winet.classes.apartment.ApartmentInfo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MeterInfoResponse {

    @SerializedName("success")
    @Expose
    private Boolean success;

    @SerializedName("result")
    @Expose
    private List<MetertInfo> meterInfo = null;

    @SerializedName("sql")
    @Expose
    private String sql;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<MetertInfo> getResult() {
        return meterInfo;
    }

    public void setMeterInfo(List<MetertInfo> meterInfo) {
        this.meterInfo = meterInfo;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

}
