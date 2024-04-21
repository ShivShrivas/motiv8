package com.motiv.motiv8.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LedgerDetails {

    @SerializedName("fkAssoID")
    @Expose
    private Integer fkAssoID;
    @SerializedName("strAssoCode")
    @Expose
    private Object strAssoCode;
    @SerializedName("stepCr")
    @Expose
    private Integer stepCr;
    @SerializedName("stepDr")
    @Expose
    private Integer stepDr;
    @SerializedName("stepBalance")
    @Expose
    private Integer stepBalance;
    @SerializedName("forDateDDMMYYYY")
    @Expose
    private String forDateDDMMYYYY;
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

    public Integer getStepCr() {
        return stepCr;
    }

    public void setStepCr(Integer stepCr) {
        this.stepCr = stepCr;
    }

    public Integer getStepDr() {
        return stepDr;
    }

    public void setStepDr(Integer stepDr) {
        this.stepDr = stepDr;
    }

    public Integer getStepBalance() {
        return stepBalance;
    }

    public void setStepBalance(Integer stepBalance) {
        this.stepBalance = stepBalance;
    }

    public String getForDateDDMMYYYY() {
        return forDateDDMMYYYY;
    }

    public void setForDateDDMMYYYY(String forDateDDMMYYYY) {
        this.forDateDDMMYYYY = forDateDDMMYYYY;
    }

    public String getStrDescr() {
        return strDescr;
    }

    public void setStrDescr(String strDescr) {
        this.strDescr = strDescr;
    }
}
