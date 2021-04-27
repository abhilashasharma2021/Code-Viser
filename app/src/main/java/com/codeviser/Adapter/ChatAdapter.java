package com.codeviser.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codeviser.Activity.AboutActivity;
import com.codeviser.Activity.MobileVerifyActivity;
import com.codeviser.Model.ChatModel;
import com.codeviser.Model.VedioModel;
import com.codeviser.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    ArrayList<ChatModel> chatModelArrayList;
    Context context;

    public ChatAdapter(ArrayList<ChatModel> chatModelArrayList, Context context) {
        this.chatModelArrayList = chatModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat, parent, false);
        return new ChatAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatModel chatModel=chatModelArrayList.get(position);
        holder.image.setImageResource(R.drawable.circleimg);
        holder.txt_name.setText(chatModel.getTitle());
      holder.relative.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              context.startActivity(new Intent(context, AboutActivity.class));
          }
      });
    }

    @Override
    public int getItemCount() {
        return chatModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView txt_name;
        RelativeLayout relative;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.image);
            txt_name=itemView.findViewById(R.id.txt_name);
            relative=itemView.findViewById(R.id.relative);
        }
    }
}
