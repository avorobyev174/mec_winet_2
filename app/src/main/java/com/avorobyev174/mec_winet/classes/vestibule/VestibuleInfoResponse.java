package com.avorobyev174.mec_winet.classes.vestibule;

import com.avorobyev174.mec_winet.classes.floor.FloorInfo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VestibuleInfoResponse {

    @SerializedName("success")
    @Expose
    private Boolean success;

    @SerializedName("result")
    @Expose
    private List<VestibuleInfo> vestibuleInfo = null;

    @SerializedName("sql")
    @Expose
    private String sql;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<VestibuleInfo> getResult() {
        return vestibuleInfo;
    }

    public void setVestibuleInfo(List<VestibuleInfo> vestibuleInfo) {
        this.vestibuleInfo = vestibuleInfo;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

}
