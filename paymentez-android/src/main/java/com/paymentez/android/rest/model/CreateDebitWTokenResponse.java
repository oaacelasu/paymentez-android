
package com.paymentez.android.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.paymentez.android.model.Card;

public class CreateDebitWTokenResponse {
    @SerializedName("transaction")
    @Expose
    private Transaction transaction;

    @SerializedName("card")
    @Expose
    private Card card;

    @SerializedName("3ds")
    @Expose
    private ThreeDS tree_ds;

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public ThreeDS getTree_ds() {
        return tree_ds;
    }

    public void setTree_ds(ThreeDS tree_ds) {
        this.tree_ds = tree_ds;
    }
}
