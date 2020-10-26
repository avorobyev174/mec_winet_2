package com.avorobyev174.mec_winet.classes.winet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WinetInfoResponse {

    @SerializedName("success")
    @Expose
    private Boolean success;

    @SerializedName("result")
    @Expose
    private List<WinetInfo> winetInfo = null;

    @SerializedName("sql")
    @Expose
    private String sql;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<WinetInfo> getResult() {
        return winetInfo;
    }

    public void setWinetInfo(List<WinetInfo> winetInfo) {
        this.winetInfo = winetInfo;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

}
