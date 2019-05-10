package com.paymentez.android.rest;

import com.paymentez.android.rest.model.CreateAuthenticateResponse;
import com.paymentez.android.rest.model.CreateTokenResponse;
import com.paymentez.android.rest.model.PaymentezError;


public interface ChallengeCallback {

    void onAccept();

    void onAvoid();
}