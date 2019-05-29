package com.paymentez.examplestore;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.paymentez.android.model.Card;
import com.paymentez.android.rest.AuthenticationCallback;
import com.paymentez.android.rest.ChallengeCallback;
import com.paymentez.android.rest.PaymentDialogCallback;
import com.paymentez.android.rest.model.CreateAuthenticateResponse;
import com.paymentez.android.rest.model.ErrorResponse;
import com.paymentez.android.rest.model.Order;
import com.paymentez.android.rest.model.PaymentezError;
import com.paymentez.android.rest.model.SdkInfo;
import com.paymentez.examplestore.rest.BackendService;
import com.paymentez.examplestore.rest.RetrofitFactory;
import com.paymentez.examplestore.rest.model.CreateChargeResponse;
import com.paymentez.examplestore.utils.Alert;
import com.paymentez.examplestore.utils.Constants;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.paymentez.android.Paymentez.*;
import static com.paymentez.examplestore.utils.Alert.showPayDialog;

public class CheckoutActivity extends AppCompatActivity {

    LinearLayout buttonSelectPayment;
    ImageView imageViewCCImage;
    TextView textViewCCLastFour;
    Button buttonPlaceOrder;
    Context mContext;
    Activity mActivity;
    String CARD_TOKEN = "";
    int SELECT_CARD_REQUEST = 1004;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        mContext = this;
        mActivity = this;

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        final BackendService backendService = RetrofitFactory.getClient().create(BackendService.class);


        imageViewCCImage = (ImageView) findViewById(R.id.imageViewCCImage);
        textViewCCLastFour = (TextView) findViewById(R.id.textViewCCLastFour);

