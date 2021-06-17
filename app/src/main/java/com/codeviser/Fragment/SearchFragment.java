package com.codeviser.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.codeviser.Adapter.SearchAdapter;
import com.codeviser.Model.SearchModel;
import com.codeviser.R;
import com.codeviser.other.API_BaseUrl;
import com.codeviser.other.AppConstats.AppConstats;
import com.codeviser.other.AppConstats.SharedHelper;
import com.codeviser.other.ProgressBarCustom.CustomDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class SearchFragment extends Fragment {
    SearchAdapter searchAdapter;
    ArrayList<SearchModel> searchModelArrayList = new ArrayList<>();
    RecyclerView recycleview_search;
    EditText etSearch;
    String strWord="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_search, container, false);
        recycleview_search = view.findViewById(R.id.recycleview_search);
        etSearch = view.findViewById(R.id.etSearch);

        recycleview_search.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        searchAdapter = new SearchAdapter(searchModelArrayList, getActivity());
        recycleview_search.setAdapter(searchAdapter);

         etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                strWord=String.valueOf(s);
                search(strWord);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        search(strWord);
        return view;
    }

    public void search(String strWord) {
   String  stUserId = SharedHelper.getKey(getActivity(), AppConstats.USERID);
        CustomDialog dialog=new CustomDialog();
        dialog.showDialog(R.layout.progress_layout,getActivity());

        Log.e("dlvkdlv", stUserId );
        AndroidNetworking.post(API_BaseUrl.BaseUrl + API_BaseUrl.search_message)
                .addBodyParameter("user_id",stUserId)
                .addBodyParameter("word", strWord)
                .setTag("")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("regrt", response.toString() );
                        dialog.hideDialog();
                        searchModelArrayList=new ArrayList<>();
                        try {
                            String chat=response.getString("chat");
                            if (!chat.isEmpty()){
                                JSONArray jsonArray=new JSONArray();
                                for (int i = 0; i <jsonArray.length() ; i++) {
                                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                                    String id=jsonObject.getString("id");
                                    String group_id=jsonObject.getString("group_id");
                                    String message_search=jsonObject.getString("message");
                                    String dates=jsonObject.getString("dates");
                                    String time=jsonObject.getString("time");
                                    String path=jsonObject.getString("path");
                                    String last_message=jsonObject.getString("last_message");
                                    JSONObject jsonObject1=new JSONObject(last_message);
                                    String message_show=jsonObject1.getString("message");
                                    String group=jsonObject.getString("group");
                                    JSONObject object=new JSONObject(group);
                                    String name=object.getString("name");
                                    String image=object.getString("image");


                                    SearchModel model=new SearchModel();
                                    model.setId(jsonObject.getString("id"));
                                    model.setGroupId(jsonObject.getString("group_id"));
                                    model.setLastMsg(jsonObject1.getString("message"));
                                    model.setName(object.getString("name"));
                                    searchModelArrayList.add(model);
                                }
                                searchAdapter = new SearchAdapter(searchModelArrayList, getActivity());
                                recycleview_search.setAdapter(searchAdapter);
                            }

                        } catch (JSONException e) {
                            Log.e("dsvdsfv", e.getMessage() );
                            dialog.hideDialog();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("rgtrhbt", anError.getMessage() );
                        dialog.hideDialog();

                    }
                });

    }
    }
