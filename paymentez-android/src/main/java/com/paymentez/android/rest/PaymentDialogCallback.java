package com.paymentez.android.rest;

import com.modirum.threedsv2.core.ChallengeStatusReceiver;
import com.paymentez.android.rest.model.CreateAuthenticateResponse;
import com.paymentez.android.rest.model.CreateTokenResponse;
import com.paymentez.android.rest.model.PaymentezError;


public interface PaymentDialogCallback {

    void onAccept();

    void onAvoid();
}