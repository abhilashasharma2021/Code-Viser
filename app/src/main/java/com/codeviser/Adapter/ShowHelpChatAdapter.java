package com.codeviser.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codeviser.Activity.ChatActivity;
import com.codeviser.Model.HomeModel;
import com.codeviser.Model.ShowHelpChatModel;
import com.codeviser.R;
import com.codeviser.other.AppConstats.AppConstats;
import com.codeviser.other.AppConstats.SharedHelper;

import java.util.ArrayList;

public class ShowHelpChatAdapter extends RecyclerView.Adapter<ShowHelpChatAdapter.ViewHolder> {


    ArrayList<ShowHelpChatModel> showHelpChatArrayList;
    Context context;

    public ShowHelpChatAdapter(ArrayList<ShowHelpChatModel> showHelpChatArrayList, Context context) {
        this.showHelpChatArrayList = showHelpChatArrayList;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rowshowhelpchat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ShowHelpChatModel modelObj = showHelpChatArrayList.get(position);
        Log.e("yukmk", "check: " + modelObj);

        if (modelObj != null) {

            Log.e("ggtfgtg", "onBindViewHolder: " + modelObj.getPath() + modelObj.getImage());
            try {
                Glide.with(context).load(modelObj.getPath() + modelObj.getImage()).into(holder.ivProfile);
            } catch (Exception e) {
                e.printStackTrace();
            }


           holder.tx_title.setText(modelObj.getMessage());

        }

    }

    @Override
    public int getItemCount() {
        return showHelpChatArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProfile;
        TextView tx_title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfile=itemView.findViewById(R.id.ivProfile);
            tx_title=itemView.findViewById(R.id.tx_title);


        }
    }
}
