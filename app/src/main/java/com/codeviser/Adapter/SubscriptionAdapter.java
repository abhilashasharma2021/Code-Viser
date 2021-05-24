package com.codeviser.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codeviser.Activity.SplashActivity;
import com.codeviser.Fragment.SubscribtionFragment;
import com.codeviser.Model.SubscriptionModel;
import com.codeviser.RozarPaymentIntegration.RazorPayImp;
import com.codeviser.databinding.RowsubscriptionlayoutBinding;
import com.razorpay.PaymentResultListener;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;


public class SubscriptionAdapter extends RecyclerView.Adapter<SubscriptionAdapter.MyViewHolder>  {


    private Context mContext;
    private ArrayList<SubscriptionModel> subscriptionList;
    RazorPayImp razorPayImp = new RazorPayImp();
    SubscribtionFragment subscribtionFragment;
    public SubscriptionAdapter(Context mContext, ArrayList<SubscriptionModel> subscriptionList,SubscribtionFragment fragment) {
        this.mContext = mContext;
        this.subscriptionList = subscriptionList;
        this.subscribtionFragment=fragment;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(RowsubscriptionlayoutBinding.inflate(LayoutInflater.from(parent.getContext()),parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SubscriptionModel modelObject = subscriptionList.get(position);

        if (modelObject !=null){
            holder.rowsubscriptionlayoutBinding.txTitle.setText(modelObject.getSubscriptionTitle());
            holder.rowsubscriptionlayoutBinding.txtDescription.setText(modelObject.getSubscriptionDescription());
            holder.rowsubscriptionlayoutBinding.txtPrice.setText(modelObject.getSubscriptionPrice());


            String subcription_status=modelObject.getSubscriptionStatus();
            /* 0=No subscription And 1== Subscribred*/

            if (subcription_status.equals("0")){
                holder.rowsubscriptionlayoutBinding.btnBuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        razorPayImp.startPayment(mContext, "100", "INR", "Code Viser");
                        subscribtionFragment.GetSubsId(modelObject.getSubscriptionID());
                    }
                });

            }
            else {

               holder.rowsubscriptionlayoutBinding.btnAlready.setVisibility(View.VISIBLE);
               holder.rowsubscriptionlayoutBinding.btnBuy.setVisibility(View.GONE);

            }



            Log.e("SubscriptionAdapter", "onBindViewHolder: " +modelObject.getPath()+modelObject.getSubscriptionImage());

            try {
                Glide.with(mContext).load(modelObject.getPath()+modelObject.getSubscriptionImage()).into(holder.rowsubscriptionlayoutBinding.ivSubscription);
            } catch (Exception e) {
                e.printStackTrace();
            }
            holder.rowsubscriptionlayoutBinding.txtValidity.setText(modelObject.getSubscriptionValidity());

        }

    }

    @Override
    public int getItemCount() {
        return subscriptionList == null ? 0 : subscriptionList.size();
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public static RowsubscriptionlayoutBinding rowsubscriptionlayoutBinding;
        public MyViewHolder(RowsubscriptionlayoutBinding rowsubscriptionlayoutBinding) {
            super(rowsubscriptionlayoutBinding.getRoot());
            this.rowsubscriptionlayoutBinding = rowsubscriptionlayoutBinding;
        }

    }
}
