package com.motiv.motiv8.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PointLedgerDetails {
    @SerializedName("fkAssoID")
    @Expose
    private Integer fkAssoID;
    @SerializedName("strAssoCode")
    @Expose
    private Object strAssoCode;
    @SerializedName("pointCr")
    @Expose
    private String pointCr;
    @SerializedName("pointDr")
    @Expose
    private String pointDr;
    @SerializedName("pointBalance")
    @Expose
    private String pointBalance;
    @SerializedName("tranDateDDMMYYYY")
    @Expose
    private String tranDateDDMMYYYY;
    @SerializedName("strDescr")
    @Expose
    private String strDescr;

    public Integer getFkAssoID() {
        return fkAssoID;
    }

    public void setFkAssoID(Integer fkAssoID) {
        this.fkAssoID = fkAssoID;
    }

    public Object getStrAssoCode() {
        return strAssoCode;
    }

    public void setStrAssoCode(Object strAssoCode) {
        this.strAssoCode = strAssoCode;
    }

    public String getPointCr() {
        return pointCr;
    }

    public void setPointCr(String pointCr) {
        this.pointCr = pointCr;
    }

    public String getPointDr() {
        return pointDr;
    }

    public void setPointDr(String pointDr) {
        this.pointDr = pointDr;
    }

    public String getPointBalance() {
        return pointBalance;
    }

    public void setPointBalance(String pointBalance) {
        this.pointBalance = pointBalance;
    }

    public String getTranDateDDMMYYYY() {
        return tranDateDDMMYYYY;
    }

    public void setTranDateDDMMYYYY(String tranDateDDMMYYYY) {
        this.tranDateDDMMYYYY = tranDateDDMMYYYY;
    }

    public String getStrDescr() {
        return strDescr;
    }

    public void setStrDescr(String strDescr) {
        this.strDescr = strDescr;
    }

}
