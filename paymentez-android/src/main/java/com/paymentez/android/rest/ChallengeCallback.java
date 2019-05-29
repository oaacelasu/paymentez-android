package com.paymentez.android.rest;


import com.paymentez.android.rest.model.PaymentezError;

public interface ChallengeCallback {

    void completed(String transactionId, String transactionStatus);

    void cancelled();

    void timedout();

    void protocolError(PaymentezError errorMessage);

    void runtimeError(PaymentezError errorMessage);

}