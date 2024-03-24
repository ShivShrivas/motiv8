package com.motiv.motiv8.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserDetail {

    @SerializedName("intPKUserID")
@Expose
private Integer intPKUserID;
    @SerializedName("strLoginID")
    @Expose
    private String strLoginID;
    @SerializedName("strAssocaieCode")
    @Expose
    private String strAssocaieCode;
    @SerializedName("strPWD")
    @Expose
    private String strPWD;
    @SerializedName("strSolutation")
    @Expose
    private String strSolutation;
    @SerializedName("strFullName")
    @Expose
    private String strFullName;
    @SerializedName("strMobile")
    @Expose
    private String strMobile;
    @SerializedName("strEmail")
    @Expose
    private String strEmail;
    @SerializedName("pincode")
    @Expose
    private String pincode;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("dtCreatedon")
    @Expose
    private String dtCreatedon;
    @SerializedName("ynStatus")
    @Expose
    private String ynStatus;
    @SerializedName("referByPkAutoId")
    @Expose
    private Integer referByPkAutoId;
    @SerializedName("referByLoginID")
    @Expose
    private String referByLoginID;
    @SerializedName("referByAssoCode")
    @Expose
    private String referByAssoCode;
    @SerializedName("referByFullName")
    @Expose
    private String referByFullName;

    public Integer getIntPKUserID() {
        return intPKUserID;
    }

    public void setIntPKUserID(Integer intPKUserID) {
        this.intPKUserID = intPKUserID;
    }

    public String getStrLoginID() {
        return strLoginID;
    }

    public void setStrLoginID(String strLoginID) {
        this.strLoginID = strLoginID;
    }

    public String getStrAssocaieCode() {
        return strAssocaieCode;
    }

    public void setStrAssocaieCode(String strAssocaieCode) {
        this.strAssocaieCode = strAssocaieCode;
    }

    public String getStrPWD() {
        return strPWD;
    }

    public void setStrPWD(String strPWD) {
        this.strPWD = strPWD;
    }

    public String getStrSolutation() {
        return strSolutation;
    }

    public void setStrSolutation(String strSolutation) {
        this.strSolutation = strSolutation;
    }

    public String getStrFullName() {
        return strFullName;
    }

    public void setStrFullName(String strFullName) {
        this.strFullName = strFullName;
    }

    public String getStrMobile() {
        return strMobile;
    }

    public void setStrMobile(String strMobile) {
        this.strMobile = strMobile;
    }

    public String getStrEmail() {
        return strEmail;
    }

    public void setStrEmail(String strEmail) {
        this.strEmail = strEmail;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDtCreatedon() {
        return dtCreatedon;
    }

    public void setDtCreatedon(String dtCreatedon) {
        this.dtCreatedon = dtCreatedon;
    }

    public String getYnStatus() {
        return ynStatus;
    }

    public void setYnStatus(String ynStatus) {
        this.ynStatus = ynStatus;
    }

    public Integer getReferByPkAutoId() {
        return referByPkAutoId;
    }

    public void setReferByPkAutoId(Integer referByPkAutoId) {
        this.referByPkAutoId = referByPkAutoId;
    }

    public String getReferByLoginID() {
        return referByLoginID;
    }

    public void setReferByLoginID(String referByLoginID) {
        this.referByLoginID = referByLoginID;
    }

    public String getReferByAssoCode() {
        return referByAssoCode;
    }

    public void setReferByAssoCode(String referByAssoCode) {
        this.referByAssoCode = referByAssoCode;
    }

    public String getReferByFullName() {
        return referByFullName;
    }

    public void setReferByFullName(String referByFullName) {
        this.referByFullName = referByFullName;
    }

}