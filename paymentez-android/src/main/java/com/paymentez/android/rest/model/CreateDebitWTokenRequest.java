
package com.paymentez.android.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.paymentez.android.model.Card;

public class CreateDebitWTokenRequest {


    @SerializedName("user")
    @Expose
    private User user;

    @SerializedName("order")
    @Expose
    private Order order;

    @SerializedName("card")
    @Expose
    private CardToken card;

    @SerializedName("extra_params")
    @Expose
    private ExtraParams extra_params;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public CardToken getCard() {
        return card;
    }

    public void setCard(String token) {
        this.card = new CardToken(token);
    }

    public ExtraParams getExtra_params() {
        return extra_params;
    }

    public void setExtra_params( String term_url, String device_type, boolean process_anyway, SdkInfo sdk_info) {
        this.extra_params = new ExtraParams(term_url, device_type, process_anyway, sdk_info);
    }
}

class ExtraParams {


    @SerializedName("threeDS2_data")
    @Expose
    private ThreeDS2Data threeDS2_data;

    @SerializedName("sdk_info")
    @Expose
    private SdkInfo sdk_info;

    public ExtraParams(String term_url, String device_type, boolean process_anyway, SdkInfo sdk_info) {
        this.threeDS2_data = new ThreeDS2Data(term_url, device_type, process_anyway);
        this.sdk_info = sdk_info;
    }

    public ThreeDS2Data getThreeDS2_data() {
        return threeDS2_data;
    }

    public SdkInfo getSdk_info() {
        return sdk_info;
    }
}
class CardToken {

    public CardToken(String token) {
        this.token = token;
    }

    @SerializedName("token")
    @Expose
    private String token;

    public String getToken() {
        return token;
    }

}
class ThreeDS2Data {

    public ThreeDS2Data(String term_url, String device_type, boolean process_anyway) {
        this.term_url = term_url;
        this.device_type = device_type;
        this.process_anyway = process_anyway;
    }

    @SerializedName("term_url")
    @Expose
    private String term_url;

    @SerializedName("device_type")
    @Expose
    private String device_type;

    @SerializedName("process_anyway")
    @Expose
    private boolean process_anyway;

    public String getTerm_url() {
        return term_url;
    }

    public String getDevice_type() {
        return device_type;
    }

    public boolean isProcess_anyway() {
        return process_anyway;
    }
}