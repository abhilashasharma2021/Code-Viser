package com.codeviser.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.baoyz.widget.PullRefreshLayout;
import com.codeviser.Adapter.VedioAdapter;
import com.codeviser.Model.VedioModel;
import com.codeviser.R;
import com.codeviser.other.API_BaseUrl;
import com.codeviser.other.AppConstats.AppConstats;
import com.codeviser.other.AppConstats.SharedHelper;
import com.codeviser.other.Connectivity;
import com.codeviser.other.ProgressBarCustom.CustomDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;


public class FeedsFragment extends Fragment {

    VedioAdapter vedioAdapter;
    ArrayList<VedioModel> vedioModelArrayList = new ArrayList<>();
    RecyclerView rvVideo;
    Connectivity connectivity;
    TextView txt_tiest;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        /*getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);*/
        View view = inflater.inflate(R.layout.fragment_feeds, container, false);
        rvVideo = view.findViewById(R.id.rvVideo);


        PullRefreshLayout layout = view.findViewById(R.id.swipeRefreshLayout);

        layout.setOnRefreshListener(() -> layout.postDelayed(() -> {
            layout.setRefreshing(false);
            showItems();
        }, 2000));

        connectivity=new Connectivity(getActivity());

        rvVideo.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        if (connectivity.isOnline()){
            showItems();
        }else {
            Toast.makeText(getActivity(),"Please check internet connection",Toast.LENGTH_SHORT).show();
        }


      /*  int nightModeFlags = getResources().getConfiguration().uiMode &
                Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                Log.e("gsgssss", "1" );
                int nightMode = AppCompatDelegate.getDefaultNightMode();
                //Set the theme mode for the restarted activity
                if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
                    AppCompatDelegate.setDefaultNightMode
                            (AppCompatDelegate.MODE_NIGHT_NO);
                }
                txt_tiest.setBackgroundColor(Color.WHITE);
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                Log.e("gsgssss", "2" );
                txt_tiest.setBackgroundColor(Color.BLACK);

                break;

            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                Log.e("gsgssss", "3" );
                break;
        }*/

        return view;
    }



    private void showItems() {
        String stUserId = SharedHelper.getKey(getActivity(), AppConstats.USERID);

        CustomDialog dialog = new CustomDialog();
        dialog.showDialog(R.layout.progress_layout, getActivity());

        AndroidNetworking.post(API_BaseUrl.BaseUrl + API_BaseUrl.showImageVideo)
                .addBodyParameter("userID",stUserId)
                .setTag("Show Video and Image")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("ImageVideoFragment", "response: " + response);
                        dialog.hideDialog();
                        vedioModelArrayList = new ArrayList<>();
                        try {
                            if (response.getString("result").equals("true")) {
                                JSONArray jsonArray = new JSONArray(response.getString("data"));
                                if (!response.getString("data").isEmpty()) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        VedioModel model = new VedioModel();
                                        model.setType(jsonObject.getString("image_video_type"));//*image_video_type=0 means video image_video_type=1 means image *//*
                                        model.setPath(jsonObject.getString("path"));
                                        model.setFile(jsonObject.getString("file"));
                                        model.setTitle(jsonObject.getString("title"));
                                        model.setName(jsonObject.getString("admin_name"));
                                        model.setImage(jsonObject.getString("admin_img"));
                                        vedioModelArrayList.add(model);
                                    }

                                    vedioAdapter = new VedioAdapter(vedioModelArrayList, getActivity());
                                    rvVideo.setAdapter(vedioAdapter);
                                }

                            }


                        } catch (JSONException e) {
                            Log.e("ImageVideoFragment", "e: " + e);
                            dialog.hideDialog();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("ImageVideoFragment", "anError: " + anError);
                        dialog.hideDialog();
                    }
                });
    }
}
