package com.codeviser.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codeviser.Activity.ChatActivity;
import com.codeviser.Activity.MobileVerifyActivity;
import com.codeviser.Model.HomeModel;
import com.codeviser.R;
import com.codeviser.other.AppConstats.AppConstats;
import com.codeviser.other.AppConstats.SharedHelper;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {


    ArrayList<HomeModel> messageHomeModelArrayList;
    Context context;

    public HomeAdapter(ArrayList<HomeModel> messageHomeModelArrayList, Context context) {
        this.messageHomeModelArrayList = messageHomeModelArrayList;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HomeModel homeModel=messageHomeModelArrayList.get(position);


        if (!homeModel.equals("")){
            try {
                Glide.with(context).load(homeModel.getPath()+homeModel.getUserimage()).into(holder.profile_image);
            } catch (Exception e) {
                e.printStackTrace();
            }

            holder.txt_name.setText(homeModel.getName());
            holder.txt_msg.setText(homeModel.getLastMsg());
            holder.txtTime.setText(homeModel.getLastTime());
            holder.relative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedHelper.putKey(context, AppConstats.GroupId, homeModel.getGroupID());
               context.startActivity(new Intent(context, ChatActivity.class));
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return messageHomeModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profile_image;
        TextView txt_name,txt_msg,txtTime;
        RelativeLayout relative;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profile_image=itemView.findViewById(R.id.profile_image);
            txt_name=itemView.findViewById(R.id.txt_name);
            txt_msg=itemView.findViewById(R.id.txt_msg);
            relative=itemView.findViewById(R.id.relative);
            txtTime=itemView.findViewById(R.id.txtTime);

        }
    }
}
