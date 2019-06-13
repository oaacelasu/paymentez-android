package com.paymentez.android;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.kount.api.DataCollector;
import com.modirum.threedsv2.core.ButtonCustomization;
import com.modirum.threedsv2.core.ChallengeParameters;
import com.modirum.threedsv2.core.ChallengeStatusReceiver;
import com.modirum.threedsv2.core.CompletionEvent;
import com.modirum.threedsv2.core.ConfigParameters;
import com.modirum.threedsv2.core.LabelCustomization;
import com.modirum.threedsv2.core.ProtocolConstants;
import com.modirum.threedsv2.core.ProtocolErrorEvent;
import com.modirum.threedsv2.core.RuntimeErrorEvent;
import com.modirum.threedsv2.core.TextBoxCustomization;
import com.modirum.threedsv2.core.ThreeDS2Service;
import com.modirum.threedsv2.core.ThreeDSecurev2Service;
import com.modirum.threedsv2.core.ToolbarCustomization;
import com.modirum.threedsv2.core.Transaction;
import com.modirum.threedsv2.core.UiCustomization;
import com.modirum.threedsv2.core.Warning;
import com.paymentez.android.model.Card;
import com.paymentez.android.rest.AuthenticationCallback;
import com.paymentez.android.rest.ChallengeCallback;
import com.paymentez.android.rest.InitCallback;
import com.paymentez.android.rest.PaymentezService;
import com.paymentez.android.rest.PaymenezClient;
import com.paymentez.android.rest.TokenCallback;
import com.paymentez.android.rest.model.CardBinResponse;
import com.paymentez.android.rest.model.CreateAuthenticateRequest;
import com.paymentez.android.rest.model.CreateAuthenticateResponse;
import com.paymentez.android.rest.model.CreateDebitWTokenRequest;
import com.paymentez.android.rest.model.CreateDebitWTokenResponse;
import com.paymentez.android.rest.model.CreateTokenRequest;
import com.paymentez.android.rest.model.CreateTokenResponse;
import com.paymentez.android.rest.model.DebitWTokenCallback;
import com.paymentez.android.rest.model.Ephemeral;
import com.paymentez.android.rest.model.ErrorResponse;
import com.paymentez.android.rest.model.Order;
import com.paymentez.android.rest.model.PaymentezError;
import com.paymentez.android.rest.model.SdkInfo;
import com.paymentez.android.rest.model.User;
//import com.squareup.picasso.Downloader;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by mmucito on 24/05/16.
 */
public class Paymentez{

    public static boolean TEST_MODE;
    private static String PAYMENTEZ_CLIENT_APP_CODE;
    private static String PAYMENTEZ_CLIENT_APP_KEY;

    static int MERCHANT_ID = 500005;
    static int KOUNT_ENVIRONMENT = DataCollector.ENVIRONMENT_TEST;

    static PaymentezService paymentezService;
    static ThreeDS2Service service;
    static  ConfigParameters configParam;
    static  UiCustomization uiConfig;
    static  Transaction transaction;

    /**
     * Init library
     *
     * @param test_mode false to use production environment
     * @param paymentez_client_app_code provided by Paymentez.
     * @param paymentez_client_app_key provided by Paymentez.
     */
    public static void setEnvironment(boolean test_mode, String paymentez_client_app_code, String paymentez_client_app_key, final Context mContext, InitCallback mCallback) {
        TEST_MODE = test_mode;
        PAYMENTEZ_CLIENT_APP_CODE = paymentez_client_app_code;
        PAYMENTEZ_CLIENT_APP_KEY = paymentez_client_app_key;
        if (TEST_MODE){
            KOUNT_ENVIRONMENT = DataCollector.ENVIRONMENT_TEST;

        }else{
            KOUNT_ENVIRONMENT = DataCollector.ENVIRONMENT_PRODUCTION;

        }
        initThreeDS(mContext, mCallback);
    }

