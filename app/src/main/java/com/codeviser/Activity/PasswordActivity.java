package com.codeviser.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

import es.dmoral.toasty.Toasty;

public class PasswordActivity extends AppCompatActivity {

    MaterialButton btn_next;
    String strConfirm="",strPassword="";
    EditText etConfirmPass,etPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        btn_next=findViewById(R.id.btn_next);
        etPass=findViewById(R.id.etPass);
        etConfirmPass=findViewById(R.id.etConfirmPass);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                strConfirm=etConfirmPass.getText().toString().trim();
                strPassword=etPass.getText().toString().trim();


                if (strPassword.equals("")){
                    Toast.makeText(PasswordActivity.this,"Please Enter Password",Toast.LENGTH_LONG).show();

                }
                else if (strConfirm.equals("")){
                    Toast.makeText(PasswordActivity.this, "Please enter confirm password", Toast.LENGTH_SHORT).show();
                }else if (!strConfirm.equals(strPassword)){
                    Toast.makeText(PasswordActivity.this, "Password not match !! try again", Toast.LENGTH_SHORT).show();
                }else {

                    insertPassword (strPassword,strConfirm);
                }
            }
        });
    }

    private void insertPassword(String strPassword, String strConfirm){
        String USERID = SharedHelper.getKey(getApplicationContext(), AppConstats.USERID);
        CustomDialog dialog = new CustomDialog();
        dialog.showDialog(R.layout.progress_layout, this);

        AndroidNetworking.post(API_BaseUrl.BaseUrl+API_BaseUrl.match_password)
                .addBodyParameter("userID",USERID)
                .addBodyParameter("password",strPassword)
                .addBodyParameter("confirm_password",strConfirm)
                .setPriority(Priority.HIGH)
                .setTag("Sussessful")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("dfvbcv ",response.toString());
                        dialog.hideDialog();
                        try {
                            if (response.getString("result").equals("true")){
                                String data=response.getString("data");

                                if (!data.isEmpty()){
                                    Toasty.success(PasswordActivity.this,response.getString("message"),Toasty.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), CreateProfileActivity.class));

                                }

                            }
                            else {
                                Toasty.success(PasswordActivity.this,response.getString("message"),Toasty.LENGTH_SHORT).show();
                                dialog.hideDialog();
                            }
                        } catch (JSONException e) {
                            Log.e("dvfdvbf",e.getMessage());
                            dialog.hideDialog();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("grfthgftb",anError.getMessage());
                        dialog.hideDialog();


                    }
                });



    }
}