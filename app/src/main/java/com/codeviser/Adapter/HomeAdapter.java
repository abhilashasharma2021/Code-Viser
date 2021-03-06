package com.codeviser.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codeviser.Activity.ChatActivity;
import com.codeviser.Activity.HelpChat;
import com.codeviser.Activity.MobileVerifyActivity;
import com.codeviser.Model.HomeModel;
import com.codeviser.R;
import com.codeviser.other.AppConstats.AppConstats;
import com.codeviser.other.AppConstats.SharedHelper;

import java.util.ArrayList;
import java.util.HashMap;

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
        HomeModel homemodelobj=messageHomeModelArrayList.get(position);
        Log.e("yukmk", "check: " +homemodelobj);

        if (homemodelobj !=null){

            Log.e("HomeAdapter", "onBindViewHolder: " +homemodelobj.getPath()+homemodelobj.getUserimage());
            try {
                Glide.with(context).load(homemodelobj.getPath()+homemodelobj.getUserimage()).into(holder.ivProfile);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.e("fdbfb", "onBindViewHolder: " +homemodelobj.getName());
            Log.e("fdbfb", "onBindViewHolder: " +homemodelobj.getLastMsg());

            holder.txt_name.setText(homemodelobj.getName());
            holder.txt_msg.setText(homemodelobj.getLastMsg());
            holder.txtTime.setText(homemodelobj.getLastTime());
           String status=homemodelobj.getStatus();

            Log.e("HomeAdapter", "status: " +status);

         if (status!=null){

             if (status.equals("1")){

                 holder.relative.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {


                    /*help_status==1 me send query
                    status=1 me admin  ne accept kiye*/
                         SharedHelper.putKey(context, AppConstats.GroupId, homemodelobj.getGroupID());
                         SharedHelper.putKey(context, AppConstats.GROUPTYPE, homemodelobj.getType());    /*type = 0 means  one way communication like  channel and type = 1  two way communication*/
                         SharedHelper.putKey(context, AppConstats.GROUPENAME, homemodelobj.getName());
                         SharedHelper.putKey(context, AppConstats.Status, homemodelobj.getStatus());
                         SharedHelper.putKey(context, AppConstats.GROUPEIMAGE, homemodelobj.getPath()+homemodelobj.getUserimage());

                         context.startActivity(new Intent(context, ChatActivity.class));
                     }
                 });
             }
             else {



             }



         }




        }

    }

    @Override
    public int getItemCount() {
        return messageHomeModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProfile;
        TextView txt_name,txt_msg,txtTime;
        RelativeLayout relative;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfile=itemView.findViewById(R.id.ivProfile);
            txt_name=itemView.findViewById(R.id.txt_name);
            txt_msg=itemView.findViewById(R.id.txt_msg);
            relative=itemView.findViewById(R.id.relative);
            txtTime=itemView.findViewById(R.id.txtTime);

        }
    }



}
