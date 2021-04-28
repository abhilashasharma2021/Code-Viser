package com.codeviser.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.codeviser.R;
import com.codeviser.databinding.ActivityChangePasswordBinding;
import com.codeviser.databinding.ActivityForgotPasswordBinding;
import com.codeviser.other.API_BaseUrl;
import com.codeviser.other.ProgressBarCustom.CustomDialog;
import com.google.android.gms.common.api.Api;

import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;

public class ForgotPasswordActivity extends AppCompatActivity {
ActivityForgotPasswordBinding binding;
String str_email="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        binding= ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_email=binding.etEmail.getText().toString().trim();

                if (validation()){

                    forgot_password();

                }
                else {
                    validation();
                }


            }
        });



    }
    private Boolean validation(){

        Boolean aBoolean=false;
        if (binding.etEmail.getText().toString().isEmpty()){

            binding.etEmail.setError("Registered Email Address Must Required");
        }
        else {
            aBoolean=true;
        }

        return aBoolean;

    }


    public void forgot_password(){
        CustomDialog dialog=new CustomDialog();
        dialog.showDialog(R.layout.progress_layout,this);

        AndroidNetworking.post(API_BaseUrl.BaseUrl + API_BaseUrl.forget_password)
                .addBodyParameter("email",str_email)
                .setTag("mail sent successfully")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("regrt", response.toString() );
                        dialog.hideDialog();
                        try {
                            if (response.getString("result").equals("An email has been sent to you")){
                                Toasty.success(ForgotPasswordActivity.this, response.getString("result"), Toast.LENGTH_SHORT).show();
                                // startActivity(new Intent(ChangePasswordActivity.this,ChangePasswordActivity.class));

                                binding.etEmail.setText("");

                            }
                            else {
                                Toasty.error(ForgotPasswordActivity.this, response.getString("result"), Toast.LENGTH_SHORT).show();
                                dialog.hideDialog();
                            }
                        } catch (JSONException e) {
                            Log.e("kckjjc", e.getMessage() );
                            dialog.hideDialog();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("rgtrhbt", anError.getMessage() );
                        dialog.hideDialog();

                    }
                });

    }
}