package com.codeviser.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codeviser.Adapter.HomeAdapter;
import com.codeviser.Adapter.SearchAdapter;
import com.codeviser.Model.HomeModel;
import com.codeviser.R;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    SearchAdapter searchAdapter;
    ArrayList<HomeModel> homeModelArrayList = new ArrayList<>();
    RecyclerView recycleview_search;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_search, container, false);
        recycleview_search = view.findViewById(R.id.recycleview_search);

        recycleview_search.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        searchAdapter = new SearchAdapter(homeModelArrayList, getActivity());
        recycleview_search.setAdapter(searchAdapter);
        search();
        return view;
    }

    public void search() {

        HomeModel homeModel= new HomeModel(R.drawable.circleimg,"Sonam","Hi");
        for (int i = 0; i < 5; i++) {
            homeModelArrayList.add(homeModel);
        }

    }
}