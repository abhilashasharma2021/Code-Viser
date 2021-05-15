package com.codeviser.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codeviser.Activity.SplashActivity;
import com.codeviser.Model.SubscriptionModel;
import com.codeviser.databinding.RowsubscriptionlayoutBinding;

import java.util.ArrayList;


public class SubscriptionAdapter extends RecyclerView.Adapter<SubscriptionAdapter.MyViewHolder>{


    private Context mContext;
    private ArrayList<SubscriptionModel> subscriptionList;

    public SubscriptionAdapter(Context mContext, ArrayList<SubscriptionModel> subscriptionList) {
        this.mContext = mContext;
        this.subscriptionList = subscriptionList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(RowsubscriptionlayoutBinding.inflate(LayoutInflater.from(parent.getContext()),parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SubscriptionModel modelObject = subscriptionList.get(position);
        holder.rowsubscriptionlayoutBinding.txTitle.setText(modelObject.getSubscriptionTitle());
        holder.rowsubscriptionlayoutBinding.txtDescription.setText(modelObject.getSubscriptionDescription());
        holder.rowsubscriptionlayoutBinding.txtPrice.setText(modelObject.getSubscriptionPrice());

        Log.e("SubscriptionAdapter", "onBindViewHolder: " +modelObject.getPath()+modelObject.getSubscriptionImage());

        try {
            Glide.with(mContext).load(modelObject.getPath()+modelObject.getSubscriptionImage()).into(holder.rowsubscriptionlayoutBinding.ivSubscription);
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.rowsubscriptionlayoutBinding.txtValidity.setText(modelObject.getSubscriptionValidity());

    }

    @Override
    public int getItemCount() {
        return subscriptionList == null ? 0 : subscriptionList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private RowsubscriptionlayoutBinding rowsubscriptionlayoutBinding;
        public MyViewHolder(RowsubscriptionlayoutBinding rowsubscriptionlayoutBinding) {
            super(rowsubscriptionlayoutBinding.getRoot());
            this.rowsubscriptionlayoutBinding = rowsubscriptionlayoutBinding;
        }

    }
}
