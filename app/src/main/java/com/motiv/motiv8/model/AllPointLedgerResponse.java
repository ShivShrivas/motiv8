package com.motiv.motiv8.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllPointLedgerResponse {
    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("statusMessage")
    @Expose
    private String statusMessage;
    @SerializedName("ledger")
    @Expose
    private List<PointLedgerDetails> ledger;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public List<PointLedgerDetails> getLedger() {
        return ledger;
    }

    public void setLedger(List<PointLedgerDetails> ledger) {
        this.ledger = ledger;
    }

}
