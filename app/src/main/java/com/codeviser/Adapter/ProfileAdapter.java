package com.codeviser.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codeviser.Model.HomeModel;
import com.codeviser.Model.ProfileModel;
import com.codeviser.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {

    ArrayList<ProfileModel> profileModelArrayList;
    Context context;

    public ProfileAdapter(ArrayList<ProfileModel> profileModelArrayList, Context context) {
        this.profileModelArrayList = profileModelArrayList;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.profile, parent, false);
        return new ProfileAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProfileModel profileModel=profileModelArrayList.get(position);
      holder.txt_title.setText(profileModel.getTitle());
      holder.txt_name.setText(profileModel.getName());
      holder.user.setImageResource(profileModel.getImage());

    }

    @Override
    public int getItemCount() {
        return profileModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView user,edit;
         TextView txt_name,txt_title;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            user=itemView.findViewById(R.id.user);
            txt_name=itemView.findViewById(R.id.txt_name);
            txt_title=itemView.findViewById(R.id.txt_title);
        }
    }
}
