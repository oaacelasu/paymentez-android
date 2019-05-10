
package com.paymentez.android.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.paymentez.android.model.Card;

public class CreateAuthenticateResponse {

    @SerializedName("authentication")
    @Expose
    private Authentication authentication;

    @SerializedName("transaction")
    @Expose
    private Transaction transaction;

    @SerializedName("challenge_request")
    @Expose
    private String challenge_request;

    @SerializedName("hidden_iframe")
    @Expose
    private String hidden_iframe;

    public Authentication getAuthentication() {
        return authentication;
    }

    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public String getChallenge_request() {
        return challenge_request;
    }

    public void setChallenge_request(String challenge_request) {
        this.challenge_request = challenge_request;
    }

    public String getHidden_iframe() {
        return hidden_iframe;
    }

    public void setHidden_iframe(String hidden_iframe) {
        this.hidden_iframe = hidden_iframe;
    }
}
