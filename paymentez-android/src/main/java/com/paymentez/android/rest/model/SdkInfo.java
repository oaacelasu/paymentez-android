
package com.paymentez.android.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SdkInfo {

    @SerializedName("trans_id")
    @Expose
    private String trans_id;

    @SerializedName("reference_number")
    @Expose
    private String reference_number;

    @SerializedName("app_id")
    @Expose
    private String app_id;

    @SerializedName("enc_data")
    @Expose
    private String enc_data;

    @SerializedName("max_timeout")
    @Expose
    private int max_timeout;

    @SerializedName("options_IF")
    @Expose
    private int options_IF;

    @SerializedName("options_UI")
    @Expose
    private String options_UI;

    @SerializedName("ephem_pub_key")
    @Expose
    private String ephem_pub_key;

    public String getTrans_id() {
        return trans_id;
    }

    public void setTrans_id(String trans_id) {
        this.trans_id = trans_id;
    }

    public String getReference_number() {
        return reference_number;
    }

    public void setReference_number(String reference_number) {
        this.reference_number = reference_number;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getEnc_data() {
        return enc_data;
    }

    public void setEnc_data(String enc_data) {
        this.enc_data = enc_data;
    }

    public int getMax_timeout() {
        return max_timeout;
    }

    public void setMax_timeout(int max_timeout) {
        this.max_timeout = max_timeout;
    }

    public int getOptions_IF() {
        return options_IF;
    }

    public void setOptions_IF(int options_IF) {
        this.options_IF = options_IF;
    }

    public String getOptions_UI() {
        return options_UI;
    }

    public void setOptions_UI(String options_UI) {
        this.options_UI = options_UI;
    }

    public String getEphem_pub_key() {
        return ephem_pub_key;
    }

    public void setEphem_pub_key(String ephem_pub_key) {
        this.ephem_pub_key = ephem_pub_key;
    }
}
