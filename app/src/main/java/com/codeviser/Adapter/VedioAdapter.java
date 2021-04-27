package com.codeviser.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codeviser.Model.HomeModel;
import com.codeviser.Model.VedioModel;
import com.codeviser.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VedioAdapter extends RecyclerView.Adapter<VedioAdapter.ViewHolder> {

    ArrayList<VedioModel> vedioModelArrayList;
    Context context;

    public VedioAdapter(ArrayList<VedioModel> vedioModelArrayList, Context context) {
        this.vedioModelArrayList = vedioModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public VedioAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.vedio, parent, false);
        return new VedioAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VedioAdapter.ViewHolder holder, int position) {
        VedioModel vedioModel=vedioModelArrayList.get(position);
        holder.image.setImageResource(R.drawable.image3);
    }

    @Override
    public int getItemCount() {
        return vedioModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.imgvideo);

        }
    }
}
