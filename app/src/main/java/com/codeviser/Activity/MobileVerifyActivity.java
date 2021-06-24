package com.codeviser.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;

import static com.codeviser.other.API_BaseUrl.BaseUrl;

public class MobileVerifyActivity extends AppCompatActivity {

    MaterialButton btnNext;
    String number = "";
    ImageView phone;
    EditText et_verify;
    RelativeLayout rlLogin;
    /*Register project in google developer conole without firebase
    * google Developers Console - Google Cloud Platform https://console.developers.google.com › project
*/
    ////////////////////google interation with firebase/////////////////////
    public static final int RC_GOOGLE_SIGN_IN = 9999;
    RelativeLayout relGoogle;
    SignInButton signInButton;
    public static GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_verify2);
        btnNext = findViewById(R.id.btnNext);
        phone = findViewById(R.id.phone);
        et_verify = findViewById(R.id.et_verify);
        relGoogle = findViewById(R.id.relGoogle);
        rlLogin = findViewById(R.id.rlLogin);



        googleSignIn();

        rlLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MobileVerifyActivity.this,LoginActivity.class));
            }
        });



        btnNext.setOnClickListener(new View.OnClickListener() {
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
                                    SharedHelper.putKey(getApplicationContext(), AppConstats.PROVIDER, "Normal");
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



    ////////////////////////*Google interration without firebase*//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void googleSignIn() {


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();


        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        relGoogle.setOnClickListener(view -> signIn());
    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RC_GOOGLE_SIGN_IN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {

            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            if (account != null) {
                String name = account.getDisplayName();
                String email = account.getEmail();
                String authID = account.getId();
                Uri imageURI = account.getPhotoUrl();
                String image = String.valueOf(imageURI);

                //      String regID = SharedHelper.getKey(LoginorRegisterActivity.this, AppConstats.REG_ID);
                Log.e("wedwedwedwed", name + "," + email + "," + authID + "," + image);
                // soicalGoogleLogin(authID, "google", name, email, image, regID);
                 socialLogin(authID, "google",name,email);
            }


        } catch (ApiException e) {

            Log.e("kjckjsc", "signInResult:failed code=" + e.getStatusCode());
        }


    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private void socialLogin(String authID, String provider, String name, String email){

        AndroidNetworking.post(API_BaseUrl.BaseUrl + API_BaseUrl.social_login)
                .addBodyParameter("authID",authID)
                .addBodyParameter("auth_provider",provider)
                .addBodyParameter("name",name)
                .addBodyParameter("email",email)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("MobileVerifyActivity", "onResponse: " +response);
                        try {
                            if (response.getString("result").equals("true")){

                                JSONObject jsonObject=new JSONObject(response.getString("data"));

                                SharedHelper.putKey(getApplicationContext(), AppConstats.USERID, jsonObject.getString("userID"));
                                SharedHelper.putKey(getApplicationContext(), AppConstats.PROVIDER, jsonObject.getString("auth_provider"));

                             Toasty.success(MobileVerifyActivity.this,response.getString("message"),Toasty.LENGTH_SHORT).show();
                                startActivity(new Intent(MobileVerifyActivity.this, MainActivity.class));
                            }
                        } catch (JSONException e) {
                            Log.e("MobileVerifyActivity", "e: " +e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("MobileVerifyActivity", "onError: " +anError);
                    }
                });

    }
    }