    /**
     * Set your Risk Merchant ID
     * @param merchant_id Insert your valid merchant ID
     *
     */
    public static void setRiskMerchantId(int merchant_id){
        MERCHANT_ID = merchant_id;
    }

    public static PaymentezService getPaymentezService(Context mContext){
        paymentezService = PaymenezClient.getClient(mContext, TEST_MODE, PAYMENTEZ_CLIENT_APP_CODE, PAYMENTEZ_CLIENT_APP_KEY).create(PaymentezService.class);

        return paymentezService;
    }

    public static SdkInfo getThreeDSTransactionData(String type){
        transaction = service.createTransaction(type,  "2.1.0");

        SdkInfo sdkInfo = new SdkInfo();
        sdkInfo.setTrans_id(transaction.getAuthenticationRequestParameters().getSDKTransactionID());
        sdkInfo.setReference_number(transaction.getAuthenticationRequestParameters().getSDKReferenceNumber());
        sdkInfo.setApp_id( transaction.getAuthenticationRequestParameters().getSDKAppID());
        sdkInfo.setEnc_data(transaction.getAuthenticationRequestParameters().getDeviceData());
        sdkInfo.setMax_timeout(5);
        sdkInfo.setOptions_IF(3);
        sdkInfo.setOptions_UI("01,02,03,04,05");

        Gson gson = new GsonBuilder().create();
        Ephemeral ephemeral = gson.fromJson(transaction.getAuthenticationRequestParameters().getSDKEphemeralPublicKey(), Ephemeral.class);
        sdkInfo.setEphem_pub_key(ephemeral.getKey());
        return sdkInfo;
    }
    public static ProgressDialog getProgressDialog(Activity currentActivity){
        return transaction.getProgressView(currentActivity);
    }

    public static void doChallengeThreeDS(final Activity activity, final CreateDebitWTokenResponse response, final ChallengeCallback callback, final int timeout){

        final ChallengeParameters challengeParameters = new ChallengeParameters();
        challengeParameters.set3DSServerTransactionID(response.getTree_ds().getAuthentication().getReference_id());
        challengeParameters.setAcsTransactionID(response.getTree_ds().getSdk_response().getAcs_trans_id());
        challengeParameters.setACSSignedContent(response.getTree_ds().getSdk_response().getAcs_signed_content());
        challengeParameters.setAcsRefNumber(response.getTree_ds().getSdk_response().getAcs_reference_number());

        new Thread() {
            public void run() {
                transaction.doChallenge(activity, challengeParameters, new ChallengeStatusReceiver() {
                    @Override
                    public void completed(CompletionEvent completionEvent) {
//At this point, the Merchant app can contact the 3DS Server
//to determine the result of the challenge

                        callback.completed("Transaction Id: " + completionEvent.getSDKTransactionID() + "\n" +
                                "Amount: " + response.getTransaction().getAmount() + "\n" +

                                "Status: " + response.getTransaction().getStatus() +" -> "+ response.getTree_ds().getAuthentication().getReturn_message() +"\n" +

                                "Cavv: " + response.getTree_ds().getAuthentication().getCavv() + "\n" +
                                "Xid: " + response.getTree_ds().getAuthentication().getXid() + "\n" +
                                "Eci: " + response.getTree_ds().getAuthentication().getEci() + "\n" +
                                "Version: " + response.getTree_ds().getAuthentication().getVersion()+ "\n" +
                                "ReferenceID: " + response.getTree_ds().getAuthentication().getReference_id(), completionEvent.getTransactionStatus());

                    }

                    @Override
                    public void cancelled() {
//can go to Cancelled view if desired
                        callback.cancelled();
                    }

                    @Override
                    public void timedout() {
                        callback.timedout();
                        //can show error alert
                    }

                    @Override
                    public void protocolError(ProtocolErrorEvent protocolErrorEvent) {
                        PaymentezError error
                                = new PaymentezError(protocolErrorEvent.getErrorMessage().getErrorMessageType(), protocolErrorEvent.getErrorMessage().getErrorDetail(), protocolErrorEvent.getErrorMessage().getErrorDescription());
                        callback.protocolError(error);
                        //can show error alert

                    }

                    @Override
                    public void runtimeError(RuntimeErrorEvent runtimeErrorEvent) {
                        PaymentezError error
                                = new PaymentezError(runtimeErrorEvent.getErrorMessage(), "", "");
                        callback.runtimeError(error);
                        //can show error alert

                    }
                }, timeout);
            }
        }.start();
    }


