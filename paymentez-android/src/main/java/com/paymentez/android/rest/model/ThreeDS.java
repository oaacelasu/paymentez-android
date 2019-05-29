
package com.paymentez.android.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.paymentez.android.model.Card;

public class ThreeDS {
    @SerializedName("sdk_response")
    @Expose
    private SdkResponse sdk_response;

    @SerializedName("authentication")
    @Expose
    private Authentication authentication;

    public SdkResponse getSdk_response() {
        return sdk_response;
    }

    public void setSdk_response(SdkResponse sdk_response) {
        this.sdk_response = sdk_response;
    }

    public Authentication getAuthentication() {
        return authentication;
    }

    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }
}
