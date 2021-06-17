package com.codeviser.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.codeviser.R;
import com.codeviser.other.API_BaseUrl;
import com.codeviser.other.AppConstats.AppConstats;
import com.codeviser.other.AppConstats.SharedHelper;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import android.app.Activity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {
    String userId = "";
    ImageView ivSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

      //  getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);

        setContentView(R.layout.activity_splash);

        ivSplash=findViewById(R.id.ivSplash);

        showSplash();
       /* new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), WelcomeActivity.class));
                finish();
            }
        }, 2000);*/

        userId = SharedHelper.getKey(getApplicationContext(), AppConstats.USERID);

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.READ_SMS,
                        Manifest.permission.RECEIVE_SMS,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CALL_PHONE

                ).withListener(new MultiplePermissionsListener() {


            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (userId.equals("")) {

                            startActivity(new Intent(getApplicationContext(), WelcomeActivity.class));
                            finish();

                        } else {
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();

                        }


                    }
                }, 3000);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).check();

    }



    private void showSplash(){

       /* CustomDialog dialog = new CustomDialog();
        dialog.showDialog(R.layout.progress_layout, this);*/
        AndroidNetworking.post(API_BaseUrl.BaseUrl + API_BaseUrl.show_slash_screen)
                .setPriority(Priority.HIGH)
                .setTag("test")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                       // dialog.hideDialog();
                        Log.e("SplashActivity", "onResponse: " +response);

                        try {
                            if (response.getString("result").equals("true")){
                                String data=response.getString("data");
                             JSONObject jsonObject=new JSONObject(data);

                                Log.e("SplashActivity", "onResponse: " +response.getString("path")+jsonObject.getString("image"));
                                try {
                                    Glide.with(SplashActivity.this).load(response.getString("path")+jsonObject.getString("image")).into(ivSplash);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e) {
                           // dialog.hideDialog();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                       // dialog.hideDialog();
                    }
                });
    }
}