    public static void getImageBin(Context mContext, String bin){
        paymentezService = PaymenezClient.getClient(mContext, TEST_MODE, PAYMENTEZ_CLIENT_APP_CODE, PAYMENTEZ_CLIENT_APP_KEY).create(PaymentezService.class);
        paymentezService.cardBin(bin).enqueue(new Callback<CardBinResponse>() {
            @Override
            public void onResponse(Call<CardBinResponse> call, Response<CardBinResponse> response) {
                CardBinResponse cardBinResponse = response.body();
                if(response.isSuccessful()) {

                }else {

                }
            }

            @Override
            public void onFailure(Call<CardBinResponse> call, Throwable e) {

            }
        });
    }

    /**
     * The simplest way to create a token, using a {@link Card} and {@link TokenCallback}. T
     * @param mContext Context of the Main Activity
     * @param uid User identifier. This is the identifier you use inside your application; you will receive it in notifications.
     * @param email Email of the user initiating the purchase. Format: Valid e-mail format.
     * @param card the {@link Card} used to create this payment token
     * @param callback a {@link TokenCallback} to receive either the token or an error
     */
    public static void addCard(Context mContext, @NonNull final String uid, @NonNull final String email, @NonNull final Card card, @NonNull final TokenCallback callback) {

        paymentezService = PaymenezClient.getClient(mContext, TEST_MODE, PAYMENTEZ_CLIENT_APP_CODE, PAYMENTEZ_CLIENT_APP_KEY).create(PaymentezService.class);
        User user = new User();
        user.setId(uid);
        user.setEmail(email);
        user.setFiscal_number(card.getFiscal_number());

        CreateTokenRequest createTokenRequest = new CreateTokenRequest();
        createTokenRequest.setSessionId(getSessionId(mContext));
        createTokenRequest.setCard(card);
        createTokenRequest.setUser(user);

        paymentezService.createToken(createTokenRequest).enqueue(new Callback<CreateTokenResponse>() {
            @Override
            public void onResponse(Call<CreateTokenResponse> call, Response<CreateTokenResponse> response) {
                CreateTokenResponse createTokenResponse = response.body();
                if(response.isSuccessful()) {
                    callback.onSuccess(createTokenResponse.getCard());
                    return;
                }else {
                    PaymentezError error
                            = new PaymentezError("Exception", "", "General Error");
                    try {
                        Gson gson = new GsonBuilder().create();
                        ErrorResponse errorResponse = gson.fromJson(response.errorBody().string(), ErrorResponse.class);
                        callback.onError(errorResponse.getError());
                        return;
                    } catch (Exception e) {
                        try {
                            error = new PaymentezError("Exception", "Http Code: " + response.code(), response.message());
                        } catch (Exception e2) {
                        }
                    }
                    callback.onError(error);
                    return;

                }
            }

            @Override
            public void onFailure(Call<CreateTokenResponse> call, Throwable e) {
                PaymentezError error
                        = new PaymentezError("Network Exception",
                        "Invoked when a network exception occurred communicating to the server.", e.getLocalizedMessage());
                callback.onError(error);
                return;
            }
        });
    }



