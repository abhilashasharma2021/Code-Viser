package com.codeviser.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    MaterialButton login_btn;
    EditText etEmail,etPassword,etMobile;
    TextView  txtforgotPass,txtNew;
    String stEmail="",stPassword="",stMobile="",stFinal="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtforgotPass=findViewById(R.id.txtforgotPass);
        txtNew=findViewById(R.id.txtNew);
        etPassword=findViewById(R.id.etPassword);
        etMobile=findViewById(R.id.etMobile);
        etEmail=findViewById(R.id.etEmail);
        txtforgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,ForgotPasswordActivity.class));
            }
        });

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                stEmail=s+"";
                stMobile="";
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        etMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                stMobile=s+"";
                stEmail="";
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        login_btn=findViewById(R.id.login_btn);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stPassword=etPassword.getText().toString().trim();

                Log.e("dsvsvsew", stEmail+"stEmail");
                Log.e("dsvsvsew", stMobile+"stMobile");


                if (!stEmail.equals("")){
                    stFinal=stEmail;
                }
                if (!stMobile.equals("")){
                    stFinal=stMobile;
                }
                if (stFinal.equals("")){
                    Toast.makeText(LoginActivity.this, "Enter your detail", Toast.LENGTH_SHORT).show();
                }else {
                    login();
                }





            }
        });

        txtNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MobileVerifyActivity.class));
            }
        });
        Configuration configuration=new Configuration();
        int currentNightMode = configuration.uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                // Night mode is not active, we're using the light theme
                Log.e("jhjvjhvjhv", "no active " );
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                // Night mode is active, we're using dark theme
                Log.e("jhjvjhvjhv", "active " );
                break;
        }
    }



    private  void  login(){
        CustomDialog dialog = new CustomDialog();
        dialog.showDialog(R.layout.progress_layout, LoginActivity.this);

        Log.e("LoginActivity", "stEmail: " +stEmail);
        Log.e("LoginActivity", "stPassword: " +stFinal);
        AndroidNetworking.post(API_BaseUrl.BaseUrl + API_BaseUrl.login)
                .addBodyParameter("email", stFinal)
                .addBodyParameter("password", stPassword)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("grtgfnjgh", response.toString());
                        dialog.hideDialog();
                        try {
                            if (response.getString("result").equals("true")) {
                                String data= response.getString("data");
                                JSONObject jsonObject=new JSONObject(data);


                                SharedHelper.putKey(LoginActivity.this, AppConstats.USEREMAIL, jsonObject.getString("email"));
                                SharedHelper.putKey(LoginActivity.this, AppConstats.USERID, jsonObject.getString("userID"));
                                SharedHelper.putKey(LoginActivity.this, AppConstats.USERNAME, jsonObject.getString("userName"));
                                SharedHelper.putKey(LoginActivity.this, AppConstats.USERDOB, jsonObject.getString("dob"));
                                SharedHelper.putKey(LoginActivity.this, AppConstats.USERGENDER, jsonObject.getString("gender"));
                                SharedHelper.putKey(LoginActivity.this, AppConstats.USERIMAGE, jsonObject.getString("userImage"));
                                SharedHelper.putKey(LoginActivity.this, AppConstats.USERPASSWORD, jsonObject.getString("user_password"));




                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                Toast.makeText(LoginActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                                finish();

                            } else {
                                Toast.makeText(LoginActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                                dialog.hideDialog();

                            }
                        } catch (JSONException e) {
                            Log.e("fhgfrnhgf", e.getMessage());
                            dialog.hideDialog();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("fhgfrnhgf", anError.getMessage());
                        dialog.hideDialog();

                    }
                });


    }
}