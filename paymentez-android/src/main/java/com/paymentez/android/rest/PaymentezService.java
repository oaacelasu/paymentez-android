package com.paymentez.android.rest;

import com.google.gson.JsonObject;
import com.paymentez.android.rest.model.CardBinResponse;
import com.paymentez.android.rest.model.CreateAuthenticateRequest;
import com.paymentez.android.rest.model.CreateAuthenticateResponse;
import com.paymentez.android.rest.model.CreateTokenRequest;
import com.paymentez.android.rest.model.CreateTokenResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by mmucito on 13/09/17.
 */

public interface PaymentezService {

    @POST("/v2/card/add")
    Call<CreateTokenResponse> createToken(@Body CreateTokenRequest createTokenRequest);

    @GET("/v2/card_bin/{bin}")
    Call<CardBinResponse> cardBin(@Path("bin") String bin);

    @POST("/v2/3ds/authenticate/")
    Call<CreateAuthenticateResponse> authenticate (@Body CreateAuthenticateRequest createAuthenticateRequest);

}
