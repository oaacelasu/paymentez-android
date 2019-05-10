
package com.paymentez.android.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.paymentez.android.model.Card;

public class CreateAuthenticateRequest {


    @SerializedName("user")
    @Expose
    private User user;

    @SerializedName("order")
    @Expose
    private Order order;

    @SerializedName("card")
    @Expose
    private Card card;

    @SerializedName("sdk_info")
    @Expose
    private SdkInfo sdk_info;

    @SerializedName("term_url")
    @Expose
    private String term_url;

    @SerializedName("device_type")
    @Expose
    private String device_type;

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

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public SdkInfo getSdf_info() {
        return sdk_info;
    }

    public void setSdf_info(SdkInfo sdk_info) {
        this.sdk_info = sdk_info;
    }

    public String getTerm_url() {
        return term_url;
    }

    public void setTerm_url(String term_url) {
        this.term_url = term_url;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }
}
