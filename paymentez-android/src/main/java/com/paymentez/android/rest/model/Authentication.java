
package com.paymentez.android.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Authentication {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("xid")
    @Expose
    private String xid;
    @SerializedName("return_code")
    @Expose
    private String return_code;

    @SerializedName("eci")
    @Expose
    private String eci;
    @SerializedName("return_message")
    @Expose
    private String return_message;
    @SerializedName("version")
    @Expose
    private String version;

    @SerializedName("cavv")
    @Expose
    private String cavv;
    @SerializedName("reference_id")
    @Expose
    private String reference_id;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getXid() {
        return xid;
    }

    public void setXid(String xid) {
        this.xid = xid;
    }

    public String getReturn_code() {
        return return_code;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }

    public String getEci() {
        return eci;
    }

    public void setEci(String eci) {
        this.eci = eci;
    }

    public String getReturn_message() {
        return return_message;
    }

    public void setReturn_message(String return_message) {
        this.return_message = return_message;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCavv() {
        return cavv;
    }

    public void setCavv(String cavv) {
        this.cavv = cavv;
    }

    public String getReference_id() {
        return reference_id;
    }

    public void setReference_id(String reference_id) {
        this.reference_id = reference_id;
    }
}
