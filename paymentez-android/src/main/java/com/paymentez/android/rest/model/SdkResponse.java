
package com.paymentez.android.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SdkResponse {

    @SerializedName("acs_trans_id")
    @Expose
    private String acs_trans_id;
    @SerializedName("acs_signed_content")
    @Expose
    private String acs_signed_content;

    @SerializedName("acs_reference_number")
    @Expose
    private String acs_reference_number;

    public String getAcs_trans_id() {
        return acs_trans_id;
    }

    public void setAcs_trans_id(String acs_trans_id) {
        this.acs_trans_id = acs_trans_id;
    }

    public String getAcs_signed_content() {
        return acs_signed_content;
    }

    public void setAcs_signed_content(String acs_signed_content) {
        this.acs_signed_content = acs_signed_content;
    }

    public String getAcs_reference_number() {
        return acs_reference_number;
    }

    public void setAcs_reference_number(String acs_reference_number) {
        this.acs_reference_number = acs_reference_number;
    }
}
