package com.codeviser.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.codeviser.R;
import com.codeviser.databinding.ActivityChangePasswordBinding;
import com.codeviser.other.API_BaseUrl;
import com.codeviser.other.AppConstats.AppConstats;
import com.codeviser.other.AppConstats.SharedHelper;
import com.codeviser.other.ProgressBarCustom.CustomDialog;
import com.google.android.gms.common.api.Api;

import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;

public class ChangePasswordActivity extends AppCompatActivity {
  ActivityChangePasswordBinding binding;
    String strNewPassword="",strConfirm="",stUserId="",stUserOldPassword="",strOld="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

         stUserId = SharedHelper.getKey(getApplicationContext(), AppConstats.USERID);
        stUserOldPassword = SharedHelper.getKey(getApplicationContext(), AppConstats.USERPASSWORD);

         binding.ivBack.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 finish();
             }
         });
       binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strConfirm=binding.etConfirmPassword.getText().toString().trim();
                strNewPassword=binding.etNewPassword.getText().toString().trim();
                strOld=binding.etOldPassword.getText().toString().trim();

                if (!stUserOldPassword.equals(strOld)){
                    Toast.makeText(ChangePasswordActivity.this,"Please Enter Correct Old Password",Toast.LENGTH_LONG).show();

                }
                else if (strNewPassword.equals("")){
                    Toast.makeText(ChangePasswordActivity.this, "Please enter your new password", Toast.LENGTH_SHORT).show();
                }else if (!strConfirm.equals(strNewPassword)){
                    Toast.makeText(ChangePasswordActivity.this, "Password not match !! try again", Toast.LENGTH_SHORT).show();
                }else {

                    change_password (strOld,strNewPassword,strConfirm);
                }
            }
        });
    }

    public void change_password(String strOld, String strNewPassword, String strConfirm){
        CustomDialog dialog=new CustomDialog();
        dialog.showDialog(R.layout.progress_layout,this);

        Log.e("dlvkdlv", stUserId );
        Log.e("dlvkdlv", this.strOld);
        Log.e("dlvkdlv", this.strNewPassword);
        AndroidNetworking.post(API_BaseUrl.BaseUrl + API_BaseUrl.change_password)
                .addBodyParameter("user_id",stUserId)
                .addBodyParameter("old_password", strOld)
                .addBodyParameter("new_password",strNewPassword)
                .addBodyParameter("confirm_password",strConfirm)
                .setTag("")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("regrt", response.toString() );
                        dialog.hideDialog();
                        try {
                            if (response.getString("result").equals("Successful")){


                                binding.etNewPassword.setText("");
                                binding.etConfirmPassword.setText("");
                                binding.etOldPassword.setText("");

                                Toasty.success(ChangePasswordActivity.this,response.getString("result"), Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toasty.error(ChangePasswordActivity.this,response.getString("result"), Toast.LENGTH_SHORT).show();
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