    public static void authenticate(Context mContext, @NonNull final String uid, @NonNull final String email, @NonNull final Order order, @NonNull final Card card, @NonNull final SdkInfo sdk_info, @NonNull final String term_url, @NonNull final String device_type,  @NonNull final AuthenticationCallback callback) {

        paymentezService = PaymenezClient.getClient(mContext, TEST_MODE, PAYMENTEZ_CLIENT_APP_CODE, PAYMENTEZ_CLIENT_APP_KEY).create(PaymentezService.class);
        User user = new User();
        user.setId(uid);
        user.setEmail(email);

        CreateAuthenticateRequest createAuthenticateRequest = new CreateAuthenticateRequest();
        createAuthenticateRequest.setUser(user);
        createAuthenticateRequest.setOrder(order);
        createAuthenticateRequest.setCard(card);
        createAuthenticateRequest.setSdf_info(sdk_info);
        createAuthenticateRequest.setTerm_url(term_url);
        createAuthenticateRequest.setDevice_type(device_type);

        paymentezService.authenticate(createAuthenticateRequest).enqueue(new Callback<CreateAuthenticateResponse>() {
            @Override
            public void onResponse(Call<CreateAuthenticateResponse> call, Response<CreateAuthenticateResponse> response) {
                CreateAuthenticateResponse createAuthenticateResponse = response.body();
                Log.d("MIO", response.toString());
                if(response.isSuccessful()) {
                    callback.onSuccess(createAuthenticateResponse);

                    return;
                }else {
                    PaymentezError error
                            = new PaymentezError("Exception", "", "General Error");
                    try {
                        Gson gson = new GsonBuilder().create();
                        ErrorResponse errorResponse = gson.fromJson(response.errorBody().string(), ErrorResponse.class);
                        callback.onError(errorResponse.getError());
                        return;
                    } catch (Exception e) {
                        try {
                            error = new PaymentezError("Exception", "Http Code: " + response.code(), response.message());
                        } catch (Exception e2) {
                        }
                    }
                    callback.onError(error);
                    return;

                }
            }

            @Override
            public void onFailure(Call<CreateAuthenticateResponse> call, Throwable e) {
                PaymentezError error
                        = new PaymentezError("Network Exception",
                        "Invoked when a network exception occurred communicating to the server.", e.getLocalizedMessage());
                callback.onError(error);
                return;
            }
        });
    }


    public static void debitWToken(Context mContext, @NonNull final String uid, @NonNull final String email, @NonNull final Order order, @NonNull final String cardToken, @NonNull final SdkInfo sdk_info, @NonNull final String term_url, @NonNull final String device_type,  @NonNull final DebitWTokenCallback callback) {

        paymentezService = PaymenezClient.getClient(mContext, TEST_MODE, PAYMENTEZ_CLIENT_APP_CODE, PAYMENTEZ_CLIENT_APP_KEY).create(PaymentezService.class);
        User user = new User();
        user.setId(uid);
        user.setEmail(email);

        CreateDebitWTokenRequest createDebitWTokenRequest = new CreateDebitWTokenRequest();
        createDebitWTokenRequest.setUser(user);
        createDebitWTokenRequest.setOrder(order);
        createDebitWTokenRequest.setCard(cardToken);
        createDebitWTokenRequest.setExtra_params(term_url, device_type, false, sdk_info);

        paymentezService.debitWToken(createDebitWTokenRequest).enqueue(new Callback<CreateDebitWTokenResponse>() {
            @Override
            public void onResponse(Call<CreateDebitWTokenResponse> call, Response<CreateDebitWTokenResponse> response) {
                CreateDebitWTokenResponse createDebitWTokenResponse = response.body();
                Log.d("MIO", response.toString());
                if(response.isSuccessful()) {
                    callback.onSuccess(createDebitWTokenResponse);

                    return;
                }else {
                    PaymentezError error
                            = new PaymentezError("Exception", "", "General Error");
                    try {
                        Gson gson = new GsonBuilder().create();
                        ErrorResponse errorResponse = gson.fromJson(response.errorBody().string(), ErrorResponse.class);
                        callback.onError(errorResponse.getError());
                        return;
                    } catch (Exception e) {
                        try {
                            error = new PaymentezError("Exception", "Http Code: " + response.code(), response.message());
                        } catch (Exception e2) {
                        }
                    }
                    callback.onError(error);
                    return;

                }
            }

            @Override
            public void onFailure(Call<CreateDebitWTokenResponse> call, Throwable e) {
                PaymentezError error
                        = new PaymentezError("Network Exception",
                        "Invoked when a network exception occurred communicating to the server.", e.getLocalizedMessage());
                callback.onError(error);
                return;
            }
        });
    }

