package com.motiv.motiv8.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PincodeResponse {
    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("statusMessage")
    @Expose
    private String statusMessage;
    @SerializedName("pincodeList")
    @Expose
    private List<Pincode> pincodeList;

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

    public List<Pincode> getPincodeList() {
        return pincodeList;
    }

    public void setPincodeList(List<Pincode> pincodeList) {
        this.pincodeList = pincodeList;
    }

}


