package com.codeviser.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.codeviser.R;
import com.codeviser.other.API_BaseUrl;
import com.codeviser.other.AppConstats.AppConstats;
import com.codeviser.other.AppConstats.SharedHelper;
import com.codeviser.other.ProgressBarCustom.CustomDialog;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;

import static com.codeviser.other.API_BaseUrl.BaseUrl;

public class MobileVerifyActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    MaterialButton btn_next;
    String mobile = "";
    String number = "";
    ImageView phone;
    EditText et_verify;
    private GoogleApiClient mCredentialsApiClient;
    private static final int RC_HINT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_verify2);
        btn_next = findViewById(R.id.btn_next);
        phone = findViewById(R.id.phone);

        et_verify = findViewById(R.id.et_verify);


        phone.setOnClickListener(v -> requestHint());


        mCredentialsApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .enableAutoManage(this, this)
                .addApi(Auth.CREDENTIALS_API)
                .build();


        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                number = et_verify.getText().toString();

                if (TextUtils.isEmpty(number)) {
                    Toast.makeText(MobileVerifyActivity.this, "Please enter your number", Toast.LENGTH_SHORT).show();

                } else if (number.length() < 10) {

                    et_verify.setError("Please enter atleast 10 digit mobile number");
                } else {

                    if (number.startsWith("+91")) {
                        number = number.replace("+91", "");
                        getOtp(number);

                    } else {
                        getOtp(number);
                    }


                }
            }
        });
    }

    public void getOtp(String mobileNumber) {

        CustomDialog dialog = new CustomDialog();
        dialog.showDialog(R.layout.progress_layout, this);
        AndroidNetworking.post(API_BaseUrl.BaseUrl + API_BaseUrl.get_otp)
                .addBodyParameter("mobile", mobileNumber)
                .setPriority(Priority.HIGH)
                .setTag("test")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.hideDialog();
                        Log.e("MobileVerifyActivity", "response: " + response);
                        try {


                            if (response.getString("result").equals("true")) {
                                String data = response.getString("data");
                                if (!data.isEmpty()) {
                                    JSONObject jsonObject = new JSONObject(data);

                                    Toasty.success(MobileVerifyActivity.this, response.getString("message"), Toasty.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), OtpActivity.class));
                                    SharedHelper.putKey(getApplicationContext(), AppConstats.OTP, jsonObject.getString("otp"));
                                    SharedHelper.putKey(getApplicationContext(), AppConstats.USERID, jsonObject.getString("userID"));
                                    SharedHelper.putKey(getApplicationContext(), AppConstats.USERMOBILE, jsonObject.getString("userMobile"));
                                    Log.e("MobileVerifyActivity", "otp: " + jsonObject.getString("otp"));

                                }


                            } else {
                                Toasty.success(MobileVerifyActivity.this, response.getString("message"), Toasty.LENGTH_SHORT).show();
                                dialog.hideDialog();
                            }


                        } catch (JSONException e) {
                            Log.e("bgh", "onResponse: " + e.getMessage());
                            dialog.hideDialog();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("hjkjk", "onError: " + anError.getMessage());
                        dialog.hideDialog();

                    }
                });
    }

    private void requestHint() {
        HintRequest hintRequest = new HintRequest.Builder()
                .setPhoneNumberIdentifierSupported(true)
                .build();

        PendingIntent intent = Auth.CredentialsApi.getHintPickerIntent(
                mCredentialsApiClient, hintRequest);
        try {
            startIntentSenderForResult(intent.getIntentSender(), RC_HINT, null, 0, 0, 0);
        } catch (IntentSender.SendIntentException e) {
            Log.e("jghjgj", "Could not start hint picker Intent", e);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("kjghjj", "Connected");
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.d("TAG", "GoogleApiClient is suspended with cause code: " + cause);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("TAG", "GoogleApiClient failed to connect: " + connectionResult);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_HINT) {
            if (resultCode == RESULT_OK) {
                Credential cred = data.getParcelableExtra(Credential.EXTRA_KEY);

                if (cred != null) {
                    Log.e("TAG", "onActivityResult: " + cred.getId());
                    et_verify.setText(cred.getId());

                    Task<Void> task = SmsRetriever.getClient(MobileVerifyActivity.this).startSmsUserConsent(cred.getId());

                    task.addOnCompleteListener(task1 -> {

                        if (task1.isSuccessful()) {
                            Log.e("scuuu", String.valueOf(task1.getResult()) + "rr");
                        }
                    });

                }

            } else {

                Toast.makeText(this, "Didn't select mobile number", Toast.LENGTH_SHORT).show();
            }
        }


    }

}