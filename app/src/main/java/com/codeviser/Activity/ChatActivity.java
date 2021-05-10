package com.codeviser.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.codeviser.Adapter.ChatAdapter;
import com.codeviser.Adapter.VedioAdapter;
import com.codeviser.Model.ChatModel;
import com.codeviser.Model.VedioModel;
import com.codeviser.R;
import com.codeviser.other.API_BaseUrl;
import com.codeviser.other.AppConstats.AppConstats;
import com.codeviser.other.AppConstats.SharedHelper;
import com.codeviser.other.ProgressBarCustom.CustomDialog;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {


    public static final int FILE_VIDEO_REQUEST_CODE = 8989;
    public static final int FILE_IMAGE_REQUEST_CODE = 1111;
    ChatAdapter chatAdapter;
    ArrayList<ChatModel> chatModelArrayList = new ArrayList<>();
    RecyclerView recyclechat;
    String filePath = "";
    ImageView imgAttach;
    CardView cardAttach;
    RelativeLayout rlCamera,rlVideo;

    ArrayList<File> fileList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        recyclechat = findViewById(R.id.recyclechat);
        imgAttach = findViewById(R.id.imgAttach);
        cardAttach = findViewById(R.id.cardAttach);
        rlCamera = findViewById(R.id.rlCamera);
        rlVideo = findViewById(R.id.rlVideo);

        imgAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardAttach.setVisibility(View.VISIBLE);
                rlCamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ChatActivity.this, FilePickerActivity.class);
                        intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                                .setCheckPermission(true)
                                .setShowImages(true)
                                .setShowVideos(false)
                                .enableImageCapture(true)
                                .setMaxSelection(3)
                                .setSkipZeroSizeFiles(true)
                                .build());
                        startActivityForResult(intent, FILE_IMAGE_REQUEST_CODE);
                    }
                });


                rlVideo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ChatActivity.this, FilePickerActivity.class);
                        intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                                .setCheckPermission(true)
                                .setShowImages(false)
                                .setShowVideos(true)
                                .enableImageCapture(true)
                                .setMaxSelection(1)
                                .setSkipZeroSizeFiles(true)
                                .build());
                        startActivityForResult(intent, FILE_VIDEO_REQUEST_CODE);
                    }
                });
            }
        });


        recyclechat.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        chat();




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FILE_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            List<MediaFile> mediaFiles = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);
            if (mediaFiles != null) {

                for (int i = 0; i < mediaFiles.size(); i++) {
                    Log.e("cscj", String.valueOf(mediaFiles.get(i).getPath()));

                    filePath = mediaFiles.get(i).getPath();

                    Log.e("fncjdshfckj", filePath);

                    fileList.add(new File(filePath));




                }

                for (int i = 0; i < fileList.size(); i++) {

                    Log.e("skasksasasa", fileList.get(i).toString());
                }

               // uploadFiles(fileList, "0", strDescription);


            } else {

            }
        }

        //////VIDEO/////////


        if (requestCode == FILE_VIDEO_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            List<MediaFile> mediaFiles = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);

            if (mediaFiles != null) {

                for (int i = 0; i < mediaFiles.size(); i++) {

                    filePath = mediaFiles.get(i).getPath();

                    Log.e("retgrgerfg", filePath);

                    fileList.add(new File(filePath));




                }

              //  uploadFiles(fileList, "1", strDescription);

            } else {

            }
        }

    }

    public void chat() {
        String groupId = SharedHelper.getKey(getApplicationContext(), AppConstats.GroupId);
        CustomDialog dialog = new CustomDialog();
        dialog.showDialog(R.layout.progress_layout, this);

        AndroidNetworking.post(API_BaseUrl.BaseUrl + API_BaseUrl.show_groups_chat)
                .addBodyParameter("group_id", groupId)
                .setPriority(Priority.HIGH)
                .setTag("show Chat")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("ChatActivity", "onResponse: " +response);
                        dialog.hideDialog();
                        chatModelArrayList=new ArrayList<>();
                        try {
                            if (response.getString("result").equals("true")){
                                String chat=response.getString("chat");
                                if (!chat.isEmpty()){
                                    JSONArray jsonArray=new JSONArray(chat);
                                    for (int i = 0; i <jsonArray.length() ; i++) {
                                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                                        ChatModel model=new ChatModel();
                                        model.setGroupId(jsonObject.getString("group_id"));
                                        model.setMessage(jsonObject.getString("message"));
                                        model.setTime(jsonObject.getString("time"));
                                        model.setFile(jsonObject.getString("file"));
                                        model.setPath(jsonObject.getString("path"));
                                       // model.setUserName(jsonObject.getString("path"));
                                        //model.setType(jsonObject.getString(""));
                                        chatModelArrayList.add(model);

                                    }
                                    chatAdapter= new ChatAdapter(chatModelArrayList, ChatActivity.this);
                                    recyclechat.setAdapter(chatAdapter);
                                }

                            }
                        } catch (JSONException e) {
                            dialog.hideDialog();
                            Log.e("ChatActivity", "onResponse: " +e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("ChatActivity", "onError: " +anError);
                        dialog.hideDialog();
                    }
                });

    }

}