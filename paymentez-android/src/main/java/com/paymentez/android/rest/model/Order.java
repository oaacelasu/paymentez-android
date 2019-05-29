
package com.paymentez.android.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Order {

    @SerializedName("amount")
    @Expose
    private double amount;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("dev_reference")
    @Expose
    private String dev_reference;

    @SerializedName("vat")
    @Expose
    private int vat;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDev_reference() {
        return dev_reference;
    }

    public void setDev_reference(String dev_reference) {
        this.dev_reference = dev_reference;
    }

    public int getVat() {
        return vat;
    }

    public void setVat(int vat) {
        this.vat = vat;
    }
}
