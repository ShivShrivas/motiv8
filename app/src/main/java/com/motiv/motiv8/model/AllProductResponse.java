package com.motiv.motiv8.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllProductResponse {

    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("statusMessage")
    @Expose
    private String statusMessage;
    @SerializedName("product")
    @Expose
    private Object product;
    @SerializedName("products")
    @Expose
    private List<ProductDetail> products;

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

    public Object getProduct() {
        return product;
    }

    public void setProduct(Object product) {
        this.product = product;
    }

    public List<ProductDetail> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDetail> products) {
        this.products = products;
    }
}