        buttonPlaceOrder = (Button) findViewById(R.id.buttonPlaceOrder);
        buttonPlaceOrder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (CARD_TOKEN == null || CARD_TOKEN.equals("")) {
                    Alert.show(mContext,
                            "Error",
                            "You Need to Select a Credit Card!");
                } else {
                    final double ORDER_AMOUNT_FOR_CCAPI = 10.5;
                    final double ORDER_AMOUNT_AVOID_CHALENGE = 10;
                    final double ORDER_AMOUNT_FOR_CHALENGE = 100;

                    final String ORDER_ID = "" + System.currentTimeMillis();
                    final String ORDER_DESCRIPTION = "ORDER #" + ORDER_ID;
                    final String DEV_REFERENCE = ORDER_ID;

                    final SdkInfo sdk_info = getThreeDSTransactionData();

                    final Order order = new Order();
                    order.setAmount(ORDER_AMOUNT_AVOID_CHALENGE);
                    order.setDescription(ORDER_DESCRIPTION);
                    order.setDev_reference(DEV_REFERENCE);
                    Log.i("MIO", String.valueOf(order.getAmount()));

                    final Card card = new Card();
                    card.setNumber("4116020000001087");
                    card.setHolderName("Oscar Acelas");
                    card.setExpiryMonth(12);
                    card.setExpiryYear(2030);

                    final ProgressDialog pd = getProgressDialog(mActivity);

                    showPayDialog(mContext,
                            new PaymentDialogCallback() {

                                private   void launchAuthenticate(){
                                    pd.show();
                                    authenticate(mContext, Constants.USER_ID, Constants.USER_EMAIL, order, card, sdk_info, "http://paymentez-stg-hrd.appspot.com/api/v1/test/application_callback/?modirium=True", "SDK", new AuthenticationCallback() {
                                        public void onSuccess(CreateAuthenticateResponse response) {
                                            boolean challenge = response.getAuthentication().getStatus().contentEquals("C");
                                            boolean authenticated = response.getAuthentication().getStatus().contentEquals("Y");

                                            if(challenge)
                                                doChallenge(response);
                                            else if (authenticated)
                                                payment();
                                            else
                                                Alert.show(mContext,
                                                        "Authentication",
                                                        "Status: " + response.getAuthentication().getStatus() + "\n" +
                                                                "Message: " + response.getAuthentication().getReturn_message() + "\n" +
                                                                "Code: " + response.getAuthentication().getReturn_code());
                                            //TODO: Create charge or Save Token to your backend
                                        }

                                        public void onError(PaymentezError error) {
                                            pd.dismiss();
                                            Alert.show(mContext,
                                                    "Error",
                                                    "Type: " + error.getType() + "\n" +
                                                            "Help: " + error.getHelp() + "\n" +
                                                            "Description: " + error.getDescription());

                                            //TODO: Handle error
                                        }
                                    });
                                }

                                private void payment(){
                                    backendService.createCharge(Constants.USER_ID, getSessionId(mContext),
                                            CARD_TOKEN, ORDER_AMOUNT_FOR_CCAPI, DEV_REFERENCE, ORDER_DESCRIPTION).enqueue(new Callback<CreateChargeResponse>() {
                                        @Override
                                        public void onResponse(Call<CreateChargeResponse> call, Response<CreateChargeResponse> response) {
                                            pd.dismiss();
                                            CreateChargeResponse createChargeResponse = response.body();
                                            if(response.isSuccessful() && createChargeResponse != null && createChargeResponse.getTransaction() != null) {
                                                Alert.show(mContext,
                                                        "Successful Charge",
                                                        "status: " + createChargeResponse.getTransaction().getStatus() +
                                                                "\nstatus_detail: " + createChargeResponse.getTransaction().getStatusDetail() +
                                                                "\nmessage: " + createChargeResponse.getTransaction().getMessage() +
                                                                "\ntransaction_id:" + createChargeResponse.getTransaction().getId());
                                            }else {
                                                Gson gson = new GsonBuilder().create();
                                                try {
                                                    ErrorResponse errorResponse = gson.fromJson(response.errorBody().string(), ErrorResponse.class);
                                                    Alert.show(mContext,
                                                            "Error",
                                                            errorResponse.getError().getType());
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<CreateChargeResponse> call, Throwable e) {
                                            pd.dismiss();
                                            Alert.show(mContext,
                                                    "Error",
                                                    e.getLocalizedMessage());
                                        }
                                    });
                                }

                                private void doChallenge(CreateAuthenticateResponse response){
                                    doChallengeThreeDS(mActivity, response, new ChallengeCallback() {

                                        @Override
                                        public void completed(String transactionStatus) {
                                            boolean authenticated = transactionStatus.contentEquals("Y");
                                            if(authenticated)
                                                payment();
                                        }

                                        @Override
                                        public void cancelled() {
                                            Alert.show(mContext,
                                                    "Cancelled",
                                                    "");
                                        }

                                        @Override
                                        public void timedout() {
                                            Alert.show(mContext,
                                                    "Timeout",
                                                    "");
                                        }

                                        @Override
                                        public void protocolError(PaymentezError error) {
                                            Alert.show(mContext,
                                                    "Error",
                                                    "Type: " + error.getType() + "\n" +
                                                            "Help: " + error.getHelp() + "\n" +
                                                            "Description: " + error.getDescription());
                                        }

                                        @Override
                                        public void runtimeError(PaymentezError error) {
                                            Alert.show(mContext,
                                                    "Error",
                                                    "Type: " + error.getType() + "\n" +
                                                            "Help: " + error.getHelp() + "\n" +
                                                            "Description: " + error.getDescription());
                                        }
                                    }, 6);
                                }

                                @Override
                                public void onAccept() {
                                    order.setAmount(ORDER_AMOUNT_FOR_CHALENGE);
                                    Log.i("MIO", String.valueOf(order.getAmount()));
                                    launchAuthenticate();
                                }

                                @Override
                                public void onAvoid() {
                                    launchAuthenticate();
                                }
                            });

                }
            }
        });

        buttonSelectPayment = (LinearLayout) findViewById(R.id.buttonSelectPayment);
        buttonSelectPayment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ListCardsActivity.class);
                startActivityForResult(intent, SELECT_CARD_REQUEST);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == SELECT_CARD_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                CARD_TOKEN = data.getStringExtra("CARD_TOKEN");
                String CARD_TYPE = data.getStringExtra("CARD_TYPE");
                String CARD_LAST4 = data.getStringExtra("CARD_LAST4");

                if (CARD_LAST4 != null && !CARD_LAST4.equals("")) {
                    textViewCCLastFour.setText("XXXX." + CARD_LAST4);
                    imageViewCCImage.setImageResource(Card.getDrawableBrand(CARD_TYPE));
                }

            }
        }
    }
}
