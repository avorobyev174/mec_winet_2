package com.avorobyev174.mec_winet.classes.vestibule;

import com.avorobyev174.mec_winet.classes.floor.FloorParams;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VestibuleResponseWithParams {

    @SerializedName("success")
    @Expose
    private Boolean success;

    @SerializedName("result")
    @Expose
    private String result = null;

    @SerializedName("sql")
    @Expose
    private String sql;


    @SerializedName("params")
    @Expose
    private VestibuleParams params;

    public VestibuleParams getParams() {
        return params;
    }

    public void setParams(VestibuleParams params) {
        this.params = params;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

}
