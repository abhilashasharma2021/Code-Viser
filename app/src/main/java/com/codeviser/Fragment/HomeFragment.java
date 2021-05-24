package com.codeviser.Fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.codeviser.Activity.HelpActivity;
import com.codeviser.Activity.MainActivity;
import com.codeviser.Activity.SettingActivity;
import com.codeviser.Activity.SplashActivity;
import com.codeviser.Adapter.HomeAdapter;
import com.codeviser.Model.HomeModel;
import com.codeviser.R;
import com.codeviser.other.API_BaseUrl;
import com.codeviser.other.AppConstats.AppConstats;
import com.codeviser.other.AppConstats.SharedHelper;
import com.codeviser.other.ProgressBarCustom.CustomDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class HomeFragment extends Fragment {
    HomeAdapter homeAdapter;
    ArrayList<HomeModel> messageHomeModelArrayList = new ArrayList<>();
    RecyclerView recycleview_message;
    ImageView iv_settings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recycleview_message = view.findViewById(R.id.recycleview_message);

        recycleview_message.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));


        iv_settings=view.findViewById(R.id.iv_settings);
        iv_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getActivity(), iv_settings);
                popupMenu.getMenuInflater().inflate(R.menu.menus_file, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        if (menuItem.getItemId() == R.id.setting) {
                           startActivity(new Intent(getActivity(), SettingActivity.class));
                        }

                        else if (menuItem.getItemId() == R.id.logout){

                            logout();

                        }/*else if (menuItem.getItemId() == R.id.ip_address) {
                            final Dialog dialog = new Dialog(MainActivity.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setCancelable(true);
                            dialog.setContentView(R.layout.ip_popup);
                            edt_address=dialog.findViewById(R.id.edt_address);
                            btn_save=dialog.findViewById(R.id.btn_save);

                            if (IPADDRESS.equals("")){
                                edt_address.setHint("Enter ip address");
                            }else {
                                edt_address.setText(IPADDRESS);
                            }
                            btn_save.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    IPADDRESS=edt_address.getText().toString().trim();
                                    wfComm.initSocket(IPADDRESS, 9100);

                                }
                            });
                            dialog.show();
                        } else {
                            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                            alert.setMessage("Are you sure ?")
                                    .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Logout();
                                        }
                                    }).setNegativeButton("Cancel", null);
                            AlertDialog alert1 = alert.create();
                            alert1.show();
                        }*/
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
        home();
        return  view;
    }

    public void home() {
      String stUserId = SharedHelper.getKey(getActivity(), AppConstats.USERID);
        CustomDialog dialog = new CustomDialog();
        dialog.showDialog(R.layout.progress_layout, getActivity());

        AndroidNetworking.post(API_BaseUrl.BaseUrl + API_BaseUrl.show_group)
                .addBodyParameter("user_id","264 ")
                .setTag("Show Video and Image")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.hideDialog();
                        messageHomeModelArrayList=new ArrayList<>();
                        Log.e("bhfgtxbhgf", "onResponse: " +response);

                        try {
                            if (response.getString("result").equals("true")) {
                                JSONArray jsonArray = new JSONArray(response.getString("data"));
                                if (!response.getString("data").isEmpty()) {
                                    for (int i = 0; i <jsonArray.length() ; i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        String id=jsonObject.getString("id");
                                        String group_id=jsonObject.getString("group_id");
                                        String groups=jsonObject.getString("groups");
                                        JSONObject object=new JSONObject(groups);
                                        String last_message=object.getString("last_message");
                                        JSONObject jsonObject1=new JSONObject(last_message);

                                        HomeModel model=new HomeModel();


                                        model.setGroupID(jsonObject.getString("group_id"));
                                        model.setId(jsonObject.getString("id"));
                                        model.setName(object.getString("name"));
                                        model.setUserimage(object.getString("image"));
                                        model.setPath(object.getString("path"));
                                        Log.e("sxcxzvc", "name: " +object.getString("name"));
                                        Log.e("sxcxzvc", "path: " +object.getString("path"));
                                        Log.e("sxcxzvc", "image: " +object.getString("image"));
                                        Log.e("sxcxzvc", "message: " +jsonObject1.getString("message"));
                                        model.setLastTime(jsonObject1.getString("time"));
                                        model.setLastDate(jsonObject1.getString("dates"));
                                        model.setLastMsg(jsonObject1.getString("message"));
                                        messageHomeModelArrayList.add(model);
                                    }

                                    homeAdapter = new HomeAdapter(messageHomeModelArrayList, getActivity());
                                    recycleview_message.setAdapter(homeAdapter);
                                }
                            }
                        } catch (JSONException e) {
                            dialog.hideDialog();
                            Log.e("fdbgfb", "e: " +e);
                        }
                    }


                    @Override
                    public void onError(ANError anError) {
                        dialog.hideDialog();
                        Log.e("yhnjghmn", "onError: " +anError);
                    }
                });
    }

    public void logout() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.logout_dialog);
        Button btn_yes = dialog.findViewById(R.id.btn_yes);
        Button btn_no = dialog.findViewById(R.id.btn_no);

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedHelper.putKey(getActivity(), AppConstats.USERID, "");
                startActivity(new Intent(getActivity(), SplashActivity.class));
                getActivity().finish();
            }
        });

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }

}