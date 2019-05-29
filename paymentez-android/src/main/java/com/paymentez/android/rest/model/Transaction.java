
package com.paymentez.android.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Transaction {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("payment_date")
    @Expose
    private String payment_date;

    @SerializedName("amount")
    @Expose
    private double amount;

    @SerializedName("authorization_code")
    @Expose
    private String authorization_code;

    @SerializedName("installments")
    @Expose
    private int installments;

    @SerializedName("dev_reference")
    @Expose
    private String dev_reference;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("carrier_code")
    @Expose
    private String carrier_code;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("status_detail")
    @Expose
    private String status_detail;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPayment_date() {
        return payment_date;
    }

    public void setPayment_date(String payment_date) {
        this.payment_date = payment_date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getAuthorization_code() {
        return authorization_code;
    }

    public void setAuthorization_code(String authorization_code) {
        this.authorization_code = authorization_code;
    }

    public int getInstallments() {
        return installments;
    }

    public void setInstallments(int installments) {
        this.installments = installments;
    }

    public String getDev_reference() {
        return dev_reference;
    }

    public void setDev_reference(String dev_reference) {
        this.dev_reference = dev_reference;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCarrier_code() {
        return carrier_code;
    }

    public void setCarrier_code(String carrier_code) {
        this.carrier_code = carrier_code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus_detail() {
        return status_detail;
    }

    public void setStatus_detail(String status_detail) {
        this.status_detail = status_detail;
    }
}
