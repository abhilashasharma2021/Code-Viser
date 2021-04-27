package com.codeviser.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codeviser.Activity.MobileVerifyActivity;
import com.codeviser.Model.HomeModel;
import com.codeviser.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    ArrayList<HomeModel>homeModelArrayList;
    Context context;

    public SearchAdapter(ArrayList<HomeModel> homeModelArrayList, Context context) {
        this.homeModelArrayList = homeModelArrayList;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search, parent, false);
        return new SearchAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HomeModel homeModel=homeModelArrayList.get(position);
        holder.txt_name.setText(homeModel.getName());
        holder.txt_msg.setText(homeModel.getTitle());
        holder.relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, MobileVerifyActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return homeModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profile_image;
        TextView txt_name,txt_msg;
        RelativeLayout relative;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profile_image=itemView.findViewById(R.id.profile_image);
            txt_name=itemView.findViewById(R.id.txt_name);
            txt_msg=itemView.findViewById(R.id.txt_msg);
            relative=itemView.findViewById(R.id.relative);
        }
    }
}
