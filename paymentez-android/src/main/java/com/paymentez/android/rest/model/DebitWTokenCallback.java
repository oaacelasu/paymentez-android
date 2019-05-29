package com.paymentez.android.rest.model;

/**
 * An interface representing a callback to be notified about the results of
 * {@link CreateDebitWTokenResponse} creation or requests
 */
public interface DebitWTokenCallback {

    /**
     * PaymentezError callback method.
     * @param error the error that occurred.
     */
    void onError(PaymentezError error);

    void onSuccess(CreateDebitWTokenResponse response);
}