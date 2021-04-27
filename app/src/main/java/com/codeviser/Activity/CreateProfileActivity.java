package com.codeviser.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.codeviser.R;
import com.codeviser.databinding.ActivityCreateProfileBinding;
import com.codeviser.other.API_BaseUrl;
import com.codeviser.other.AppConstats.AppConstats;
import com.codeviser.other.AppConstats.SharedHelper;
import com.codeviser.other.ProgressBarCustom.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import es.dmoral.toasty.Toasty;

public class CreateProfileActivity extends AppCompatActivity {
    ActivityCreateProfileBinding binding;
    String strName = "", strDob = "",strEmail="",StrFinalStatus="";

    private static final String IMAGE_DIRECTORY = "/directory";
    private int GALLERY = 1, CAMERA = 2;
    File f;
    DatePickerDialog datePickerDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.rlDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(CreateProfileActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {


                                binding.txDob.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);

                                strDob = dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year;
                            }
                        }, mYear, mMonth, mDay);

                datePickerDialog.show();

            }
        });



        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                View radioButton = binding.radioGroup.findViewById(checkedId);
                int index = binding.radioGroup.indexOfChild(radioButton);

                Log.e("wedgdsgdf", index+"" );
                switch (index) {
                    case 0:
                        StrFinalStatus = "male";
                        break;
                    case 1:
                        StrFinalStatus = "Female";
                        break;
                }
            }
        });



        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strName = binding.etName.getText().toString().trim();
                strDob = binding.txDob.getText().toString().trim();
                strEmail = binding.etEmail.getText().toString().trim();

                if (TextUtils.isEmpty(strName)) {
                    Toast.makeText(CreateProfileActivity.this, "Please enter your Name", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(strDob)) {

                    Toast.makeText(CreateProfileActivity.this, "Please enter your Date of Birth", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(strEmail)) {

                    Toast.makeText(CreateProfileActivity.this, "Please enter email address", Toast.LENGTH_SHORT).show();
                } else if (StrFinalStatus.equals("")) {

                    Toast.makeText(CreateProfileActivity.this, "Please select gender", Toast.LENGTH_SHORT).show();
                }
                else {


                    createProfile(strName,strDob,strEmail,StrFinalStatus);
                }

            }
        });


        binding.rlProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPictureDialog();

            }
        });


    }

    public void showPictureDialog() {

        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    String path = saveImage(bitmap);

                    Log.e("ProfileActivity", "path: " + path);
                    Log.e("ProfileActivity", "bitmap: " + bitmap);
                    Toast.makeText(getApplicationContext(), "Image Saved!", Toast.LENGTH_SHORT).show();
                    binding.ivProfile.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            binding.ivProfile.setImageBitmap(thumbnail);
            saveImage(thumbnail);
            Toast.makeText(getApplicationContext(), "Image Saved!", Toast.LENGTH_SHORT).show();
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
            MediaScannerConnection.scanFile(getApplicationContext(),
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


    private void createProfile(String strName, String strDob,String strEmail,String StrFinalStatus) {


        CustomDialog dialog = new CustomDialog();
        dialog.showDialog(R.layout.progress_layout, this);
        String USERID = SharedHelper.getKey(getApplicationContext(), AppConstats.USERID);

       /* OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(netw)
                .build();*/
        AndroidNetworking.upload(API_BaseUrl.BaseUrl+API_BaseUrl.create_profile)
                .addMultipartParameter("userID",USERID)
                .addMultipartParameter("name",strName)
                .addMultipartParameter("dob",strDob)
                .addMultipartParameter("email",strEmail)
                .addMultipartParameter("gender",StrFinalStatus)
                .addMultipartFile("image",f)
                .setPriority(Priority.HIGH)
                .setTag("Sussessful")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("sdgfvdhbfdh ",response.toString());
                        dialog.hideDialog();
                        try {
                            if (response.getString("result").equals("true")){
                                String data=response.getString("data");

                                if (!data.isEmpty()){
                                    JSONObject jsonObject=new JSONObject(data);
                                    SharedHelper.putKey(getApplicationContext(), AppConstats.USERNAME, jsonObject.getString("userName"));
                                    SharedHelper.putKey(getApplicationContext(), AppConstats.USERPASSWORD, jsonObject.getString("user_password"));
                                    SharedHelper.putKey(getApplicationContext(), AppConstats.USEREMAIL, jsonObject.getString("email"));
                                    SharedHelper.putKey(getApplicationContext(), AppConstats.USERGENDER, jsonObject.getString("gender"));


                                    Toasty.success(CreateProfileActivity.this,response.getString("message"),Toasty.LENGTH_SHORT).show();
                                   // Toasty.success(ProfileActivity.this,"Successfully registered",Toasty.LENGTH_SHORT).show();
                                    startActivity(new Intent(CreateProfileActivity.this, MainActivity.class));

                                }

                            }
                            else {
                                Toasty.success(CreateProfileActivity.this,response.getString("message"),Toasty.LENGTH_SHORT).show();
                                dialog.hideDialog();
                            }
                        } catch (JSONException e) {
                            Log.e("dvfdvbf",e.getMessage());
                            dialog.hideDialog();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("grfthgftb",anError.getMessage());
                        dialog.hideDialog();


                    }
                });

    }
}