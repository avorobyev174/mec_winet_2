package com.avorobyev174.mec_winet.classes.winetData.api;

import com.avorobyev174.mec_winet.classes.winet.api.WinetInfo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WinetDataInfoResponse {

    @SerializedName("success")
    @Expose
    private Boolean success;

    @SerializedName("result")
    @Expose
    private List<WinetInfo> winetDataInfo = null;

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
        return winetDataInfo;
    }

    public void setWinetDataInfo(List<WinetInfo> winetDataInfo) {
        this.winetDataInfo = winetDataInfo;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

}
