package com.paymentez.examplestore;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.paymentez.android.rest.model.CreateDebitWTokenResponse;
import com.paymentez.android.rest.model.DebitWTokenCallback;
import com.paymentez.android.rest.model.ErrorResponse;
import com.paymentez.android.rest.model.Order;
import com.paymentez.android.rest.model.PaymentezError;
import com.paymentez.android.rest.model.SdkInfo;
import com.paymentez.examplestore.rest.BackendService;
import com.paymentez.examplestore.rest.RetrofitFactory;
import com.paymentez.examplestore.rest.model.CreateChargeResponse;
import com.paymentez.examplestore.utils.Alert;
import com.paymentez.examplestore.utils.Constants;

import org.w3c.dom.Text;

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

    TextView product1Quantity;
    TextView product2Quantity;
    TextView product1Price;
    TextView product2Price;
    TextView totalToPay;
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
        product1Quantity = (TextView) findViewById(R.id.textView1);
        product2Quantity = (TextView) findViewById(R.id.textView12);
        product1Price = (TextView) findViewById(R.id.textView3);
        product2Price = (TextView) findViewById(R.id.textView32);
        totalToPay = (TextView) findViewById(R.id.textView323);


        buttonPlaceOrder = (Button) findViewById(R.id.buttonPlaceOrder);
        buttonPlaceOrder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (CARD_TOKEN == null || CARD_TOKEN.equals("")) {
                    Alert.show(mContext,
                            "Error",
                            "You Need to Select a Credit Card!");
                } else {
                    final double ORDER_AMOUNT = totalToPay.getText().equals("$15.00")?15.0:100.0;

                    final String ORDER_ID = "" + System.currentTimeMillis();
                    final String ORDER_DESCRIPTION = "ORDER #" + ORDER_ID;
                    final String DEV_REFERENCE = ORDER_ID;

                    final SdkInfo sdk_info = getThreeDSTransactionData();

                    final Order order = new Order();
                    order.setAmount(ORDER_AMOUNT);
                    order.setDescription(ORDER_DESCRIPTION);
                    order.setDev_reference(DEV_REFERENCE);
                    order.setVat(0);
                    Log.i("MIO", String.valueOf(order.getAmount()));


                    final ProgressDialog pd = getProgressDialog(mActivity);

                    showPayDialog(mContext,
                            new PaymentDialogCallback() {

                                private void launchAuthenticate(){
                                    pd.show();
                                    debitWToken(mContext, Constants.USER_ID, Constants.USER_EMAIL, order, CARD_TOKEN, sdk_info, "http://paymentez-stg-hrd.appspot.com/api/v1/test/application_callback/?modirium=True", "SDK", new DebitWTokenCallback() {
                                        public void onSuccess(CreateDebitWTokenResponse response) {
                                            boolean challenge = response.getTree_ds().getAuthentication().getStatus().contentEquals("C");
                                            boolean authenticated = response.getTree_ds().getAuthentication().getStatus().contentEquals("Y");

                                            if(challenge)
                                                doChallenge(response);
                                            else if (authenticated){
                                                pd.dismiss();
                                                Alert.show(mContext,
                                                        "Authenticated!",
                                                        "Transaction Id: " + response.getTransaction().getId() + "\n" +
                                                                "Amount: " + response.getTransaction().getAmount() + "\n" +

                                                                "Status: " + response.getTransaction().getStatus() +" -> "+ response.getTree_ds().getAuthentication().getReturn_message() +"\n" +

                                                                "Cavv: " + response.getTree_ds().getAuthentication().getCavv() + "\n" +
                                                                "Xid: " + response.getTree_ds().getAuthentication().getXid() + "\n" +
                                                                "Eci: " + response.getTree_ds().getAuthentication().getEci() + "\n" +
                                                                "Version: " + response.getTree_ds().getAuthentication().getVersion()+ "\n" +
                                                                "ReferenceID: " + response.getTree_ds().getAuthentication().getReference_id());
                                            } else{
                                                pd.dismiss();
                                                Alert.show(mContext,
                                                        "Something went wrong!",
                                                        "Transaction Id: " + response.getTransaction().getId() + "\n" +
                                                                "Amount: " + response.getTransaction().getAmount() + "\n" +

                                                                "Status: " + response.getTransaction().getStatus() +" -> "+ response.getTree_ds().getAuthentication().getReturn_message() +"\n" +

                                                                "Cavv: " + response.getTree_ds().getAuthentication().getCavv() + "\n" +
                                                                "Xid: " + response.getTree_ds().getAuthentication().getXid() + "\n" +
                                                                "Eci: " + response.getTree_ds().getAuthentication().getEci() + "\n" +
                                                                "Version: " + response.getTree_ds().getAuthentication().getVersion()+ "\n" +
                                                                "ReferenceID: " + response.getTree_ds().getAuthentication().getReference_id());
                                            }
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
                                            CARD_TOKEN, ORDER_AMOUNT, DEV_REFERENCE, ORDER_DESCRIPTION).enqueue(new Callback<CreateChargeResponse>() {
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

                                private void doChallenge(CreateDebitWTokenResponse response){
                                    doChallengeThreeDS(mActivity, response, new ChallengeCallback() {

                                        @Override
                                        public void completed(final String message, final String transactionStatus) {
                                            pd.dismiss();
                                            boolean authenticated = transactionStatus.contentEquals("Y");
                                            if(authenticated)
                                            {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        Alert.show(mContext,
                                                                "Authenticated!",
                                                                message);
                                                    }
                                                });

                                            }else{
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Alert.show(mContext,
                                                                "Something went wrong",
                                                                message);
                                                    }
                                                });

                                            }
                                        }

                                        @Override
                                        public void cancelled() {
                                            pd.dismiss();
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Alert.show(mContext,
                                                            "Cancelled",
                                                            "");
                                                }
                                            });

                                        }

                                        @Override
                                        public void timedout() {
                                            pd.dismiss();
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Alert.show(mContext,
                                                            "Timeout",
                                                            "");
                                                }
                                            });

                                        }

                                        @Override
                                        public void protocolError(final PaymentezError error) {
                                            pd.dismiss();
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Alert.show(mContext,
                                                            "Error",
                                                            "Type: " + error.getType() + "\n" +
                                                                    "Help: " + error.getHelp() + "\n" +
                                                                    "Description: " + error.getDescription());
                                                }
                                            });

                                        }

                                        @Override
                                        public void runtimeError(final PaymentezError error) {
                                            pd.dismiss();
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Alert.show(mContext,
                                                            "Error",
                                                            "Type: " + error.getType() + "\n" +
                                                                    "Help: " + error.getHelp() + "\n" +
                                                                    "Description: " + error.getDescription());
                                                }
                                            });

                                        }
                                    }, 6);
                                }

                                @Override
                                public void onAccept() {
                                    launchAuthenticate();
                                }

                                @Override
                                public void onAvoid() {
                                    payment();
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
            case R.id.menu_1:
                product1Quantity.setText("1");
                product2Quantity.setText("1");
                product1Price.setText("$5.00");
                product2Price.setText("$10.00");
                totalToPay.setText("$15.00");
                return true;
            case R.id.menu_2:
                product1Quantity.setText("6");
                product2Quantity.setText("7");
                product1Price.setText("$30.00");
                product2Price.setText("$70.00");
                totalToPay.setText("$100.00");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_checkout_menu, menu);
        return true;
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
