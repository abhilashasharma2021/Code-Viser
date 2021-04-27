package com.codeviser.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.codeviser.Adapter.HelpAdapter;
import com.codeviser.Adapter.HomeAdapter;
import com.codeviser.Model.HelpModel;
import com.codeviser.Model.HomeModel;
import com.codeviser.R;
import com.codeviser.databinding.ActivityCreateProfileBinding;
import com.codeviser.databinding.ActivityHelpBinding;
import com.codeviser.other.API_BaseUrl;
import com.codeviser.other.AppConstats.AppConstats;
import com.codeviser.other.AppConstats.SharedHelper;
import com.codeviser.other.ProgressBarCustom.CustomDialog;
import com.google.android.gms.common.api.Api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HelpActivity extends AppCompatActivity {
ActivityHelpBinding binding;
RecyclerView.LayoutManager layoutManager;
    ArrayList<HelpModel> helpArrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHelpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        layoutManager = new LinearLayoutManager(HelpActivity.this, RecyclerView.VERTICAL, false);
        binding.rvHelp.setLayoutManager(layoutManager);
        binding.rvHelp.setHasFixedSize(true);

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();

            }
        });

        help();
    }


    private void help() {

        String stUSERID = SharedHelper.getKey(getApplicationContext(), AppConstats.USERID);
        CustomDialog dialog = new CustomDialog();
        dialog.showDialog(R.layout.progress_layout, this);

        AndroidNetworking.post(API_BaseUrl.BaseUrl + API_BaseUrl.help_to_support)
                .addBodyParameter("userID", stUSERID)
                .setTag("showing help successfully")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("HelpActivity", "onResponse: " + response);
                        dialog.hideDialog();
                        helpArrayList=new ArrayList<>();
                        try {
                            if (response.getString("result").equals("true")) {
                                String data = response.getString("data");
                                if (!data.isEmpty()){
                                    JSONArray jsonArray=new JSONArray(data);
                                    for (int i = 0; i <jsonArray.length() ; i++) {
                                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                                        HelpModel model=new HelpModel();
                                        model.setTitle(jsonObject.getString("title1"));
                                        model.setDescription(jsonObject.getString("text1"));
                                        helpArrayList.add(model);
                                    }
                                    HelpAdapter    helpAdapter = new HelpAdapter(HelpActivity.this, helpArrayList);
                                    binding.rvHelp.setAdapter(helpAdapter);
                                }



                            }
                        } catch (JSONException e) {
                            Log.e("HelpActivity", "onResponse: " + e);
                            dialog.hideDialog();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("HelpActivity", "onResponse: " + anError);
                        dialog.hideDialog();
                    }
                });
    }
}