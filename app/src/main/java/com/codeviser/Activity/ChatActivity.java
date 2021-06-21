package com.codeviser.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.bumptech.glide.Glide;
import com.codeviser.Adapter.ChatAdapter;
import com.codeviser.Model.ChatModel;
import com.codeviser.R;
import com.codeviser.other.API_BaseUrl;
import com.codeviser.other.AppConstats.AppConstats;
import com.codeviser.other.AppConstats.SharedHelper;
import com.codeviser.other.ProgressBarCustom.CustomDialog;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

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
    String getGroupType="",getGroupImage="",getGroupName="",strDescription="";
    RelativeLayout rlAdmin,rlSend;
    TextView txName;
     ImageView profile_image,imgSend,img_back;
    ProgressDialog dialog;
    EditText etMsg;
    String stType="";

    ArrayList<File> fileList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        recyclechat = findViewById(R.id.recyclechat);
        imgAttach = findViewById(R.id.imgAttach);
        cardAttach = findViewById(R.id.cardAttach);
        rlCamera = findViewById(R.id.rlCamera);
        img_back = findViewById(R.id.img_back);
        rlVideo = findViewById(R.id.rlVideo);
        rlSend = findViewById(R.id.rlSend);
        imgSend = findViewById(R.id.imgSend);
        etMsg = findViewById(R.id.etMsg);
        profile_image = findViewById(R.id.profile_image);
        rlAdmin = findViewById(R.id.rlAdmin);
        txName = findViewById(R.id.txName);
        getGroupType = SharedHelper.getKey(getApplicationContext(), AppConstats.GROUPTYPE);
        getGroupImage = SharedHelper.getKey(getApplicationContext(), AppConstats.GROUPEIMAGE);
        getGroupName = SharedHelper.getKey(getApplicationContext(), AppConstats.GROUPENAME);
        /*type = 0 means  one way communication like  channel and type = 1  two way communication*/

        if (!getGroupName.isEmpty()){
            txName.setText(getGroupName);
        }

        if (!getGroupImage.isEmpty()){
            try {
                Glide.with(ChatActivity.this).load(getGroupImage).into(profile_image);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (!getGroupType.isEmpty()){
            if (getGroupType.equals("0")) {

                rlAdmin.setVisibility(View.VISIBLE);
                rlSend.setVisibility(View.GONE);
            }else {
                rlAdmin.setVisibility(View.GONE);
                rlSend.setVisibility(View.VISIBLE);
            }

        }






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
                                .setMaxSelection(1)
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

        show_chat();




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

                cardAttach.setVisibility(View.GONE);
                imgSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        strDescription = etMsg.getText().toString().trim();

                        uploadFiles(fileList,  strDescription);

                    }
                });



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

                cardAttach.setVisibility(View.GONE);
                imgSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        strDescription = etMsg.getText().toString().trim();

                        uploadFiles(fileList,  strDescription);

                    }
                });


            } else {

            }
        }

    }

    public void show_chat() {
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

                                        String user_detail=jsonObject.getString("user_detail");
                                        JSONObject jsonObject1=new JSONObject(user_detail);
                                        String file=jsonObject.getString("file");
                                        if (!file.isEmpty()){
                                            JSONArray array=new JSONArray(file);
                                            for (int j = 0; j <array.length() ; j++) {
                                                JSONObject object=array.getJSONObject(j);

                                                ChatModel model=new ChatModel();
                                                model.setGroupId(jsonObject.getString("group_id"));
                                                model.setMessage(jsonObject.getString("message"));
                                                model.setTime(jsonObject.getString("strtotime"));
                                                model.setUserName(jsonObject1.getString("userName"));
                                                model.setUserImage(jsonObject1.getString("userImage"));
                                                model.setUserPath(jsonObject1.getString("path"));
                                                model.setFile(object.getString("file"));
                                                model.setPath(object.getString("path"));
                                                model.setType(object.getString("type"));
                                                chatModelArrayList.add(model);


                                            }
                                            chatAdapter= new ChatAdapter(chatModelArrayList, ChatActivity.this);
                                            recyclechat.setAdapter(chatAdapter);
                                        }

                                        }


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

    private void uploadFiles(ArrayList<File> fileList, String strDescription) {

        String groupId = SharedHelper.getKey(getApplicationContext(), AppConstats.GroupId);
        String userId = SharedHelper.getKey(getApplicationContext(), AppConstats.USERID);
        dialog = new ProgressDialog(this);
        dialog.setTitle("Uploading files");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(150, TimeUnit.SECONDS)
                .readTimeout(150, TimeUnit.SECONDS)
                .writeTimeout(150, TimeUnit.SECONDS)
                .build();


        AndroidNetworking.upload(API_BaseUrl.BaseUrl + API_BaseUrl.groups_chat)
                .addMultipartParameter("group_id", groupId)
                .addMultipartParameter("user_id", userId)
                .addMultipartParameter("message1", strDescription)
                .addMultipartFileList("file[]",fileList)
                .setOkHttpClient(okHttpClient)
                .setTag("uploadFiles")
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        Log.e("sdgdfg", totalBytes + "");
                        double p = ((bytesUploaded / (float) totalBytes) * 100);
                        dialog.setProgress((int) p);
                        dialog.setMessage("please wait...." + new DecimalFormat("##.##").format(p) + " %");
                        dialog.show();

                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {


                        Log.e("ChatActivity", "onResponse: " +response);
                        try {
                            if (response.has("result")) {

                                String strResult = response.getString("result");
                                String message = response.getString("message");
                                if (strResult.equals("true")) {

                                    etMsg.setText("");
                                    show_chat();
                                    TastyToast.makeText(getApplicationContext(), "Download Successful!", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                                    dialog.hide();
                                } else {
                                    dialog.hide();
                                    TastyToast.makeText(getApplicationContext(), "Down" +
                                            "loading failed ! Try again later", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                                }

                            } else {
                                dialog.hide();
                                TastyToast.makeText(getApplicationContext(), "Downloading failed ! Try again later", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                            }
                        }   catch (JSONException e) {
                                    dialog.hide();
                                    Log.e("fsdggvc", Objects.requireNonNull(e.getMessage()));
                            }





                    }

                    @Override
                    public void onError(ANError anError) {
                        dialog.hide();
                        Log.e("shfdjh", Objects.requireNonNull(anError.getMessage()));
                    }
                });


    }



}