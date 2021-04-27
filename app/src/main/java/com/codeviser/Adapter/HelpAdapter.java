package com.codeviser.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.codeviser.Model.HelpModel;
import com.codeviser.R;
import com.codeviser.databinding.RowHelpLayoutBinding;
import com.codeviser.other.AppConstats.AppConstats;
import com.codeviser.other.AppConstats.SharedHelper;

import java.util.List;

public class HelpAdapter extends RecyclerView.Adapter<HelpAdapter.MyViewHolder> {


    private Context mContext;
    private List<HelpModel> helpList;

    public HelpAdapter(Context mContext, List<HelpModel> helpList) {
        this.mContext = mContext;
        this.helpList = helpList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(RowHelpLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        HelpModel modelObject = helpList.get(position);

        if (!modelObject.equals("")){
            holder.rowHelpLayoutBinding.txTitle.setText(modelObject.getTitle());
            holder.rowHelpLayoutBinding.txDescription.setText(modelObject.getDescription());

        }



    }

    @Override
    public int getItemCount() {
        return helpList == null ? 0 : helpList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private RowHelpLayoutBinding rowHelpLayoutBinding;

        public MyViewHolder(RowHelpLayoutBinding rowHelpLayoutBinding) {
            super(rowHelpLayoutBinding.getRoot());
            this.rowHelpLayoutBinding = rowHelpLayoutBinding;
        }

    }
}
