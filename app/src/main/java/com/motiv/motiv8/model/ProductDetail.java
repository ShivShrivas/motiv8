package com.motiv.motiv8.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductDetail {
    @SerializedName("productID")
    @Expose
    private String productID;
    @SerializedName("productName")
    @Expose
    private String productName;
    @SerializedName("companyName")
    @Expose
    private String companyName;
    @SerializedName("brandName")
    @Expose
    private String brandName;
    @SerializedName("strCategoryName")
    @Expose
    private String strCategoryName;
    @SerializedName("skuCode")
    @Expose
    private String skuCode;
    @SerializedName("taxRate")
    @Expose
    private String taxRate;
    @SerializedName("mrp")
    @Expose
    private String mrp;
    @SerializedName("motivePrice")
    @Expose
    private String motivePrice;
    @SerializedName("motivPoint")
    @Expose
    private String motivPoint;
    @SerializedName("deliveryCharge")
    @Expose
    private String deliveryCharge;
    @SerializedName("descr")
    @Expose
    private String descr;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("createdOn")
    @Expose
    private String createdOn;

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getStrCategoryName() {
        return strCategoryName;
    }

    public void setStrCategoryName(String strCategoryName) {
        this.strCategoryName = strCategoryName;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(String taxRate) {
        this.taxRate = taxRate;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getMotivePrice() {
        return motivePrice;
    }

    public void setMotivePrice(String motivePrice) {
        this.motivePrice = motivePrice;
    }

    public String getMotivPoint() {
        return motivPoint;
    }

    public void setMotivPoint(String motivPoint) {
        this.motivPoint = motivPoint;
    }

    public String getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(String deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

}
