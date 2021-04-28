package com.codeviser.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.chaos.view.PinView;
import com.codeviser.R;
import com.codeviser.other.API_BaseUrl;
import com.codeviser.other.AppConstats.AppConstats;
import com.codeviser.other.AppConstats.SharedHelper;
import com.codeviser.other.ProgressBarCustom.CustomDialog;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;

public class OtpActivity extends AppCompatActivity {
    MaterialButton btn_next;
    PinView pinView;
    TextView txNumber,txTimer;
    String strPinView = "";
    private static final int SMS_CONSENT_REQUEST = 2;
    private final BroadcastReceiver smsVerificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {

                Bundle extras = intent.getExtras();

                if (extras != null) {

                    Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

                    if (status != null) {

                        switch (status.getStatusCode()) {
                            case CommonStatusCodes.SUCCESS:

                                Intent consentIntent = extras.getParcelable(SmsRetriever.EXTRA_CONSENT_INTENT);
                                try {

                                    startActivityForResult(consentIntent, SMS_CONSENT_REQUEST);
                                } catch (ActivityNotFoundException e) {

                                    Log.e("sjdsi", e.getMessage(), e);
                                }
                                break;
                            case CommonStatusCodes.TIMEOUT:

                                break;
                        }
                    }

                }


            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        pinView = findViewById(R.id.pinView);
        btn_next = findViewById(R.id.btn_next);
        txNumber = findViewById(R.id.txNumber);
        txTimer = findViewById(R.id.txTimer);
        String otp = SharedHelper.getKey(getApplicationContext(), AppConstats.OTP);
        String getMobile = SharedHelper.getKey(getApplicationContext(), AppConstats.USERMOBILE);

        txNumber.setText("We send it to the number"+ getMobile);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                strPinView = pinView.getText().toString().trim();


                if (TextUtils.isEmpty(strPinView)) {

                } else if (strPinView.length() < 4) {

                    Toast.makeText(OtpActivity.this, "Enter 4 dgit", Toast.LENGTH_SHORT).show();
                } else if (!strPinView.equals(otp)) {

                    Toast.makeText(OtpActivity.this, "You entered wrong otp", Toast.LENGTH_SHORT).show();

                } else {

                    verifyOtp(getMobile, strPinView);

                }

            }
        });


        new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {

                txTimer.setText("Resend Code : " + millisUntilFinished / 1000);

            }

            public void onFinish() {

                //txTimer.setText("Done!!!!");

            }

        }.start();


    }


    @Override
    protected void onResume() {
        super.onResume();

        SmsRetriever.getClient(this).startSmsUserConsent(null);
        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        registerReceiver(smsVerificationReceiver, intentFilter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SMS_CONSENT_REQUEST) {
            if (resultCode == RESULT_OK) {

                if (data != null) {
                    String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                    if (message != null) {

                        message = message.replaceAll("[^0-9]", "");
                        message = message.substring(0, 4);

                        Log.e("OTP GET", "onActivityResult: " + message);

                        pinView = findViewById(R.id.pinView);
                        pinView.setText(message);

                        String otp = SharedHelper.getKey(getApplicationContext(), AppConstats.OTP);

                        strPinView = pinView.getText().toString().trim();

                     /*   if (TextUtils.isEmpty(strPinView)) {

                        } else if (strPinView.length() < 4) {

                            Toast.makeText(OtpActivity.this, "Enter 4 dgit", Toast.LENGTH_SHORT).show();
                        } else if (!strPinView.equals(otp)) {

                            Toast.makeText(OtpActivity.this, "You entered wrong otp", Toast.LENGTH_SHORT).show();

                        } else {

                            startActivity(new Intent(getApplicationContext(), PasswordActivity.class));
                        }*/
                    }


                } else {

                    Log.e("errorMsg", "error");
                }
            }
        }
    }

    private void verifyOtp(String stMobile, String strPinView) {
        CustomDialog dialog = new CustomDialog();
        dialog.showDialog(R.layout.progress_layout, this);

        AndroidNetworking.post(API_BaseUrl.BaseUrl + API_BaseUrl.otp_verify)
                .addBodyParameter("mobile", stMobile)
                .addBodyParameter("otp", "1234")
                .setPriority(Priority.HIGH)
                .setTag("OTP successfully verified")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("skclkxck", response.toString());
                        dialog.hideDialog();
                        try {
                            if (response.getString("result").equals("true")) {
                                String data = response.getString("data");

                                if (!data.isEmpty()) {


                                    Toasty.success(OtpActivity.this, response.getString("message"), Toasty.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), PasswordActivity.class));
                                }


                            } else {
                                Toasty.success(OtpActivity.this, response.getString("message"), Toasty.LENGTH_SHORT).show();
                                dialog.hideDialog();
                            }
                        } catch (JSONException e) {
                            Log.e("wrfwef", e.getMessage());
                            dialog.hideDialog();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("ewrgfdsg", anError.getMessage());
                        dialog.hideDialog();


                    }
                });


    }

}


