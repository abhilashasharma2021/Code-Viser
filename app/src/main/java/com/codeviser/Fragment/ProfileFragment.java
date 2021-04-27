package com.codeviser.Fragment;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.codeviser.Activity.CreateProfileActivity;
import com.codeviser.Activity.MainActivity;
import com.codeviser.Adapter.ProfileAdapter;
import com.codeviser.Adapter.SearchAdapter;
import com.codeviser.Model.HomeModel;
import com.codeviser.Model.ProfileModel;
import com.codeviser.R;
import com.codeviser.other.API_BaseUrl;
import com.codeviser.other.AppConstats.AppConstats;
import com.codeviser.other.AppConstats.SharedHelper;
import com.codeviser.other.ProgressBarCustom.CustomDialog;
import com.google.android.gms.common.api.Api;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;

public class ProfileFragment extends Fragment {

    EditText etName, etGender;
    TextView txDob, txtPhone;
    ImageView ivBack, img;
    RelativeLayout rl,rlDate;
    Button btnNext;
    String strUserName="",strDob="",stMobile="",stGender="";
    private static final String IMAGE_DIRECTORY = "/directory";
    private int GALLERY = 1, CAMERA = 2;
    File f;
    DatePickerDialog datePickerDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        etName = view.findViewById(R.id.etName);
        etGender = view.findViewById(R.id.etGender);
        rl = view.findViewById(R.id.rl);
        txDob = view.findViewById(R.id.txDob);
        txtPhone = view.findViewById(R.id.txtPhone);
        img = view.findViewById(R.id.img);
        btnNext = view.findViewById(R.id.btnNext);
        ivBack = view.findViewById(R.id.ivBack);
        rlDate = view.findViewById(R.id.rlDate);
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              showPictureDialog();
            }
        });



        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strUserName=etName.getText().toString().trim();
                strDob=txDob.getText().toString().trim();
                stGender=etGender.getText().toString().trim();

                update_profile(strUserName,strDob,stGender);
                //  Toast.makeText(ProfileActivity.this, "please wait", Toast.LENGTH_SHORT).show();

            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });


       rlDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {


                              txDob.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);

                                strDob = dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year;
                            }
                        }, mYear, mMonth, mDay);

                datePickerDialog.show();

            }
        });



        show_profile();
        return view;
    }


    private void show_profile() {
        String stUserId = SharedHelper.getKey(getActivity(), AppConstats.USERID);
        CustomDialog dialog = new CustomDialog();
        dialog.showDialog(R.layout.progress_layout, getActivity());

        AndroidNetworking.initialize(getActivity());
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().protocols(Arrays.asList(Protocol.HTTP_1_1))
                .build();
        AndroidNetworking.initialize(getActivity(), okHttpClient);

        AndroidNetworking.post(API_BaseUrl.BaseUrl + API_BaseUrl.show_profile)
                .addBodyParameter("userID", stUserId)
                .setOkHttpClient(okHttpClient)
                .setTag("SHOW profile successfully")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("rtgytfh", response.toString());
                        dialog.hideDialog();

                        try {
                            if (response.getString("result").equals("Update successfully")) {

                                String userID = response.getString("userID");
                                String email = response.getString("email");
                                String username = response.getString("userName");
                                String mobile = response.getString("userMobile");
                                String gender = response.getString("gender");
                                String dob = response.getString("dob");
                                String profile_image = response.getString("userImage");
                                String path = response.getString("path");


                                Log.e("ProfileActivity", "image: " + path + profile_image);


                                etName.setText(username);
                                txDob.setText(dob);
                                txtPhone.setText(mobile);
                                etGender.setText(gender);

                                if (!profile_image.equals("")) {
                                    try {

                                        Glide.with(getActivity()).load(path + profile_image).error(R.drawable.image1).into(img);
                                    } catch (Exception e) {
                                    }
                                } else {

                                }

                            }
                        } catch (JSONException e) {
                            Log.e("rtyrtyhtr", e.getMessage());
                            dialog.hideDialog();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("regrtht", anError.getMessage());
                        dialog.hideDialog();

                    }
                });

    }

    public void showPictureDialog() {

        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Action");
        String[] pictureDialogItems = {"Select photo from gallery", "Capture image from camera"};

        builder.setItems(pictureDialogItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which) {

                    case 0:
                        choosePhotoFromGallery();
                        break;

                    case 1:
                        captureFromCamera();
                        break;
                }

            }
        });

        builder.show();
    }


    public void choosePhotoFromGallery() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(intent, GALLERY);
    }

    public void captureFromCamera() {

        Intent intent_2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent_2, CAMERA);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                    String path = saveImage(bitmap);

                    Log.e("ProfileActivity", "path: " + path);
                    Log.e("ProfileActivity", "bitmap: " + bitmap);
                    Toast.makeText(getActivity(), "Image Saved!", Toast.LENGTH_SHORT).show();
                    img.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            img.setImageBitmap(thumbnail);
            saveImage(thumbnail);
            Toast.makeText(getActivity(), "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);

        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");

            Log.e("ProfileActivity", "f: " + f);
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(getActivity(),
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.e("ProfileActivity", "File Saved::---&gt;" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    public void update_profile(String strUserName, String strDate, String stGender){

        String stUserId = SharedHelper.getKey(getActivity(), AppConstats.USERID);
        CustomDialog dialog=new CustomDialog();
        dialog.showDialog(R.layout.progress_layout,getActivity());

        AndroidNetworking.initialize(getActivity());
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().protocols(Arrays.asList(Protocol.HTTP_1_1))
                .build();
        AndroidNetworking.initialize(getActivity(), okHttpClient);

        AndroidNetworking.upload(API_BaseUrl.BaseUrl + API_BaseUrl.update_profile)
                .addMultipartParameter("username",strUserName)
                .addMultipartParameter("dob",strDate)
                .addMultipartParameter("gender",stGender)
                .addMultipartParameter("userID",stUserId)
                .addMultipartFile("image",f)
                .setOkHttpClient(okHttpClient)
                .setTag("update profile successfully")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("dvlkdvkxck", response.toString() );
                        dialog.hideDialog();

                        try {
                            if (response.getString("result").equals("Update successfully")){

                                String userID = response.getString("userID");
                                String email = response.getString("email");
                                String username = response.getString("userName");
                                String mobile = response.getString("userMobile");
                                String gender = response.getString("gender");
                                String dob = response.getString("dob");
                                String profile_image = response.getString("userImage");
                                String path = response.getString("path");


                                Log.e("ProfileActivity", "image: " + path + profile_image);


                                etName.setText(username);
                                txDob.setText(dob);
                                txtPhone.setText(mobile);
                                etGender.setText(gender);

                                if (!profile_image.equals("")) {
                                    try {

                                        Glide.with(getActivity()).load(path + profile_image).error(R.drawable.image1).into(img);
                                    } catch (Exception e) {
                                    }
                                } else {

                                }







                            }
                        } catch (JSONException e) {
                            Log.e("fdbcccccccxc", e.getMessage() );
                            dialog.hideDialog();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("dvdfv", anError.getMessage());
                        dialog.hideDialog();

                    }
                });


    }
}