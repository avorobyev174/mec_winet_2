package com.avorobyev174.mec_winet.classes.section;

import com.avorobyev174.mec_winet.classes.floor.FloorInfo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SectonInfoResponse {

    @SerializedName("success")
    @Expose
    private Boolean success;

    @SerializedName("result")
    @Expose
    private List<SectionInfo> sectionInfo = null;

    @SerializedName("sql")
    @Expose
    private String sql;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<SectionInfo> getResult() {
        return sectionInfo;
    }

    public void setSectionInfo(List<SectionInfo> sectionInfo) {
        this.sectionInfo = sectionInfo;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

}
