package com.codeviser.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.codeviser.Adapter.ChatAdapter;
import com.codeviser.Adapter.HomeAdapter;
import com.codeviser.Adapter.ShowHelpChatAdapter;
import com.codeviser.Model.HomeModel;
import com.codeviser.Model.ShowHelpChatModel;
import com.codeviser.R;
import com.codeviser.databinding.ActivityHelpBinding;
import com.codeviser.databinding.ActivityHelpChatBinding;
import com.codeviser.other.API_BaseUrl;
import com.codeviser.other.AppConstats.AppConstats;
import com.codeviser.other.AppConstats.SharedHelper;
import com.codeviser.other.ProgressBarCustom.CustomDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HelpChat extends AppCompatActivity {

    ActivityHelpChatBinding binding;
   String strMsg="";
    ArrayList<ShowHelpChatModel> helpArrayList = new ArrayList<>();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityHelpChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String groupName = SharedHelper.getKey(HelpChat.this, AppConstats.GROUPENAME);
        String image = SharedHelper.getKey(HelpChat.this, AppConstats.GROUPEIMAGE);


        if (!groupName.isEmpty()){
            binding.txName.setText(groupName);
        }

        if (!image.isEmpty()){
            try {
                Glide.with(HelpChat.this).load(image).into(binding.profileIV);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        binding.rlSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strMsg=binding.etMsg.getText().toString().trim();
                if (strMsg.equals("")){

                    Toast.makeText(HelpChat.this, "Type a message", Toast.LENGTH_SHORT).show();
                }else{

                    type_chat();
                }

            }
        });


        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Log.e("guytguuygf","");
                show_HelpChat();
            }
        };

        binding.rvShowHelpChat.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

    }



    private void show_HelpChat(){
        String chatId = SharedHelper.getKey(HelpChat.this, AppConstats.GroupId);

        CustomDialog dialog = new CustomDialog();
        dialog.showDialog(R.layout.progress_layout, this);
        AndroidNetworking.post(API_BaseUrl.BaseUrl + API_BaseUrl.show_helpgroups_chat)
                .addBodyParameter("group_id", chatId)
                .setPriority(Priority.HIGH)
                .setTag("show Chat")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("hdgfdh", "onResponse: " +response);
                        helpArrayList=new ArrayList<>();
                        dialog.hideDialog();
                        try {
                            if (response.getString("result").equals("true")){
                                String chat=response.getString("chat");
                                JSONArray jsonArray=new JSONArray(chat);
                                for (int i = 0; i <jsonArray.length() ; i++) {
                                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                                    String message=jsonObject.getString("message");
                                    String user_detail=jsonObject.getString("user_detail");
                                    JSONObject jsonObject1=new JSONObject(user_detail);


                                    ShowHelpChatModel model=new ShowHelpChatModel();
                                    model.setMessage(jsonObject.getString("message"));
                                    model.setImage(jsonObject1.getString("userImage"));
                                    model.setPath(jsonObject1.getString("path"));
                                    helpArrayList.add(model);



                                }

                                ShowHelpChatAdapter  chatAdapter= new ShowHelpChatAdapter(helpArrayList, HelpChat.this);
                                binding.rvShowHelpChat.setAdapter(chatAdapter);


                            }
                        } catch (JSONException e) {
                            Log.e("HelpChat", "e: " +e.getMessage());
                            dialog.hideDialog();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("HelpChat", "anError: " +anError.getMessage());
                        dialog.hideDialog();
                    }
                });
    }


    private  void type_chat(){

        String groupId = SharedHelper.getKey(HelpChat.this, AppConstats.GroupId);
        String userId = SharedHelper.getKey(HelpChat.this, AppConstats.USERID);
        AndroidNetworking.post(API_BaseUrl.BaseUrl + API_BaseUrl.helpgroups_chat)
                .addBodyParameter("group_id",groupId)
                .addBodyParameter("user_id",userId)
                .addBodyParameter("message1",strMsg)
                .setTag("Send help chat")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("HelpChat", "onResponse: " +response);


                        try {
                            if (response.getString("result").equals("true")){

                                binding.etMsg.setText("");
                                show_HelpChat();


                            }
                        } catch (JSONException e) {
                            Log.e("HelpChat", "e: " +e.getMessage());
                        }


                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.e("HelpChat", "onError: " +anError);

                    }
                });



    }

    @Override
    protected void onResume() {
        super.onResume();
        show_HelpChat();

        registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter("FBR-IMAGE"));
    }


    @Override
    protected void onPause() {

        unregisterReceiver(mRegistrationBroadcastReceiver);

        super.onPause();
    }

}