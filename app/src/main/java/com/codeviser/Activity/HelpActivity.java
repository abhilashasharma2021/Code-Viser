package com.codeviser.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.codeviser.Model.HelpModel;
import com.codeviser.R;
import com.codeviser.databinding.ActivityHelpBinding;
import com.codeviser.other.API_BaseUrl;
import com.codeviser.other.AppConstats.AppConstats;
import com.codeviser.other.AppConstats.SharedHelper;
import com.codeviser.other.ProgressBarCustom.CustomDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class HelpActivity extends AppCompatActivity {
ActivityHelpBinding binding;
String stEmail="",stQuery="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHelpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();

            }
        });

        binding.btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stEmail = binding.txtEmail.getText().toString().trim();
                stQuery = binding.txtQuery.getText().toString().trim();

                if (stQuery.equals("")) {
                    Toast.makeText(HelpActivity.this, "Please mention your query", Toast.LENGTH_SHORT).show();
                } else if (stEmail.equals("")) {
                    Toast.makeText(HelpActivity.this, "Please mention your registerd Email", Toast.LENGTH_SHORT).show();
                } else {
                  //  query_with_admin();
                }
            }
        });


    }


    private void help() {

        String stUSERID = SharedHelper.getKey(getApplicationContext(), AppConstats.USERID);
        CustomDialog dialog = new CustomDialog();
        dialog.showDialog(R.layout.progress_layout, this);

        AndroidNetworking.post(API_BaseUrl.BaseUrl + API_BaseUrl.help_to_support)
                .addBodyParameter("user_id", stUSERID)
                .addBodyParameter("email", stEmail)
                .addBodyParameter("query", stQuery)
                .setTag("send help msg to admin")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("retr5yg", response.toString());
                        dialog.hideDialog();
                        try {
                            if (response.getString("result").equals("Successfully")) {
                                String user_id = response.getString("user_id");
                                String email = response.getString("email");
                                String id = response.getString("id");


                                Toasty.success(HelpActivity.this, response.getString("result"), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(HelpActivity.this, "Please enter registered Email address", Toast.LENGTH_SHORT).show();
                                dialog.hideDialog();
                            }


                        } catch (JSONException e) {
                            Log.e("tyhh", e.getMessage());
                            dialog.hideDialog();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        dialog.hideDialog();
                        Log.e("6yhy6h", anError.getMessage());
                    }
                });
    }

    }