    /**
     * The session ID is a parameter Paymentez use for fraud purposes.
     *
     * @return session_id
     */
    public static String getSessionId(Context mContext){
        String sessionID = UUID.randomUUID().toString();
        final String deviceSessionID = sessionID.replace("-", "");


        // Configure the collector
        final DataCollector dataCollector = com.kount.api.DataCollector.getInstance();
        if(TEST_MODE)
            dataCollector.setDebug(true);
        else
            dataCollector.setDebug(false);
        dataCollector.setContext(mContext);
        dataCollector.setMerchantID(MERCHANT_ID);
        dataCollector.setEnvironment(KOUNT_ENVIRONMENT);
        dataCollector.setLocationCollectorConfig(DataCollector.LocationConfig.COLLECT);

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                dataCollector.collectForSession(deviceSessionID, new com.kount.api.DataCollector.CompletionHandler() {
                    @Override
                    public void completed(String s) {

                    }

                    @Override
                    public void failed(String s, final DataCollector.Error error) {

                    }

                });
            }
        });

        return deviceSessionID;
    }

    private static class InitThreeDSTask extends AsyncTask<Void, Void, List<Warning>> {

        Context mContext;
        InitCallback callback;

        public InitThreeDSTask(Context mContext, InitCallback callback) {
            this.mContext = mContext;
            this.callback = callback;
        }

        @Override
        protected List<Warning> doInBackground(Void... voids) {
            service = new ThreeDSecurev2Service(mContext);
            configParam = new ConfigParameters();
            uiConfig = new UiCustomization();

            TextBoxCustomization textBoxCustomization = new TextBoxCustomization();
            textBoxCustomization.setCornerRadius(10);
            uiConfig.setTextBoxCustomization(textBoxCustomization);

            LabelCustomization labelCustomization = new LabelCustomization();
            labelCustomization.setTextFontName(Typeface.MONOSPACE.toString());
            labelCustomization.setTextFontSize(18);
            uiConfig.setLabelCustomization(labelCustomization);

            ToolbarCustomization toolbarCustomization = new ToolbarCustomization();
            toolbarCustomization.setBackgroundColor("#4CAF50");
            toolbarCustomization.setTextColor("#FFFFFF");
            toolbarCustomization.setTextFontSize(28);
            uiConfig.setToolbarCustomization(toolbarCustomization);

            ButtonCustomization buttonCustomization = new ButtonCustomization();
            buttonCustomization.setBackgroundColor("#4CAF50");
            buttonCustomization.setCornerRadius(10);
            buttonCustomization.setTextColor("#FFFFFF");
            uiConfig.setButtonCustomization(buttonCustomization, UiCustomization.ButtonType.SUBMIT);


            service.initialize(mContext, configParam, null, uiConfig);

            List<Warning> warnings = service.getWarnings();

            return warnings;
        }


        @Override
        protected void onPostExecute(List<Warning> result) {
            Log.d("MIO", result.toString());
            if(result.isEmpty()){
                callback.onSuccess();
            }
            else {
                PaymentezError error
                        = new PaymentezError("Exception", "", "General Error");
                callback.onError(error);
            }

        }
    }

    public static void initThreeDS(Context mContext, @NonNull final InitCallback callback){
        new InitThreeDSTask(mContext, callback).execute();
    }
}
