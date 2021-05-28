package com.codeviser.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
    EditText etEmail,etPassword;
    TextView  txtforgotPass,txtNew;
    String stEmail="",stPassword="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtforgotPass=findViewById(R.id.txtforgotPass);
        txtNew=findViewById(R.id.txtNew);
        etPassword=findViewById(R.id.etPassword);
        etEmail=findViewById(R.id.etEmail);
        txtforgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,ForgotPasswordActivity.class));
            }
        });
        login_btn=findViewById(R.id.login_btn);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stEmail=etEmail.getText().toString().trim();
                stPassword=etPassword.getText().toString().trim();


                if (stEmail.isEmpty()){
                   etEmail.setError("Registered Email Address Must Required");
                }else if (stPassword.isEmpty()){
                   etPassword.setError("Password Must Required");
                }
                else {
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
    }



    private  void  login(){
        CustomDialog dialog = new CustomDialog();
        dialog.showDialog(R.layout.progress_layout, LoginActivity.this);
        AndroidNetworking.post(API_BaseUrl.BaseUrl + API_BaseUrl.login)
                .addBodyParameter("email", stEmail)
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