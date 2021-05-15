package com.codeviser.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.codeviser.Adapter.SubscriptionAdapter;
import com.codeviser.Model.SubscriptionModel;
import com.codeviser.R;
import com.codeviser.databinding.FragmentSubscribtionBinding;
import com.codeviser.other.API_BaseUrl;
import com.codeviser.other.ProgressBarCustom.CustomDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SubscribtionFragment extends Fragment {
    FragmentSubscribtionBinding binding;
    private Context context;
    private View view;
    SubscriptionAdapter adapter;
    private ArrayList<SubscriptionModel> subscriptionList=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= FragmentSubscribtionBinding.inflate(getLayoutInflater(),container,false);
        view=binding.getRoot();
        context=getActivity();


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);
        binding.rvSubscription.setLayoutManager(mLayoutManager);


        showSubscription();
        return view;

    }


    private void showSubscription(){

          CustomDialog dialog = new CustomDialog();
        dialog.showDialog(R.layout.progress_layout, getActivity());
        AndroidNetworking.post(API_BaseUrl.BaseUrl + API_BaseUrl.show_subscription)
                .setPriority(Priority.HIGH)
                .setTag("test")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("SubscribtionFragment", "onResponse: " +response);
                        dialog.hideDialog();
                        subscriptionList=new ArrayList<>();
                        try {
                            if (response.getString("result").equals("true")){
                                String data=response.getString("data");
                                JSONArray jsonArray=new JSONArray(data);
                                for (int i = 0; i <jsonArray.length() ; i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    SubscriptionModel model=new SubscriptionModel();
                                    model.setSubscriptionID(jsonObject.getString("subscriptionID"));
                                    model.setSubscriptionTitle(jsonObject.getString("title"));
                                    model.setSubscriptionPrice(jsonObject.getString("price"));
                                    model.setSubscriptionValidity(jsonObject.getString("validity"));
                                    model.setSubscriptionDescription(jsonObject.getString("description"));
                                    model.setSubscriptionImage(jsonObject.getString("image"));
                                    model.setPath(jsonObject.getString("path"));
                                    subscriptionList.add(model);
                                }
                                adapter = new SubscriptionAdapter(context, subscriptionList);
                                binding.rvSubscription.setAdapter(adapter);
                            }
                        } catch (JSONException e) {
                            dialog.hideDialog();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        dialog.hideDialog();
                    }
                });
    }
}