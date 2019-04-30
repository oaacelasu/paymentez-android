package com.paymentez.android.rest;

import com.paymentez.android.rest.model.PaymentezError;

public interface InitCallback {

    /**
     * PaymentezError callback method.
     * @param error the error that occurred.
     */
    void onError(PaymentezError error);


    void onSuccess();
}