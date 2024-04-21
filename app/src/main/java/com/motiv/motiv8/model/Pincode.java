package com.motiv.motiv8.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Pincode {

    @SerializedName("pincode")
    @Expose
    private String pincode;
    @SerializedName("countryName")
    @Expose
    private String countryName;
    @SerializedName("stateName")
    @Expose
    private String stateName;
    @SerializedName("districtName")
    @Expose
    private String districtName;
    @SerializedName("areaName")
    @Expose
    private String areaName;

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
}
