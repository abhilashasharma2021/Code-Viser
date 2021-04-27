package com.codeviser.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codeviser.Adapter.SearchAdapter;
import com.codeviser.Adapter.VedioAdapter;
import com.codeviser.Model.ChatModel;
import com.codeviser.Model.HomeModel;
import com.codeviser.Model.VedioModel;
import com.codeviser.R;

import java.util.ArrayList;


public class ImageVideoFragment extends Fragment {

    VedioAdapter vedioAdapter;
    ArrayList<VedioModel> vedioModelArrayList = new ArrayList<>();
    RecyclerView recycleVideo,recycleview;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_image_video, container, false);
        recycleVideo = view.findViewById(R.id.recycleVideo);
        recycleview = view.findViewById(R.id.recycleview);


        recycleVideo.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recycleview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        vedioAdapter= new VedioAdapter(vedioModelArrayList, getActivity());
        recycleVideo.setAdapter(vedioAdapter);
        vedio();
        return view;
    }


    public void vedio() {

        VedioModel vedioModel=new VedioModel(R.drawable.image3);
        for (int i = 0; i < 1; i++) {
            vedioModelArrayList.add(vedioModel);
        }

    }
}